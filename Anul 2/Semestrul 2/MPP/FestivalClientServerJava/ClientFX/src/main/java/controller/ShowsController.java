package controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Angajat;
import model.Bilet;
import model.Spectacol;
import model.exceptions.Exception;
import services.FestivalException;
import services.IFestivalObserver;
import services.IFestivalServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ShowsController implements Initializable, IFestivalObserver {
    //implements Observer<ShowsChangeEvent>
    //private SuperService superService;
    private IFestivalServices server;
    private String  username;
    private Integer angajatId;

    private Angajat angajat;

    ObservableList<Spectacol> model = FXCollections.observableArrayList();

    @FXML
    TableView<Spectacol> tableView;
    @FXML
    TableColumn<Spectacol, String> columnArtistName;
    @FXML
    TableColumn<Spectacol, LocalDate> columnDate;
    @FXML
    TableColumn<Spectacol, String> columnTime;
    @FXML
    TableColumn<Spectacol, String> columnLocation;
    @FXML
    TableColumn<Spectacol, String> columnRemaining;
    @FXML
    TableColumn<Spectacol, String> columnTotal;
    @FXML
    private TextField textBuyer;
    @FXML
    private TextField textNoTickets;

    @FXML
    private Button logOut;


    public void setShowService(IFestivalServices server) throws FestivalException {
        this.server = server;
        //this.username = username;
        //this.angajatId = server.getAngajatByUsername(username).getID();

        //superService.addObserver(this);
        //initModel();
    }

    public void initModel() throws FestivalException {
        List<Spectacol> list = StreamSupport.stream(server.getAllShows().spliterator(), false)
                .map(x->{
                    Spectacol spec = new Spectacol(x.getID(), x.getArtistName(), x.getDateOfShow(), x.getTime(), x.getLocation(), x.getRemainingTickets(), x.getTotalTickets());
                    return spec;})
                .collect(Collectors.toList());

        model.setAll(list);
    }

    @FXML
    public void initialize() {
        columnArtistName.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateOfShow"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnRemaining.setCellValueFactory(new PropertyValueFactory<>("remainingTickets"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("totalTickets"));

        tableView.setItems(model);

        tableView.setRowFactory(new Callback<TableView<Spectacol>, TableRow<Spectacol>>() {
            @Override
            public TableRow<Spectacol> call(TableView<Spectacol> tableView) {
                final TableRow<Spectacol> row = new TableRow<Spectacol>() {
                    @Override
                    protected void updateItem(Spectacol row, boolean empty) {
                        super.updateItem(row, empty);
                        if (!empty)
                            if(row.getRemainingTickets() == 0)
                                setStyle("-fx-background-color: red");
                            else
                                setStyle("");

                    }
                };
                return row;
            }
        });

    }

    @FXML
    private void handleShowByDate(){
        showSpectacolDialog();
    }

    private void showSpectacolDialog(){
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/showsByDate.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choose a date to display shows:");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ShowsByDateController showsCtrl = loader.getController();
            showsCtrl.setService(server, username);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBuyTickets() throws FestivalException {
        Spectacol selected = tableView.getSelectionModel().getSelectedItem(); //luam elementul selectat
        Spectacol spectacol = server.getShowById(selected.getID());

        List<Integer> ids = new ArrayList<>();
        Iterable<Bilet> bilets = server.getAllBilete();
        for (Bilet b : bilets) {
            ids.add(b.getID());
        }
        Integer idBilet = Collections.max(ids) + 1;

        if(selected!=null)
            try{
                String buyerName = textBuyer.getText();
                int noTickets = Integer.parseInt(textNoTickets.getText());
                Bilet bilet = new Bilet(idBilet, spectacol, noTickets, buyerName);
                bilet.setID(idBilet);
                server.addBilet(bilet);

                initModel();
            }
            catch(Exception | FestivalException e){
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        else{
            MessageAlert.showErrorMessage(null,"Nu ati selectat niciun spectacol!");
        }

    }

    public void logOut() throws IOException {
        try {
            this.server.logout(angajat, this);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
//        logOut.getScene().getWindow().hide();
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("log_in.fxml"));
//        AnchorPane root = loader.load();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root, 700, 500));
//        stage.setTitle("Log in:");
//        stage.show();

    }



    @Override
    public void updateAvailableTickets() throws FestivalException {
        Platform.runLater(() -> {
            try {
                this.initModel();
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        });

    }

    public void setAngajat(Angajat ang) {
        this.angajat = ang;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }
}
