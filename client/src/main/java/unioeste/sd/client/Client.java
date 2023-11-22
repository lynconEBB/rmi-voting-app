package unioeste.sd.client;

import unioeste.sd.common.*;
import unioeste.sd.common.exceptions.AuthException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

public class Client {
    private Server server;
    private boolean isLogged;
    private User user;
    public List<VotingSimpleDTO>  availableVotings;
    public VotingUniqueDTO selectedVoting;

    public Client() {
        this.isLogged = false;
        this.availableVotings = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add("ioption1");
        options.add("ioption2");
        options.add("ioption4");
        options.add("ioption3");
        Voting v = new Voting(12L, new User("4trt","fdsgdf"), "fdsfsd", "fdskljgfd gldf dflkg dflg", options);
        this.selectedVoting = new VotingUniqueDTO(v,new User("4trt","fdsgdf"));
        this.selectedVoting.status = VotingStatus.STARTED;
    }

    public CompletableFuture<Void> requestVote(String option) {
        return CompletableFuture.runAsync(() -> {
            try {
                server.vote(user, selectedVoting.id, option);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Voting> createVoting(VotingFormDTO votingDTO) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Voting voting = server.createVoting(user, votingDTO);
                availableVotings.add(new VotingSimpleDTO(voting.id, voting.owner, voting.title));
                return voting;
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void requestRegistration(String name, String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry( 1099);
            server = (Server) registry.lookup("voting-server");
            User responseUser = server.addUser(name, username, password);
            setCurrentUser(responseUser);
        } catch (RemoteException | NotBoundException | AuthException e ) {
            throw new RuntimeException(e);
        }
    }

    public void requestLogin(String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry( 1099);
            server = (Server) registry.lookup("voting-server");
            User responseUser = server.login(username, password);
            setCurrentUser(responseUser);
        } catch (RemoteException | NotBoundException | AuthException e ) {
            throw new RuntimeException(e);
        }
    }

    private void setCurrentUser(User user) {
        this.user = user;
        isLogged = true;
        UpdateTask.startTask(this, server);
    }

    public void setSelectedVoting(Long id) {
         CompletableFuture.runAsync(() -> {
            try {
                selectedVoting = server.getVotingById(user, id);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isLogged() {
        return isLogged;
    }

    public User getUser() {
        return user;
    }
}
