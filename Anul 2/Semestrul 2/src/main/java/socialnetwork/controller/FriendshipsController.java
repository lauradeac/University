package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.UserFriendshipDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.FriendshipsChangeEvent;
import socialnetwork.utils.observer.Observer;
//import sun.nio.ch.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsController implements Observer<FriendshipsChangeEvent> {
    private UtilizatorService serviceU;
    private PrietenieService serviceP;
    private FriendRequestService serviceC;
    private MessageService serviceM;
    private String  username;
    private Long userId;

    ObservableList<UserFriendshipDTO> model = FXCollections.observableArrayList();
    ObservableList<Utilizator> model2 = FXCollections.observableArrayList();

    @FXML
    private ImageView imageView1;

    @FXML
    TableView<UserFriendshipDTO> tableView;
    @FXML
    TableView<Utilizator> tableView2;
    @FXML
    TableColumn<UserFriendshipDTO,String> tableColumnFirstName;
    @FXML
    TableColumn<UserFriendshipDTO,String> tableColumnLastName;
    @FXML
    TableColumn<UserFriendshipDTO, LocalDate> tableColumnData;
    @FXML
    TableColumn<UserFriendshipDTO, String> tableColumnUsername;

    @FXML
    TextField textFieldSearch;

    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName2;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName2;
    @FXML
    TableColumn<Utilizator,String> tableColumnUsername2;




    public void setFriendshipsService(UtilizatorService serviceU, PrietenieService serviceP, FriendRequestService serviceC, MessageService serviceM,String username) {


        this.serviceU = serviceU;
        this.serviceP=serviceP;
        this.serviceC=serviceC;
        this.serviceM=serviceM;
        this.username=username;
        this.userId=serviceU.getUtilizatorByUserName(username).getId();
        imageView1=new ImageView();

       imageView1.setImage(new Image("https://cdn.imgbin.com/3/12/17/imgbin-computer-icons-avatar-user-login-avatar-man-wearing-blue-shirt-illustration-mJrXLG07YnZUc2bH5pGfFKUhX.jpg"));



        serviceP.addObserver(this);
        initModel();
    }

    private void initModel() {
        List<UserFriendshipDTO> list= StreamSupport.stream(serviceP.getAll().spliterator(),false)
                .filter(x->(x.getId().getLeft()==serviceU.getUtilizatorByUserName(username).getId() || x.getId().getRight()==serviceU.getUtilizatorByUserName(username).getId()))
                .map(x->{
                    Utilizator user1=serviceU.getUser(x.getId().getLeft());
                    Utilizator user2=serviceU.getUser(x.getId().getRight());
                    UserFriendshipDTO userFriendshipDTO;
                    if(user1.getId().compareTo(serviceU.getUtilizatorByUserName(username).getId())==0)
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
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, String>("userName"));

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, String>("lastName"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<UserFriendshipDTO, LocalDate>("date"));

        tableView.setItems(model);

        tableColumnFirstName2.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnLastName2.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableColumnUsername2.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("userName"));
        tableView2.setItems(model2);

    }
    /*
    STERGEM UN PRIETEN:
    1. Se sterge din tabel ptc apelam initModel
    2. Se va sterge si tabelele unde apare cererea de prietenie acceptata (ca sa poata fi trimisa iar).
     */

    @FXML
    public void handleDelete(){
        UserFriendshipDTO selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        if(selected!=null)
            try{
                this.serviceP.removePrieten(serviceU.getUtilizatorByUserName(username).getId(),selected.getId());

                Utilizator u1=serviceU.getUtilizator(userId);
                Utilizator u2=serviceU.getUtilizator(selected.getId());
                FriendRequest fr =new FriendRequest(u1, u2,1,1,1);
                FriendRequest fr2 =new FriendRequest(u2, u1,1,1,1);

                if(serviceC.getFriendRequest(fr.getId())!=null)
                    this.serviceC.deleteFriendRequest(fr.getId());
                else
                    this.serviceC.deleteFriendRequest(fr2.getId());

                initModel();


            }
            catch(ServiceException e){
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
        }

    }
    @FXML
    private void handleAdd(){
        Utilizator selected = tableView2.getSelectionModel().getSelectedItem(); //luam elementul selectat


        if(selected!=null) {
            if (serviceP.suntPrieteni(serviceU.getUtilizatorByUserName(username).getId(), selected.getId())) {
                MessageAlert.showErrorMessage(null, "Sunteti deja prieten cu acest utilizator!");
            } else {
                if (selected.getId() == userId)

                    MessageAlert.showErrorMessage(null, "Nu va puteti imprieteni cu propriul utilizator!");


                else {
                    FriendRequest fr = new FriendRequest(serviceU.getUser(serviceU.getUtilizatorByUserName(username).getId()), selected, LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                    if (serviceC.getFriendRequest(fr.getId()) != null) {
                        MessageAlert.showErrorMessage(null, "Cerere de prietenie deja in asteptare!");
                    } else {
                        try {
                            this.serviceC.addFriendRequest(fr);
                            initModel();
                        } catch (ServiceException e) {
                            MessageAlert.showErrorMessage(null, e.getMessage());
                        }
                    }

                }
            }
        }

        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
        }

    }

    @FXML
    private void handleType(){
        String filter=textFieldSearch.getText();
        Iterable<Utilizator> users=serviceU.getAll();
        model2.setAll(StreamSupport.stream(users.spliterator(),false)
                .filter(x->(x.getUserName().startsWith(filter) || x.getFirstName().startsWith(filter)||x.getFirstName().startsWith(filter)||x.getLastName().startsWith(filter))&&x.getId()!=serviceU.getUtilizatorByUserName(username).getId()
                            || (x.getFirstName()+" "+x.getLastName()).startsWith(filter))
                .collect(Collectors.toList())
        );
    }

    public void showFriendRequestsDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/friendRequests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Your friend requests:");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendRequestsController frController = loader.getController();

            frController.setService(serviceC,serviceU,serviceP,username);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleFR(){
        showFriendRequestsDialog();
    }

    @FXML
    private void handleSFR(){
        showSentFriendRequestsDialog();
    }

    private void showSentFriendRequestsDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/sentFriendRequests.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            textFieldSearch.setText("");
            dialogStage.setTitle("Your friend requests:");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SentFriendRequestsController frController = loader.getController();
            frController.setService(serviceC,serviceU,serviceP,serviceU.getUtilizatorByUserName(username).getId());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMessagesDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/messagesView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Communicate freely with your friends, " + username);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ChatController mController = loader.getController();

            mController.setService(serviceU,serviceM, serviceP, userId);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(FriendshipsChangeEvent friendshipsChangeEvent) {
        initModel();
    }

    @FXML
    public void handleMessage(){
        showMessagesDialog();
    }

}
