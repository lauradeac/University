package socialnetwork.service;


import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.UserFriendshipDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.FriendshipsChangeEvent;
import socialnetwork.utils.observer.Observable;

//import sun.nio.ch.Util;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipsChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

public class PrietenieService  implements Observable<FriendshipsChangeEvent>  {
    private Repository<Tuple<Long, Long>, Prietenie> repo;
    private Repository<Long, Utilizator> repoU;
    private UtilizatorService serv_u;
    private List<Observer<FriendshipsChangeEvent>> observers=new ArrayList<>();


    public PrietenieService(Repository<Tuple<Long, Long>, Prietenie> repo, Repository<Long, Utilizator> repoU) {
        this.repo = repo;
        this.repoU = repoU;
        //load friends(adaug in listele de prieteni )
        for (Prietenie p : this.repo.findAll()) {
            Long id1 = p.getId().getLeft();
            Long id2 = p.getId().getRight();
            this.repoU.findOne(id1).addFriend(this.repoU.findOne(id2));
            this.repoU.findOne(id2).addFriend(this.repoU.findOne(id1));
        }
        this.serv_u = serv_u;
    }

    public boolean suntPrieteni(Long id1, Long id2) {
        for (Prietenie p : repo.findAll())
            if ((p.getId().getRight() == id1 && p.getId().getLeft() == id2) || (p.getId().getRight() == id2 && p.getId().getLeft() == id1))
                return true;
        return false;
    }

    public Prietenie getPrietenie(Long id1, Long id2)
    {
        for (Prietenie p : repo.findAll())
            if ((p.getId().getRight() == id1 && p.getId().getLeft() == id2) || (p.getId().getRight() == id2 && p.getId().getLeft() == id1))
                return p;
            return null;
    }

    public List<UserFriendshipDTO> userFriendships(String username) {
        Long id = serv_u.getUtilizatorByUserName(username).getId();
        if (repoU.findOne(id) == null) {
            throw new ServiceException("User does not exist!\n");
        }
        List<UserFriendshipDTO> list = StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(x -> (x.getId().getLeft() == id || x.getId().getRight() == id))
                .map(x -> {
                    Utilizator user1 = repoU.findOne(x.getId().getLeft());
                    Utilizator user2 = repoU.findOne(x.getId().getRight());
                    UserFriendshipDTO userFriendshipDTO;
                    if (user1.getId().compareTo(id) == 0)
                        userFriendshipDTO = new UserFriendshipDTO(user2.getId(), user2.getUserName(), user2.getFirstName(), user2.getLastName(), x.getDate());
                    else
                        userFriendshipDTO = new UserFriendshipDTO(user1.getId(), user1.getUserName(), user1.getFirstName(), user1.getLastName(), x.getDate());
                    return userFriendshipDTO;
                })
                .collect(Collectors.toList());
        return list;
    }

    public List<UserFriendshipDTO> userFriendshipsMonth(Long id, Month month) {
        if (repoU.findOne(id) == null) {
            throw new ServiceException("User does not exist!\n");
        }
        List<UserFriendshipDTO> list = StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(x -> ((x.getId().getLeft() == id || x.getId().getRight() == id) && x.getDate().getMonth() == month))
                .map(x -> {
                    Utilizator user1 = repoU.findOne(x.getId().getLeft());
                    Utilizator user2 = repoU.findOne(x.getId().getRight());
                    UserFriendshipDTO userFriendshipDTO;
                    if (user1.getId().compareTo(id) == 0)
                        userFriendshipDTO = new UserFriendshipDTO(user2.getId(), user2.getUserName(), user2.getFirstName(), user2.getLastName(), x.getDate());
                    else
                        userFriendshipDTO = new UserFriendshipDTO(user1.getId(),user1.getUserName(), user1.getFirstName(), user1.getLastName(), x.getDate());
                    return userFriendshipDTO;
                })
                .collect(Collectors.toList());
        return list;
    }

    public Repository<Tuple<Long, Long>, Prietenie> getRepo() {
        return repo;
    }

    /**
     * Adaug o noua prietenie
     *
     * @param id1 id-ul utilizatorului1
     * @param id2 id-ul utilizatorului2
     * @return obiectul de tip prietenie adaugat
     */
    public Prietenie addPrieten(Long id1, Long id2, int an, int luna, int zi) {
        if (repoU.findOne(id1) == null || repoU.findOne(id2) == null)
            throw new ServiceException("Utilizatori invalizi!");
        //Tuple<Long, Long> id = new Tuple<>(id1, id2);
        //Tuple<Long, Long> idd = new Tuple<>(id2, id1);

        if(suntPrieteni(id1,id2) || suntPrieteni(id2, id1)){
            throw new ServiceException("Aceasta prietenie exista deja!");
        }

        try {
            Prietenie pr = new Prietenie(id1, id2, an, luna, zi);
            Prietenie p = repo.save(pr);
            notifyObservers(new FriendshipsChangeEvent(ChangeEventType.ACCEPT, p));
            repoU.findOne(id1).addFriend(repoU.findOne(id2));//
            repoU.findOne(id2).addFriend(repoU.findOne(id1));//
            return p;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Sterg o prietenie
     *
     * @param id1 id-ul utilizatorului1
     * @param id2 id-ul utilizatorului2
     * @return obiectul de tip prietenie sters
     */
    public Prietenie removePrieten(Long id1, Long id2) {
        if (repoU.findOne(id1) == null || repoU.findOne(id2) == null)
            throw new ServiceException("Utilizatori invalizi!");
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        Prietenie pr = repo.findOne(id);
        if (pr != null) {
            Prietenie p = repo.delete(pr.getId());
            notifyObservers(new FriendshipsChangeEvent(ChangeEventType.DELETE, p));
            repoU.findOne(p.getId().getLeft()).removeFriend(repoU.findOne(p.getId().getRight()));
            repoU.findOne(p.getId().getRight()).removeFriend(repoU.findOne(p.getId().getLeft()));
            return p;
        } else {
            throw new ServiceException("Aceasta prietenie nu exista!");
        }
    }

    /**
     * @return toate el de tip Prietenie
     */
    public Iterable<Prietenie> getAll() {
        return repo.findAll();
    }

    public Prietenie createPrietenie(String username1, String username2, int an , int luna, int zi) {
        Long idUser1 = null;
        Long idUser2 = null;
        Prietenie newPrietenie;

        if (serv_u.existUtilizator(username1)) {
            idUser1 = serv_u.getUtilizatorByUserName(username1).getId();
        }
        if (serv_u.existUtilizator(username2)) {
            idUser2 = serv_u.getUtilizatorByUserName(username2).getId();
        }

        newPrietenie = new Prietenie(idUser1, idUser2, an, luna, zi);
        return newPrietenie;
    }

    public Prietenie createPrietenie(String username1, String username2) {
        Long idUser1 = null;
        Long idUser2 = null;
        Prietenie newPrietenie;

        if (serv_u.existUtilizator(username1)) {
            idUser1 = serv_u.getUtilizatorByUserName(username1).getId();
        }
        if (serv_u.existUtilizator(username2)) {
            idUser2 = serv_u.getUtilizatorByUserName(username2).getId();
        }

        newPrietenie = new Prietenie(idUser1, idUser2, LocalDate.now());
        return newPrietenie;
    }

    @Override
    public void addObserver(Observer<FriendshipsChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<FriendshipsChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipsChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}



