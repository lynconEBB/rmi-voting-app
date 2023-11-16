package unioeste.sd.server;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public class Main {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, ServerNotActiveException {
        App app = new App();
        app.run();
    }
}