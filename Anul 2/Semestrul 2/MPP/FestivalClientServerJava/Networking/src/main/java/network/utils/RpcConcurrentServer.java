package network.utils;


import network.rpcprotocol.ClientRpcReflectionWorker;
import services.IFestivalServices;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private IFestivalServices server;
    public RpcConcurrentServer(int port, IFestivalServices server) {
        super(port);
        this.server = server;
        System.out.println("Festival - RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(server, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
