package unioeste.sd.client;

import unioeste.sd.common.Server;
import unioeste.sd.common.User;
import unioeste.sd.common.exceptions.AuthException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Server server;
    private MainWindow mainWindow;
    private boolean isLogged;
    private User user;

    public Client(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.isLogged = false;
    }

    public void requestRegistration(String name, String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry( 1099);
            Server server = (Server) registry.lookup("voting-server");
            User responseUser = server.addUser(name, username, password);
            setCurrentUser(responseUser);
        } catch (RemoteException | NotBoundException e ) {
            mainWindow.welcomeWindow.showRegisterMessageError("Connection refused!");
            throw new RuntimeException(e);
        } catch (AuthException e) {
            mainWindow.welcomeWindow.showRegisterMessageError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void requestLogin(String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry( 1099);
            Server server = (Server) registry.lookup("voting-server");
            User responseUser = server.login(username, password);
            setCurrentUser(responseUser);
        } catch (RemoteException | NotBoundException e ) {
            mainWindow.welcomeWindow.showLoginMessageError("Connection refused!");
            throw new RuntimeException(e);
        } catch (AuthException e) {
            mainWindow.welcomeWindow.showLoginMessageError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void setCurrentUser(User user) {
        this.user = user;
        isLogged = true;
    }

    public boolean isLogged() {
        return isLogged;
    }
}
