package practic.service;

import practic.domain.Reservation;
import practic.repository.Repository;
import practic.utils.event.ReservationChangeEvent;
import practic.utils.observer.Observable;
import practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class ReservationService implements Observable<ReservationChangeEvent> {
    private Repository<Long, Reservation> repoReservation;

    private List<Observer<ReservationChangeEvent>> observers = new ArrayList<>();

    public ReservationService(Repository<Long, Reservation> reservationRepository) {
        this.repoReservation = reservationRepository;
    }

    public Long getSize(){
        Long contor = 0L;
        for (Reservation r : repoReservation.findAll()){
            contor += 1;
        }
        return contor;
    }

    public void addReservation(Reservation reservation) throws Exception {
        try{
            reservation.setId(getSize()+1L);
            repoReservation.save(reservation);
            notifyObservers(new ReservationChangeEvent());
        }
        catch(Exception e) {throw new Exception(e.getMessage());}
    }


    public Iterable<Reservation> getAll(){
        return repoReservation.findAll();
    }
    @Override
    public void addObserver(Observer<ReservationChangeEvent> e) {

        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ReservationChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ReservationChangeEvent t) {

    }
}
