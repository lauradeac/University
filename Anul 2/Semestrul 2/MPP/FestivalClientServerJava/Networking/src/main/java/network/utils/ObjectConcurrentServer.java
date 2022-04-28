package network.utils;


import network.rpcprotocol.ClientRpcReflectionWorker;
import services.IFestivalServices;

import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IFestivalServices server;
    public ObjectConcurrentServer(int port, IFestivalServices server) {
        super(port);
        this.server = server;
        System.out.println("Festival - FestivalRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
