package unioeste.sd.server;

import unioeste.sd.common.Server;
import unioeste.sd.common.User;
import unioeste.sd.common.Voting;
import unioeste.sd.common.exceptions.AuthException;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ServerImpl implements Server {
    private HashMap<String,User> users = new HashMap<>();
    private Set<Voting> votings = new HashSet<>();
    private App app;

    public ServerImpl(App app) {
        this.app = app;
    }

    @Override
    public User addUser(String name, String username, String password) {
        User user = new User(username, name, password);
        if (users.containsKey(username))
            throw new AuthException();

        users.put(user.username, user);

        return login(username, password);
    }

    @Override
    public User login(String username, String password) {

        if (!users.containsKey(username))
            throw new AuthException("User not found!");

        User savedUser = users.get(username);
        if (!savedUser.password.equals(password))
            throw new AuthException("Invalid password!");

        return savedUser;
    }
}
