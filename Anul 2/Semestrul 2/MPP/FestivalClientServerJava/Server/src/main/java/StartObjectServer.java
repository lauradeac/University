import network.utils.AbstractServer;
import network.utils.ObjectConcurrentServer;
import network.utils.ServerException;
import persistence.repository.*;

import server.ServicesImpl;
import services.IFestivalServices;

import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {

       Properties serverProps = new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        AngajatDBRepository angajatRepo = new AngajatDBRepository(serverProps);
        SpectacolDBRepository spectacolRepo = new SpectacolDBRepository(serverProps);
        BiletDBRepository biletRepo = new BiletDBRepository(serverProps, spectacolRepo);

        IFestivalServices serverImpl = new ServicesImpl(angajatRepo, spectacolRepo, biletRepo);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, serverImpl);
        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
