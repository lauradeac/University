package practic.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import practic.domain.HotelDTO;

import practic.domain.SpecialOffer;
import practic.domain.SpecialOfferDTO;
import practic.service.HotelService;
import practic.service.SpecialOfferService;
import practic.utils.observer.Observer;
import practic.utils.event.OfferChangeEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class SpecialOffersController implements Observer<OfferChangeEvent> {
    public HotelService hotelService;
    public SpecialOfferService specialOfferService;

    //ObservableList<HotelDTO> hotels = FXCollections.observableArrayList();
    ObservableList<SpecialOfferDTO> offers = FXCollections.observableArrayList();

    private String hotelName;
    private Double hotelId;

    @FXML
    private TableView<SpecialOfferDTO> specialOfferView;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableColumn<SpecialOffer, String> tableColumnId;

    @FXML
    private TableColumn<SpecialOffer, String> tableColumnStart;

    @FXML
    private TableColumn<SpecialOffer, String> tableColumnEnd;

    @FXML
    private TableColumn<SpecialOffer, String> tableColumnPercents;

    public void setServiceSpecial(HotelService hotelService, SpecialOfferService specialOfferService, String hotelName) {

        this.hotelService = hotelService;
        this.specialOfferService = specialOfferService;
        this.hotelName = hotelName;
        this.hotelId = hotelService.getHotelByName(hotelName).getHotelId();
        specialOfferService.addObserver(this);
        //initModel();
    }

    private void initModel() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        List<SpecialOfferDTO> offersList = StreamSupport.stream(specialOfferService.getAll().spliterator(), false)
                .filter(x->((x.getHotelId().equals(hotelId)
                       && ((x.getStartDate().equals(startDate)|| x.getStartDate().isAfter(startDate)) &&
                        (x.getEndDate().equals(endDate) || x.getEndDate().isBefore(endDate))))))
                .map(x->{
                    SpecialOfferDTO specialOfferDTO = new SpecialOfferDTO(x.getSpecialOfferId(), x.getStartDate(), x.getEndDate(), x.getPercents());
                    return specialOfferDTO;
                })
                .collect(Collectors.toList());
        offers.setAll(offersList);
    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("specialOfferId"));
        tableColumnStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableColumnEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tableColumnPercents.setCellValueFactory(new PropertyValueFactory<>("percents"));

        specialOfferView.setItems(offers);
    }

    @Override
    public void update(OfferChangeEvent offerChangeEvent) {
        initModel();
    }

    @FXML
    public void handleShowOffers(){
        initModel();
    }
}

