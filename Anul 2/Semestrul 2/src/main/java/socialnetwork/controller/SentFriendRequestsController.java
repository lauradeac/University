package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.FriendRequestsDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.service.FriendRequestService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.FriendRequestsEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SentFriendRequestsController implements Observer<FriendRequestsEvent> {
    private FriendRequestService serviceC;
    private UtilizatorService serviceU;
    private PrietenieService serviceP;
    private long idUser;

    ObservableList<FriendRequestsDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<FriendRequestsDTO> tableView;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnUsername;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnLastName;
    @FXML
    TableColumn<FriendRequestsDTO, String> tableColumnStatus;
    @FXML
    TableColumn<FriendRequestsDTO, LocalDate> tableColumnDate;

    public void setService(FriendRequestService serviceC, UtilizatorService serviceU, PrietenieService serviceP, long idUser) {
        this.serviceC = serviceC;
        this.serviceU = serviceU;
        this.serviceP = serviceP;
        serviceC.addObserver(this);
        this.idUser = idUser;
        initModel();
    }

    private void initModel() {

        List<FriendRequestsDTO> list = StreamSupport.stream(serviceC.getAll().spliterator(), false)
                .filter(x -> (x.getId().getLeft() == idUser))
                .map(x -> {
                    //Utilizator user1=x.getUser1();
                    Utilizator user2 = x.getUser2();
                    FriendRequestsDTO fr = new FriendRequestsDTO(x.getId(), serviceU.getUser(user2.getId()).getUserName(), serviceU.getUser(user2.getId()).getFirstName(), serviceU.getUser(user2.getId()).getLastName(), x.getStatus().name(), x.getDate());
                    return fr;
                })
                .collect(Collectors.toList());
        model.setAll(list);

    }

    @FXML
    public void initialize() {

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
    public void handleCancel() {
        FriendRequestsDTO selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        if(selected!=null)
            if(selected.getStatus().compareTo("PENDING")==0)
                try{
                    serviceC.deleteFriendRequest(selected.getId());
                    initModel();
                }
                catch(ServiceException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            else
            {
                MessageAlert.showErrorMessage(null,"S-a raspuns deja acestei cereri!");
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat nicio cerere de prietenie!");
        }

    }
}

