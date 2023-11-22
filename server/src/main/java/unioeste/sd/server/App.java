package unioeste.sd.server;

import unioeste.sd.common.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class App {

    public ServerImpl server;
    private Registry registry;

    public <T extends Remote> void addRemoteObject(T obj, String name) throws RemoteException, AlreadyBoundException {
        UnicastRemoteObject.exportObject(obj, 1099);
        registry.bind(name, obj);
    }

    public <T extends Remote> void removeRemoteObject(T obj) throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(obj,true);
    }

    public void run() throws RemoteException, AlreadyBoundException {
        ServerImpl server = new ServerImpl();
        Server stub = (Server) UnicastRemoteObject.exportObject(server, 1099);

        registry = LocateRegistry.createRegistry(1099);
        registry.bind("voting-server", stub);

        System.out.println("Server started!");
    }
}
