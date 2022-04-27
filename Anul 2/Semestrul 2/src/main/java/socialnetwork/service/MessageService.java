package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.MessageValidationException;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.MessageSentEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements Observable<MessageSentEvent> {
    private Repository<Tuple<Long, Long>, Prietenie> repo;
    private Repository<Long, Utilizator> repoU;
    private Repository<Long, Message> repoM;
    private UtilizatorService servU;

    List<Observer<MessageSentEvent>> observers=new ArrayList<>();

    /**
     * Constructorul clasei
     * @param repo - Repo-ul de prietenii
     * @param repoU - Repo-ul de utilizatori
     * @param repoM - Repo-ul de mesaje
     */
    public MessageService(Repository<Tuple<Long, Long>, Prietenie> repo, Repository<Long, Utilizator> repoU, Repository<Long, Message> repoM, UtilizatorService servU) {
        this.repo = repo;
        this.repoU = repoU;
        this.repoM = repoM;
        this.servU =servU;

    }


    /**
     * Metoda care returneaza toate mesajele
     * @return toate mesajele din repo
     */
    public Iterable<Message> getAllMessages(){
        return repoM.findAll();
    }


    /**
     * Metoda care returneaza mesajele unui user (trimise/primite)
     * @param username - username-ul utilizatorului dat ca si parametru
     * @return toate mesajele pentru user
     */
    public Iterable<Message> getAllMessagesForAUser(String username){
        Iterable<Message> lista = repoM.findAll();
        List<Message> userMessageList = new ArrayList<>();
        for (Message m : lista) {
            Long idUser = servU.getUtilizatorByUserName(username).getId();
            if (m.getFrom().getId() == idUser)
                userMessageList.add(m);
            for(Utilizator mTo : m.getTo()) {
                if(mTo.getId() == idUser) {
                    userMessageList.add(m);
                }
            }
        }
        return userMessageList;
    }


    /**
     * Metoda care adauga un mesaj
     * @param message - mesajul de adaugat
     * @return null
     */
    public Message addMessage(Message message) {
        try{
            Message message1=repoM.save(message);
            if(message1!=null){
                notifyObservers(new MessageSentEvent(ChangeEventType.MESSAGE,message1));
            }
            return message1;
        }
        catch(MessageValidationException e) {throw new ServiceException(e.getMessage());}
    }


    /**
     * Metoda care raspunde unui mesaj existent
     * @msg - mesajul cu care facem reply
     * @return null
     * @throws ServiceException - daca mesajul nu exista
     */
    public Message replyToMessage( Message msg){
        //Long idMessage, String username, String text
        Long idMessage = msg.getOriginalMessage();
        String username = msg.getFrom().getUserName();
        String text = msg.getMessage();
        Message message = repoM.findOne(idMessage);
        Message replyMessage = null;
        Utilizator u = servU.getUtilizatorByUserName(username);

        if(message == null) {
            throw new ServiceException("Nu exista acest mesaj!");
        }
        else{
            if (!message.getTo().contains(u)){
                throw new ServiceException("You cannot reply to this message! It doesn't belong to you!");
            }
            else {
                //Utilizator from = repoU.findOne(idUser);
                List<Utilizator> to = new ArrayList<>();
                to.add(message.getFrom()); // cine face reply
                replyMessage = new Message(u, LocalDateTime.now(), to, text);
                replyMessage.setOriginalMessage(idMessage);
                Message message1=repoM.save(replyMessage);

                if(message1!=null){
                    notifyObservers(new MessageSentEvent(ChangeEventType.MESSAGE,message1));
                }
                else return null;
            }
        }
        return null;

    }

    public Message replyToAll(Message msg){
        //Long idMessage, String username, String text
        Long idMessage = msg.getOriginalMessage();
        String username = msg.getFrom().getUserName();
        String text = msg.getMessage();
        Message message = repoM.findOne(idMessage);
        Message replyAll = null;
        Utilizator u = servU.getUtilizatorByUserName(username);

        if(message == null) {
            throw new ServiceException("Nu exista acest mesaj!");
        }
        else{
            if (!message.getTo().contains(u)){
                throw new ServiceException("You cannot reply to this message! It doesn't belong to you!");
            }
            else{
                List<Utilizator> to = new ArrayList<>();
                for (Utilizator user : message.getTo()){
                    if (!user.equals(u)){
                        to.add(user);
                    }
                }
                to.add(message.getFrom());
                replyAll = new Message(u, LocalDateTime.now(), to, text);
                replyAll.setOriginalMessage(idMessage);
                Message message1= repoM.save(replyAll);
                if(message1!=null){
                    notifyObservers(new MessageSentEvent(ChangeEventType.MESSAGE,message1));
                }
                else return null;

            }

        }
        return null;
    }

    /**
     * Metoda care returneaza conversatiile a doi utilizatori
     * @param user1 - username-ul primului user
     * @param user2 - username-ul celui de-al doilea user
     * @return lista de conversatii
     */
    public List<Message> getConversations(String user1, String user2){

        List<Message> messageList=new ArrayList<>();
        Long id1 = servU.getUtilizatorByUserName(user1).getId();
        Long id2 = servU.getUtilizatorByUserName(user2).getId();
        for(Message m:repoM.findAll()){

            if((m.getTo().contains(repoU.findOne(id1)) && m.getFrom().getId()==id2)
                    ||m.getTo().contains(repoU.findOne(id2)) && m.getFrom().getId()==id1)
                messageList.add(m);
        }

        if(messageList.size()!=0)
        {
            List<Message> ListaNoua= messageList.stream()
                    .sorted(Comparator.comparing(Entity::getId)).collect(Collectors.toList());
            return ListaNoua;
        }
        else
            throw new ServiceException("Nu exista conversatii intre acesti utilizatori!");

    }


    /**
     * Metoda care returneaza un mesajul dupa id
     * @param id1 - id-ul mesajului
     * @return mesaj
     */
    public Message getMessage(Long id1){
        return repoM.findOne(id1);
    }

    @Override
    public void addObserver(Observer<MessageSentEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageSentEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageSentEvent t) {
        observers.stream().forEach(x->x.update(t));
    }


}
