package practic.service;

import practic.domain.SpecialOffer;
import practic.repository.Repository;
import practic.utils.observer.Observable;
import practic.utils.observer.Observer;
import practic.utils.event.OfferChangeEvent;

import java.util.ArrayList;
import java.util.List;


public class SpecialOfferService implements Observable<OfferChangeEvent> {
    private Repository<Long, SpecialOffer> repoSpecial;

    private List<Observer<OfferChangeEvent>> observers=new ArrayList<>();

    public SpecialOfferService(Repository<Long, SpecialOffer> repoSpecial) {
        this.repoSpecial = repoSpecial;
    }

    public Iterable<SpecialOffer> getAll(){
        return repoSpecial.findAll();
    }

    @Override
    public void addObserver(Observer<OfferChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<OfferChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void notifyObservers(OfferChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
