package server;

import model.Angajat;
import model.Bilet;
import model.Spectacol;
import persistence.repository.*;
import services.FestivalException;
import services.IFestivalObserver;
import services.IFestivalServices;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServicesImpl implements IFestivalServices {

    //interfete
    private AngajatDBRepository angajatRepository;
    private SpectacolDBRepository spectacolRepository;
    private BiletDBRepository biletRepository;
    private Map<String, IFestivalObserver> loggedAngajati;

    public ServicesImpl(AngajatDBRepository angajatRepository, SpectacolDBRepository spectacolRepository, BiletDBRepository biletRepository) {

        this.angajatRepository = angajatRepository;
        this.spectacolRepository = spectacolRepository;
        this.biletRepository = biletRepository;
        loggedAngajati = new ConcurrentHashMap<>();;
    }


    public synchronized void login(Angajat angajat, IFestivalObserver obs) throws FestivalException {
        Angajat ang = angajatRepository.getAngajatByUsername(angajat.getUserName());
        if (ang != null){
            if(loggedAngajati.get(angajat.getUserName())!=null)
                throw new FestivalException("User already logged in.");
            loggedAngajati.put(angajat.getUserName(), obs);

        }else
            throw new FestivalException("Authentication failed.");
    }


//    public synchronized void buyTicket(Bilet bilet) throws FestivalException {
//        String id_receiver=bilet.getBuyerName();
//        IFestivalObserver receiverClient=loggedAngajati.get(id_receiver);
//        if (receiverClient!=null) {      //the receiver is logged in
//            biletRepository.add(bilet);
//            receiverClient.updateAvailableTickets(bilet);
//        }
//        else
//            throw new FestivalException("Angajat "+id_receiver+" not logged in.");
//    }

    public synchronized void logout(Angajat angajat, IFestivalObserver client) throws FestivalException {
        IFestivalObserver localClient=loggedAngajati.remove(angajat.getUserName());
        if (localClient==null)
            throw new FestivalException("Angajat "+angajat.getUserName()+" is not logged in.");
    }


    public Angajat getAngajatByUsername(String username){
        return angajatRepository.getAngajatByUsername(username);
    }

    @Override
    public synchronized Iterable<Bilet> getAllBilete()  {

        return biletRepository.findAll();
    }


    public synchronized Iterable<Spectacol> getAllShows(){

        return spectacolRepository.findAll();
    }

    public synchronized Spectacol getShowById(Integer id){

        return spectacolRepository.findById(id);
    }

    public synchronized void addBilet(Bilet bilet) throws FestivalException {
//        int id_bilet = bilet.getIDBilet();
//        IFestivalObserver receiverClient=loggedAngajati.get(id_bilet);
//        if (receiverClient!=null) {
        biletRepository.add(bilet);
        Spectacol spec = bilet.getShow();
        int remaining = spec.getRemainingTickets() - bilet.getNoTickets();
        spec.setRemainingTickets(remaining);
        spectacolRepository.update(spec.getID(), spec);
        notifyClients();
//        }
//        else
//            throw new FestivalException("error");

    }

//    public Integer getLastID(){
//        int id = 0;
//        for(Bilet b: biletRepository.findAll())
//            if(id < b.getID())
//                id=b.getID();
//        return id;
//    }
    private final int defaultThreadsNo=5;

    public synchronized void notifyClients(){
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        loggedAngajati.forEach((x, y) ->
            executor.execute(() -> {
            try {
                System.out.println("Updating show..." + x);
                y.updateAvailableTickets();
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        }));
        executor.shutdown();
    }

}
