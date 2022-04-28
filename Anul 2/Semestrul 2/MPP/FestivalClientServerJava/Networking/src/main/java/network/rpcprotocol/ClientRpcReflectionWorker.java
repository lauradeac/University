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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


public class ClientRpcReflectionWorker implements Runnable, IFestivalObserver {
    private IFestivalServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IFestivalServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleGET_ALL_SHOWS(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Iterable<Spectacol> shows = server.getAllShows();
            return new Response.Builder().type(ResponseType.GET_ALL_SHOWS).data(shows).build();
        } catch (FestivalException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFIND_SHOW(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Integer id = (Integer) request.data();
            Spectacol spec = server.getShowById(id);
            return new Response.Builder().type(ResponseType.FIND_SHOW).data(spec).build();
        } catch (FestivalException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());

        Angajat angajat = (Angajat)request.data();

        try {
            server.login(angajat, this);
            return okResponse;
        } catch (FestivalException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        Angajat udto=(Angajat)request.data();
        try {
            server.logout(udto, this);
            connected=false;
            return okResponse;

        } catch (FestivalException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_BILET(Request request){
            System.out.println("Buying tickets ...");
            Bilet mdto = (Bilet) request.data();

            try {
                server.addBilet(mdto);
                return okResponse;
            } catch (FestivalException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }


    }

    private Response handleGET_ALL_BILETE(Request request){
        System.out.println("Get all shows request ..."+request.type());
        try {
            Iterable<Bilet> bilets = server.getAllBilete();
            return new Response.Builder().type(ResponseType.GET_ALL_BILETE).data(bilets).build();
        } catch (FestivalException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void updateAvailableTickets()  {
        Response response = new Response.Builder().type(ResponseType.UPDATE_SHOW).build();
        System.out.println("Dam update la client");

        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
