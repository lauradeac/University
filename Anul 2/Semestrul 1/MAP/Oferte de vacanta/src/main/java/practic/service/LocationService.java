package practic.service;

import practic.domain.Location;
import practic.repository.Repository;
import practic.utils.event.LocationChangeEvent;
import practic.utils.observer.Observable;
import practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class LocationService implements Observable<LocationChangeEvent> {
    private Repository<Long, Location> repoLocation;

    private List<Observer<LocationChangeEvent>> observers=new ArrayList<>();

    public LocationService(Repository<Long, Location> repoLocation) {
        this.repoLocation = repoLocation;
    }

    public Iterable<Location> getAll() {
        return repoLocation.findAll();
    }

    public Location getLocationByName(String name){
        Iterable<Location> locations = repoLocation.findAll();
        Location loc = null;
        for(Location l: locations){
            if (l.getLocationName().equals(name)){
                loc = l;
                break;
            }
        }
        return loc;

    }

    @Override
    public void addObserver(Observer<LocationChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<LocationChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(LocationChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public Location getLocationById(Long locationId) {
        return repoLocation.findOne(locationId);
    }
}
