package practic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practic.controller.HolidayController;
import practic.domain.*;
import practic.repository.*;
import practic.service.*;
import practic.utils.GlobalVariable;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {
    private static List<Long> idClienti = new ArrayList<>();
    
    @Override
    public void start(Stage stage)  {


        String locationFile = "data/locations.txt";
        String hotelFile = "data/hotels.txt";
        String offerFile = "data/offers.txt";
        String clientFile = "data/clients.txt";
        String reservationFile = "data/reservations.txt";

        Repository<Long, Location> locationRepository = new LocationFileRepository(locationFile);
        Repository<Long, Hotel> hotelRepository = new HotelFileRepository(hotelFile);
        Repository<Long, SpecialOffer> offerRepository = new SpecialOfferRepository(offerFile);
        Repository<Long, Client> clientRepository = new ClientFileRepository(clientFile);
        Repository<Long, Reservation> reservationRepository =  new ReservationFileRepository(reservationFile);

        LocationService locationService = new LocationService(locationRepository);
        HotelService hotelService = new HotelService(hotelRepository);
        SpecialOfferService specialOfferService = new SpecialOfferService(offerRepository);
        ClientService clientService = new ClientService(clientRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);

        try {
            Iterable<Client> clients = clientService.getAll();

            for (Client c: clients){
                if (idClienti.contains(c.getClientId())){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/views/holiday.fxml"));
                    AnchorPane root = (AnchorPane) loader.load();

                    Stage dialogStage = new Stage();
                    //dialogStage.setTitle("Holiday Offers:");
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.setScene(new Scene(root, 700, 500));
                    dialogStage.setTitle("Client: "+ c.getName());

                    HolidayController hController = loader.getController();
                    hController.setService(locationService, hotelService, specialOfferService, clientService, c.getName(), reservationService);

                    dialogStage.show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //System.out.println(Arrays.toString(args).split(","));
        List<String> strings = Arrays.stream(Arrays.stream(args).toList().get(0).split(",")).toList();
        //System.out.println(strings.size());
        strings.forEach(x->{
            List<String> elems = Arrays.stream(x.split(":")).toList();
            idClienti.add(Long.valueOf(elems.get(1)));
        });
        //idClienti.forEach(System.out::println);
        GlobalVariable.setList(idClienti);

        launch();
    }
}
