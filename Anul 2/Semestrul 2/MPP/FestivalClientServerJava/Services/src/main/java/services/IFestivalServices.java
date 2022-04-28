package services;


import model.Angajat;
import model.Bilet;
import model.Spectacol;

import java.util.List;

public interface IFestivalServices {
     void login(Angajat angajat, IFestivalObserver client) throws FestivalException;

     void logout(Angajat angajat, IFestivalObserver client) throws FestivalException;

     Angajat getAngajatByUsername(String username) throws FestivalException;

     Iterable<Bilet> getAllBilete() throws  FestivalException;

     Iterable<Spectacol> getAllShows() throws FestivalException;
     //List<Spectacol> getAllShows() throws FestivalException;

     Spectacol getShowById(Integer id) throws FestivalException;

     void addBilet(Bilet bilet) throws FestivalException;

     //Integer getLastID();

     //void notifyClients() throws FestivalException;

}
