package practic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practic.HelloApplication;
import practic.domain.*;
import practic.service.*;
import practic.utils.GlobalVariable;
import practic.utils.event.HotelChangeEvent;
import practic.utils.observer.Observer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HolidayController implements Observer<HotelChangeEvent> {
    private LocationService locationService;
    private HotelService hotelService;
    private SpecialOfferService specialOffersService;
    private ClientService clientService;
    private ReservationService reservationService;
    private String selectedHotel;
    private String clientName;

    ObservableList<HotelDTO> hotels = FXCollections.observableArrayList();
    ObservableList<String> locations = FXCollections.observableArrayList();
    ObservableList<ClientOfferDTO> specialOffers = FXCollections.observableArrayList();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableView<HotelDTO> hotelTableView;

    @FXML
    private TableView<ClientOfferDTO> offersTable;

    @FXML
    TableColumn<Hotel,String> tableColumnHotelName;
    @FXML
    TableColumn<Hotel,String> tableColumnPrice;
    @FXML
    TableColumn<Hotel, LocalDate> tableColumnNoRooms;
    @FXML
    TableColumn<Hotel, String> tableColumnType;
    @FXML
    private TableColumn<ClientOfferDTO, String> columnEnd;
    @FXML
    private TableColumn<ClientOfferDTO, String> columnHotelName;
    @FXML
    private TableColumn<ClientOfferDTO, String> columnLocation;
    @FXML
    private TableColumn<ClientOfferDTO, String> columnStart;

    public void setService(LocationService locationService, HotelService hotelService, SpecialOfferService specialOfferService,
                           ClientService clientService, String clientName, ReservationService reservationService) {

        this.locationService = locationService;
        this.hotelService = hotelService;
        this.specialOffersService = specialOfferService;
        this.clientService = clientService;
        this.reservationService = reservationService;
        this.clientName = clientService.getClientByName(clientName).getName();

        hotelService.addObserver(this);

        initModel();
        initOffers();
    }

    @Override
    public void update(HotelChangeEvent hotelChangeEvent) {
        initModel();
    }

    private void initModel() {

        List<String> locationList = StreamSupport.stream(locationService.getAll().spliterator(), false)
                .map(Location::getLocationName)
                .collect(Collectors.toList());
        locations.setAll(locationList);
        comboBox.getItems().addAll(locationList);
    }

    private void initTable(){
        String selectedLocation = comboBox.getValue();
        Double currentLocationId = locationService.getLocationByName(selectedLocation).getLocationId();
        List<HotelDTO> hotelList = StreamSupport.stream(hotelService.getAll().spliterator(), false)
                .filter(x-> (x.getLocationId().equals(currentLocationId)))
                .map(x->{
                    HotelDTO hotelDTO = new HotelDTO(x.getHotelName(), x.getNoRooms(), x.getPricePerNight(), x.getType());
                    return hotelDTO;
                })
                .collect(Collectors.toList());
        hotels.setAll(hotelList);
    }

    private void initOffers() {
        Client c = clientService.getClientByName(clientName);
        List<ClientOfferDTO> offerList = StreamSupport.stream(specialOffersService.getAll().spliterator(), false)
                .filter(x-> (x.getPercents() < c.getFidelityGrade()))
                .map(x-> {
                    Hotel hotel = hotelService.getHotelById(x.getHotelId().longValue());
                    Location location = locationService.getLocationById(hotel.getLocationId().longValue());
                    ClientOfferDTO clientOfferDTO = new ClientOfferDTO(hotel.getHotelName(), location.getLocationName(), x.getStartDate(), x.getEndDate());
                    return clientOfferDTO;
                })
                .collect(Collectors.toList());
        specialOffers.setAll(offerList);
    }

    @FXML
    public void initialize() {
        tableColumnHotelName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        tableColumnNoRooms.setCellValueFactory(new PropertyValueFactory<>("noRooms"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("Type"));

        hotelTableView.setItems(hotels);

        columnHotelName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        offersTable.setItems(specialOffers);
    }

    @FXML
    public void handleLocationSelection(){
        initTable();
    }

    @FXML
    public void handleHotelSelection(){
        selectedHotel = hotelTableView.getSelectionModel().getSelectedItem().getHotelName();
        showDatePickerDialog();
    }

    @FXML
    public void handleReservation() throws Exception {
        ClientOfferDTO selected = offersTable.getSelectionModel().getSelectedItem();
        String hotelName = selected.getHotelName();
        Double currentHotel = hotelService.getHotelByName(hotelName).getHotelId();
        Client currentClient = clientService.getClientByName(clientName);
        if(selected != null){
            long days = ChronoUnit.DAYS.between(selected.getStartDate(), selected.getEndDate());
            Double id = reservationService.getSize().doubleValue()+1;
            Reservation reservation = new Reservation( id, currentClient.getClientId().doubleValue(), currentHotel, LocalDateTime.now(), (int) days);
            reservationService.addReservation(reservation);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "See you soon!", "You just made a reservation!");
            for(Client client: clientService.getAll()) {
                if(GlobalVariable.getList().contains(client.getClientId()) && !client.getName().equals(clientName) && client.getHobby().equals(currentClient.getHobby())) {
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Yay! " + client.getName(), currentClient.getName() + " has the same hobby as you!");
                }
            }
        }
        else{
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Error", "You need to select an offer!");
        }
    }
    @FXML
    public void handleShowSpecialOffers(){
        showSpecialOffersDialog();
    }

    private void showSpecialOffersDialog() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/client.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Special Offers");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ClientController clientController = loader.getController();

            clientController.setServiceClient(locationService, hotelService, specialOffersService,clientService, clientName);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showDatePickerDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/specialOffers.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Hotel: " + selectedHotel);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SpecialOffersController specialOfferController = loader.getController();
            specialOfferController.setServiceSpecial(hotelService, specialOffersService, selectedHotel);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
