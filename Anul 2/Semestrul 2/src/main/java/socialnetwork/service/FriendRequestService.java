package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.domain.exceptions.FriendRequestValidationException;
import socialnetwork.domain.exceptions.ServiceException;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.events.FriendshipsChangeEvent;
import socialnetwork.utils.events.FriendRequestsEvent;
import socialnetwork.utils.observer.Observer;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestService implements Observable<FriendRequestsEvent> {
    private Repository<Tuple<Long, Long>, FriendRequest> repo;
    private Repository<Tuple<Long, Long>, Prietenie> repoP;

    private List<Observer<FriendRequestsEvent>> observers=new ArrayList<>();



    public FriendRequestService(Repository<Tuple<Long, Long>, FriendRequest> repo, Repository<Tuple<Long, Long>, Prietenie> repoP) {
        this.repo = repo;
        this.repoP=repoP;

    }
    public Iterable<FriendRequest> getAll(){
        return repo.findAll();
    }

    public FriendRequest addFriendRequest(FriendRequest friendRequest){
        try{
            FriendRequest fr = repo.save(friendRequest);
            if(fr==null)
            {
                notifyObservers(new FriendRequestsEvent(ChangeEventType.ADD,fr));
            }
            return fr;
        } catch(
                FriendRequestValidationException e){throw new ServiceException(e.getMessage());}
    }

    public FriendRequest deleteFriendRequest(Tuple<Long,Long> id){
        try{
            FriendRequest fr=repo.delete(id);
            if(fr==null)
                throw new ServiceException("Aceasta prietenie nu exista!");
            else{


                    notifyObservers(new FriendRequestsEvent(ChangeEventType.CANCEL,fr));
                    return fr;

            }

        }
        catch(FriendRequestValidationException fr){throw new ServiceException(fr.getMessage());}
    }

    public void respond(FriendRequest friendRequest, String status){

        FriendRequest invitatie2=repo.findOne(friendRequest.getId());
        if(invitatie2==null){
            throw new ServiceException("Aceasta invitatie nu exista");
        }else{
            if(invitatie2.getStatus()==Status.PENDING){
                try{
                    friendRequest.setStatus(Status.valueOf(status));
                    friendRequest.setDate(invitatie2.getDate());
                    repo.update(friendRequest);
                    if(status.compareTo(Status.APPROVED.name())==0){
                        Prietenie pr=new Prietenie(friendRequest.getId().getLeft(), friendRequest.getId().getRight(), LocalDate.now().getYear(),LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                        repoP.save(pr);
                    }
                }catch(FriendRequestValidationException validationException){
                    throw new ServiceException(validationException.getMessage());
                }
            }
            else{
                throw new ServiceException("Aceasta invitatie a fost deja acceptata sau refuzata");
            }
        }

    }

    public void respondId(Tuple<Long,Long> id, String status){
        FriendRequest invitatie2=repo.findOne(id);
        if(invitatie2==null){
            throw new ServiceException("Aceasta invitatie nu exista");
        }else{
            if(invitatie2.getStatus().equals(Status.PENDING)){
                try{
                    invitatie2.setStatus(Status.valueOf(status));
                    repo.update(invitatie2);
                    if(status.compareTo(Status.APPROVED.name())==0){
                        Prietenie pr=new Prietenie(invitatie2.getId().getLeft(), invitatie2.getId().getRight(), LocalDate.now().getYear(),LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
                        repoP.save(pr);
                    }
                }catch(FriendRequestValidationException validationException){
                    throw new ServiceException(validationException.getMessage());
                }
            }
            else{
                throw new ServiceException("Aceasta invitatie a fost deja acceptata sau refuzata");
            }
        }

    }

    public FriendRequest updateFriendRequest(FriendRequest friendRequest){
        try{
            FriendRequest fr = repo.update(friendRequest);
            if(fr==null)
            {
                notifyObservers(new FriendRequestsEvent(ChangeEventType.UPDATE,fr));
            }
            return fr;
        } catch(
                FriendRequestValidationException e){throw new ServiceException(e.getMessage());}
    }


    public FriendRequest getFriendRequest(Tuple<Long, Long> id) {
        return repo.findOne(id);
    }

    @Override
    public void addObserver(Observer<FriendRequestsEvent> e) {
        observers.add(e);
    }


    @Override
    public void removeObserver(Observer<FriendRequestsEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendRequestsEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
