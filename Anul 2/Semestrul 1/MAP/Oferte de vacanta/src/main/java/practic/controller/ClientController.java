package practic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import practic.domain.*;
import practic.service.ClientService;
import practic.service.HotelService;
import practic.service.LocationService;
import practic.service.SpecialOfferService;
import practic.utils.event.ClientChangeEvent;
import practic.utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientController implements Observer<ClientChangeEvent> {
    private LocationService locationService;
    private HotelService hotelService;
    private SpecialOfferService specialOffersService;
    private ClientService clientService;

    private String clientName;
    private String selectedOffer;

    ObservableList<HotelDTO> hotels = FXCollections.observableArrayList();
    ObservableList<String> locations = FXCollections.observableArrayList();
    ObservableList<ClientOfferDTO> offers = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ClientOfferDTO, String> columnEnd;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<ClientOfferDTO, String> columnHotelName;

    @FXML
    private TableColumn<ClientOfferDTO, String> columnLocation;

    @FXML
    private TableColumn<ClientOfferDTO, String> columnStart;

    @FXML
    private TableView<ClientOfferDTO> tableViewOffers;


    public void setServiceClient(LocationService locationService, HotelService hotelService, SpecialOfferService specialOffersService, ClientService clientService, String clientName) {
        this.locationService = locationService;
        this.hotelService = hotelService;
        this.specialOffersService = specialOffersService;
        this.clientService = clientService;
        this.clientName = clientService.getClientByName(clientName).getName();
        clientService.addObserver(this);

        initModel();

    }

    private void initModel(){
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
        offers.setAll(offerList);
    }


    @FXML
    public void initialize() {
        columnHotelName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tableViewOffers.setItems(offers);
    }

    @Override
    public void update(ClientChangeEvent clientChangeEvent) {
        initModel();
    }
}
