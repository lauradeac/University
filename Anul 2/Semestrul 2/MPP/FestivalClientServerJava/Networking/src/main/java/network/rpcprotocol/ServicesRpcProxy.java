package network.rpcprotocol;


import model.Angajat;
import model.Bilet;
import model.Spectacol;
import services.FestivalException;
import services.IFestivalObserver;
import services.IFestivalServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IFestivalServices {
    private String host;
    private int port;

    private IFestivalObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;


    public ServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    public void logout(Angajat angajat, IFestivalObserver client) throws FestivalException {
        Angajat ang = new Angajat(angajat.getUserName(), angajat.getPassword());
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(ang).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws FestivalException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new FestivalException("Error sending object " + e);
        }

    }

    private Response readResponse() throws FestivalException {
        Response response = null;
        try {
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) throws FestivalException {
        client.updateAvailableTickets();
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.UPDATE_SHOW;
    }


    @Override
    public void login(Angajat angajat, IFestivalObserver client) throws FestivalException {
        initializeConnection();
        //Angajat ang = new Angajat(angajat.getUserName(), angajat.getPassword());
        Request req = new Request.Builder().type(RequestType.LOGIN).data(angajat).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new FestivalException(err);
        }
    }


    @Override
    public Angajat getAngajatByUsername(String username) throws FestivalException {
        Request req = new Request.Builder().type(RequestType.LOGIN).data(username).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new FestivalException(err);
        }
        return (Angajat) response.data();
    }

    @Override
    public Iterable<Bilet> getAllBilete() throws FestivalException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_BILETE).build();
        sendRequest(req);
        Response response = readResponse();
        List<Bilet> bilete;
        if (response.type() == ResponseType.GET_ALL_BILETE) {
            bilete = (List<Bilet>) response.data();
            return bilete;
        }

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new FestivalException(err);
        }
        return null;
    }


    @Override
    public Iterable<Spectacol> getAllShows() throws FestivalException {

        Request req = new Request.Builder().type(RequestType.GET_ALL_SHOWS).build();
        sendRequest(req);
        Response response = readResponse();
        List<Spectacol> shows;
        if (response.type() == ResponseType.GET_ALL_SHOWS) {
            shows = (List<Spectacol>) response.data();
            //return List.of(shows);
            return shows;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new FestivalException(err);
        }
        return null;
    }

    @Override
    public Spectacol getShowById(Integer idShow) throws FestivalException {
        Request req = new Request.Builder().type(RequestType.FIND_SHOW).data(idShow).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new FestivalException(err);
        }
        return (Spectacol) response.data();}


    @Override
    public void addBilet(Bilet bilet) throws FestivalException {
        //Bilet bilet1 = new Bilet(bilet.getIDBilet(), bilet.getShow(), bilet.getNoTickets(), bilet.getBuyerName());
        Request req=new Request.Builder().type(RequestType.ADD_BILET).data(bilet).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ADD_BILET){

        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new FestivalException(err);
        }

    }

//    @Override
//    public Integer getLastID() {
//        return getLastID();
//    }



    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                } catch (FestivalException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}