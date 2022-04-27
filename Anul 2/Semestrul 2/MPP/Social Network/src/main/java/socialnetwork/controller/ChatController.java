package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import socialnetwork.domain.Message;
import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.UserFriendshipDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.MessageSentEvent;
import socialnetwork.utils.observer.Observer;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController implements Observer<MessageSentEvent> {
    private UtilizatorService serviceU;
    private MessageService serviceM;
    private PrietenieService serviceP;
    private long idUser;
    private List<Utilizator> to=new ArrayList<>();

    //ObservableList<Utilizator> model = FXCollections.observableArrayList();

    ObservableList<UserFriendshipDTO> model = FXCollections.observableArrayList();
    ObservableList<MessageDTO> model2 = FXCollections.observableArrayList();

    private List<UserFriendshipDTO> selectedUsersToChat;

    @FXML
    private Button buttonSendMessage;
    @FXML
    private ListView<Message> listViewChat; //??DTO


    @FXML
    TableView<UserFriendshipDTO> tableView;
    @FXML
    TableColumn<UserFriendshipDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<UserFriendshipDTO,String> tableColumnLastName;
    @FXML
    TableView<MessageDTO> tableView2;
    @FXML
    TableColumn<MessageDTO,String> tableColumnMesaj;
    @FXML
    TextField textFieldTypeMessage;

    @FXML
    TableColumn<Utilizator, String> tableColumnUsername;

    public void setService(UtilizatorService serviceU, MessageService serviceM, PrietenieService serviceP, long idUser) {
        this.serviceU = serviceU;
        this.serviceM=serviceM;
        this.serviceP= serviceP;
        this.idUser=idUser;
        serviceM.addObserver(this);
        initModel();
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    private void initModel() {
//        List<Utilizator> list= StreamSupport.stream(serviceU.getAll().spliterator(),false)
//                .filter(x->(x.getId()!=idUser))
//                .collect(Collectors.toList());
//        model.setAll(list);
        List<UserFriendshipDTO> list= StreamSupport.stream(serviceP.getAll().spliterator(),false)
                .filter(x->(x.getId().getLeft()==serviceU.getUser(idUser).getId() || x.getId().getRight()==serviceU.getUser(idUser).getId()))
                .map(x->{
                    Utilizator user1=serviceU.getUser(x.getId().getLeft());
                    Utilizator user2=serviceU.getUser(x.getId().getRight());
                    UserFriendshipDTO userFriendshipDTO;
                    if(user1.getId().compareTo(serviceU.getUser(idUser).getId())==0)
                        userFriendshipDTO=new UserFriendshipDTO(user2.getId(), user2.getUserName(), user2.getFirstName(),user2.getLastName(),x.getDate());
                    else
                        userFriendshipDTO=new UserFriendshipDTO(user1.getId(), user1.getUserName(), user1.getFirstName(),user1.getLastName(),x.getDate());
                    return userFriendshipDTO;
                })
                .collect(Collectors.toList());
        model.setAll(list);


    }

    @FXML
    public void initialize() {
        //tabela de prieteni
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tableView.setItems(model);
        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);


        //tabela de mesaje
        tableColumnMesaj.setCellValueFactory(new PropertyValueFactory<MessageDTO,String>("mesajTabel"));
        tableView2.setItems(model2);
        tableView2.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

    }

    @FXML
    public void handleChat() {

        //List<Utilizator> newSelectedUsersToChat = tableView.getSelectionModel().getSelectedItems();
        List<UserFriendshipDTO> newSelectedUsersToChat = tableView.getSelectionModel().getSelectedItems();
        tableView2.getItems().clear();
        selectedUsersToChat = newSelectedUsersToChat;


        initModel2(selectedUsersToChat);

        buttonSendMessage.setText("Send");

            if(selectedUsersToChat.size()>1)
                tableColumnMesaj.setText("Group chat");
            else
                tableColumnMesaj.setText("Chat");



    }

    @FXML
    void handleChangeButton(MouseEvent event) {
        if (tableView2.getSelectionModel().getSelectedItem()==null) {
            buttonSendMessage.setText("Send");
        } else {
            buttonSendMessage.setText("Reply");
        }
    }

    @FXML
    public void handleSend() {
        selectedUsersToChat = tableView.getSelectionModel().getSelectedItems();

        MessageDTO selected = tableView2.getSelectionModel().getSelectedItem();
        if (selected == null){ //send simplu

            try {
                List<Utilizator>usersToChat = new ArrayList<>();
                for (UserFriendshipDTO u: selectedUsersToChat){
                    usersToChat.add(serviceU.getUser(u.getId()));
                }
                Message msg = new Message(serviceU.getUser(idUser), LocalDateTime.now(), usersToChat, textFieldTypeMessage.getText());
                msg.setOriginalMessage(null);
                serviceM.addMessage(msg);

            }
            catch (ServiceException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Trimitere esuata!", "Nu puteti trimite acest mesaj!");
            }

    }
        //reply 1 pers
        else if(selectedUsersToChat.size()==1) {
            try {
                List<Utilizator>usersToChat = new ArrayList<>();
                for (UserFriendshipDTO u: selectedUsersToChat){
                    usersToChat.add(serviceU.getUser(u.getId()));
                }
                Message msg = new Message(serviceU.getUser(idUser), LocalDateTime.now(), usersToChat, textFieldTypeMessage.getText());
                msg.setOriginalMessage(selected.getId());
                // serviceM
                //serviceM.replyToMessage(selected.getId(), serviceU.getUser(idUser).getUserName(), textFieldTypeMessage.getText());
                serviceM.replyToMessage(msg);

            } catch (ServiceException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Trimitere esuata!","Nu puteti raspunde la acest mesaj!");
            }
        }

        else { //reply group
            try {
                List<Utilizator>usersToChat = new ArrayList<>();
                for (UserFriendshipDTO u: selectedUsersToChat){
                    usersToChat.add(serviceU.getUser(u.getId()));
                }
                Message msg1 = new Message(serviceU.getUser(idUser), LocalDateTime.now(), usersToChat, textFieldTypeMessage.getText());
                msg1.setOriginalMessage(selected.getId());

                //serviceM.replyToAll(selected.getId(), serviceU.getUser(idUser).getUserName(), textFieldTypeMessage.getText());
                serviceM.replyToAll(msg1);
            } catch (ServiceException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Trimitere esuata!", "Nu puteti raspunde la acest mesaj!");
            }
        }

        textFieldTypeMessage.setText("");
        buttonSendMessage.setText("Send");
        initModel2(selectedUsersToChat); //de fapt refresh
    }




    private void initModel2(List<UserFriendshipDTO> selected){

        List<Message> displayedMessages= new ArrayList<>();

        List<Utilizator> allUsersInChat2= new ArrayList<>();
        for(UserFriendshipDTO u : selected)
            allUsersInChat2.add(serviceU.getUser(u.getId()));

        Utilizator nou2=serviceU.getRepo().findOne(idUser);
        allUsersInChat2.add(nou2);

        for(Message m: serviceM.getAllMessages())
        {
            //userii pt un mesaj aleator
            List<Utilizator> allUsersInChat= new ArrayList<>();
            for(Utilizator u : m.getTo())
                allUsersInChat.add(u);
            allUsersInChat.add(m.getFrom());




            boolean ok=true;
            for(Utilizator u: allUsersInChat)
                if (!allUsersInChat2.contains(u))
                    ok=false;

            for(Utilizator u: allUsersInChat2)
                if (!allUsersInChat.contains(u))
                    ok=false;
            if(ok==true) displayedMessages.add(m);
        }

        if(displayedMessages.size()!=0)
        { List<Message> ListaNoua=displayedMessages.stream().sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

            model2.setAll(StreamSupport.stream(ListaNoua.spliterator(),false)

                    .map(x->{
                        MessageDTO msg;

                        if(x.getOriginalMessage()==0){
                            msg=new MessageDTO(x.getId(),serviceU.getUser(x.getFrom().getId()).getLastName()+" "+serviceU.getUser(x.getFrom().getId()).getFirstName()+" at " + x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ": "+x.getMessage());}
                        else{

                            msg=new MessageDTO(x.getId(),serviceU.getUser(x.getFrom().getId()).getLastName()+" "+serviceU.getUser(x.getFrom().getId()).getFirstName()+" replied to '"+serviceM.getMessage(x.getOriginalMessage()).getMessage()+"': "+x.getMessage() + " \n at " + x.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));}
                        return msg;

                    })
                    .collect(Collectors.toList()));
        }

      else
        {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Chat gol!","Nu exista inca mesaje in acest chat!");

        }
    }




    @Override
    public void update(MessageSentEvent messageSentEvent) {
        initModel2(selectedUsersToChat);
    }




}
