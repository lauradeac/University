package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.FriendRequestsEvent;
import socialnetwork.utils.events.FriendshipsChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FriendRequestsController implements Observer<FriendRequestsEvent> {
    private FriendRequestService serviceC;
    private UtilizatorService serviceU;
    private PrietenieService serviceP;
    private long idUser;
    private String username;


    ObservableList<FriendRequestsDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<FriendRequestsDTO> tableView;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnUsername;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnLastName;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnStatus;
    @FXML
    TableColumn<FriendRequestsDTO, LocalDate> tableColumnDate;



    public void setService(FriendRequestService serviceC, UtilizatorService serviceU, PrietenieService serviceP, String username) {
        this.serviceC = serviceC;

        this.serviceU=serviceU;

        this.serviceP=serviceP;
        serviceC.addObserver(this);
        this.username=username;
        this.idUser = serviceU.getUtilizatorByUserName(username).getId();
        initModel();
    }

    private void initModel() {

        List<FriendRequestsDTO> list = StreamSupport.stream(serviceC.getAll().spliterator(), false)
                .filter(x -> (x.getId().getRight() == idUser))
                .map(x->{
                    Utilizator user1=x.getUser1();
                    System.out.println(serviceU.getUser(user1.getId()).getUserName());
                    FriendRequestsDTO fr=new FriendRequestsDTO(x.getId(), serviceU.getUser(user1.getId()).getUserName() ,serviceU.getUser(user1.getId()).getFirstName(),serviceU.getUser(user1.getId()).getLastName(),x.getStatus().name(),x.getDate());
                    return fr;})
                .collect(Collectors.toList());


        model.setAll(list);


    }

    @FXML
    public void initialize() {

        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<FriendRequestsDTO, String>("userName"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendRequestsDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendRequestsDTO, String>("lastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<FriendRequestsDTO, String>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendRequestsDTO, LocalDate>("date"));
        tableView.setItems(model);

    }



    @Override
    public void update(FriendRequestsEvent friendRequestsEvent) {
        initModel();
    }

    @FXML
    public void handleAccept(){
        FriendRequestsDTO selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        if(selected!=null)
            if(selected.getStatus().compareTo("PENDING")==0) {
                try {
                    this.serviceP.addPrieten(idUser, selected.getId().getLeft(), LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                    FriendRequest fr = serviceC.getFriendRequest(selected.getId());
                    fr.setStatus(Enum.valueOf(Status.class,Status.APPROVED.name())); ///?????????
                    this.serviceC.updateFriendRequest(fr);

                    initModel();

                } catch (ServiceException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            }
            else {
                MessageAlert.showErrorMessage(null,"Ati raspuns deja acestei cereri!");
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie!");
        }
    }

    @FXML
    public void handleReject(){
        FriendRequestsDTO selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        if(selected!=null)
            if(selected.getStatus().compareTo("PENDING")==0) {
                try {
                    FriendRequest fr = serviceC.getFriendRequest(selected.getId());

                    serviceC.respond(fr, Status.REJECTED.name());
                    serviceC.deleteFriendRequest(fr.getId());
                    //o stergem ca sa mai dam posibilitatea sa se trimita iar

                    initModel();
                } catch (ServiceException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            }
            else {
                MessageAlert.showErrorMessage(null,"Ati raspuns deja acestei cereri!");
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie!");
        }
    }

}