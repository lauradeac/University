package practic;

import practic.domain.*;
import practic.repository.*;
import practic.service.ClientService;
import practic.service.HotelService;
import practic.service.LocationService;
import practic.service.SpecialOfferService;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String locationFile = "data/locations.txt";
        String hotelFile = "data/hotels.txt";
        String offerFile = "data/offers.txt";
        String clientFile = "data/clients.txt";


        Repository<Long, Location> locationRepository = new LocationFileRepository(locationFile);
        Repository<Long, Hotel> hotelRepository = new HotelFileRepository(hotelFile);
        Repository<Long, SpecialOffer> offerRepository = new SpecialOfferRepository(offerFile);
        Repository<Long, Client> clientRepository = new ClientFileRepository(clientFile);

        LocationService locationService = new LocationService(locationRepository);
        HotelService hotelService = new HotelService(hotelRepository);
        SpecialOfferService specialOfferService = new SpecialOfferService(offerRepository);
        ClientService clientService = new ClientService(clientRepository);

        //Scanner in = new Scanner(System.in);

        //String id = in.nextLine();
//        for (Client c : clientRepository.findAll()){
//            Scanner in = new Scanner(System.in);
//            String id = in.nextLine();
//            c.setClientId(Long.parseLong(id));
//            clientRepository.save(c);
//        }

//        Client client = new Client(Long.parseLong(id), "Paul", 65, 20, Hobby.MUSIC);
//        clientRepository.save(client);
        //System.out.println(client);
        System.out.println(clientRepository.findAll());



//        System.out.println("Locations");
//        System.out.println(locationRepository.findAll());
//        System.out.println("Hotels");
//        System.out.println(hotelRepository.findAll());
        //2;2;18/06/2022;27/05/2022;100
        //System.out.println(specialOfferService.getAll());

    }

}
