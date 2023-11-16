package unioeste.sd.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    User addUser(String name, String username, String password) throws RemoteException;
    User login(String username, String password) throws RemoteException;
}
