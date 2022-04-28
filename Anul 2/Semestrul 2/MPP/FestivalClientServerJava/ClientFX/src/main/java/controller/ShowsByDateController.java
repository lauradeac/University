package controller;

import model.Spectacol;
import services.FestivalException;
import services.IFestivalServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ShowsByDateController {
    private IFestivalServices server;
        //implements Observer<ShowsChangeEvent> {
    //private SuperService superService;
    private String username;

    ObservableList<Spectacol> model = FXCollections.observableArrayList();

    @FXML
    TableView<Spectacol> tableView;
    @FXML
    TableColumn<Spectacol, String> columnArtistName;
    @FXML
    TableColumn<Spectacol, String> columnTime;
    @FXML
    TableColumn<Spectacol, String> columnLocation;
    @FXML
    TableColumn<Spectacol, String> columnRemaining;
    @FXML
    DatePicker datePicker;


    public void setService(IFestivalServices server, String username) {
        this.server = server;
        this.username = username;

        //initModel();
    }

    @FXML
    private void showSpectacole(){
        initModel();
    }
    private void initModel() {
        LocalDate dateShow = datePicker.getValue();

        List<Spectacol> list= null;
        try {
            list = StreamSupport.stream(server.getAllShows().spliterator(),false)
                    .filter(x->(x.getDateOfShow().equals(dateShow)))
                    .map(x->{
                        Spectacol spectacol = new Spectacol(x.getID(), x.getArtistName(), x.getDateOfShow(), x.getTime(), x.getLocation(), x.getRemainingTickets(), x.getTotalTickets());
                        return spectacol;
                    })
                    .collect(Collectors.toList());
        } catch (FestivalException e) {
            e.printStackTrace();
        }
        model.setAll(list);

    }

    @FXML
    public void initialize() {
        columnArtistName.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnRemaining.setCellValueFactory(new PropertyValueFactory<>("remainingTickets"));

        tableView.setItems(model);

    }


}
