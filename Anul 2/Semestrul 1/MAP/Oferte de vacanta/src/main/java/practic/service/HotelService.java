package practic.service;

import practic.domain.Hotel;
import practic.repository.Repository;
import practic.utils.event.HotelChangeEvent;
import practic.utils.observer.Observable;
import practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class HotelService implements Observable<HotelChangeEvent> {

    public Repository<Long, Hotel> repoHotel;
    //private MemberValidator validator;
    private List<Observer<HotelChangeEvent>> observers=new ArrayList<>();

    public HotelService(Repository<Long, Hotel> repoHotel) {
        this.repoHotel = repoHotel;
    }

    public Iterable<Hotel> getAll(){
        return repoHotel.findAll();
    }

    public Hotel getHotelById(Long id){
        return repoHotel.findOne(id);
    }

    public Hotel getHotelByName(String name){
        Iterable<Hotel> hotels = repoHotel.findAll();
        Hotel hotel = null;
        for(Hotel h: hotels){
            if (h.getHotelName().equals(name)){
                hotel = h;
                break;
            }
        }
        return hotel;
    }

    @Override
    public void addObserver(Observer<HotelChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<HotelChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(HotelChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
