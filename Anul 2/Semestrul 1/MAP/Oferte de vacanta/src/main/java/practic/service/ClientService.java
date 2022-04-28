package practic.service;

import practic.domain.Client;
import practic.domain.Hotel;
import practic.repository.Repository;
import practic.utils.event.ClientChangeEvent;
import practic.utils.observer.Observable;
import practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class ClientService implements Observable<ClientChangeEvent> {
    private Repository<Long, Client> repoClient;
    private List<Observer<ClientChangeEvent>> observers=new ArrayList<>();

    public ClientService(Repository<Long, Client> repoClient) {
        this.repoClient = repoClient;
    }

    public Iterable<Client> getAll(){
        return repoClient.findAll();
    }

    public Client getClientByName(String name){
        Iterable<Client> clients = repoClient.findAll();
        Client client = null;
        for(Client c: clients){
            if (c.getName().equals(name)){
                client = c;
                break;
            }
        }
        return client;
    }

    public Client getClientById(Long id){
        return repoClient.findOne(id);
    }

    @Override
    public void addObserver(Observer<ClientChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ClientChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ClientChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
