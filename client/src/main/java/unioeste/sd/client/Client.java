package unioeste.sd.client;

import imgui.type.ImBoolean;
import unioeste.sd.common.*;
import unioeste.sd.common.exceptions.AuthException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Client {
    private Server server;
    private boolean isLogged;
    private User user;
    public List<VotingSimpleDTO>  availableVotings;
    public volatile VotingUniqueDTO selectedVoting;
    public Map<String, ImBoolean> userVotes;

    public Client() {
        this.isLogged = false;
        this.availableVotings = new ArrayList<>();
        this.selectedVoting = null;
        this.userVotes = new HashMap<>();
    }

    public CompletableFuture<Void> requestVote(String option) {
        return CompletableFuture.runAsync(() -> {
            // Avoid setting checkbox to false
            if (userVotes.get(option).get() == false) {
                userVotes.get(option).set(true);
                return;
            }

            try {
                for (Map.Entry<String, ImBoolean> userVote : userVotes.entrySet()) {
                    if (userVote.getValue().get() == true && !userVote.getKey().equals(option)) {
                        selectedVoting.votes.put(userVote.getKey(), selectedVoting.votes.get(userVote.getKey()) - 1);
                        userVote.getValue().set(false);
                    }
                }
                selectedVoting.votes.put(option, selectedVoting.votes.get(option) + 1);
                selectedVoting.voters.put(user.username, option);

                server.vote(user, selectedVoting.id, option);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void requestProceed() {
        CompletableFuture.runAsync(() -> {
            try {
                switch (selectedVoting.status) {
                    case CREATED -> {
                        selectedVoting.status = VotingStatus.STARTED;
                    }
                    case STARTED -> {
                        selectedVoting.status = VotingStatus.FINISHED;
                        int max = selectedVoting.votes.values().stream().mapToInt(v -> v).max().orElse(0);
                        for (Map.Entry<String, Integer> entry : selectedVoting.votes.entrySet()) {
                            if (entry.getValue() == max) {
                                selectedVoting.winners.add(entry.getKey());
                            }
                        }
                    }
                }
                server.proceedVoting(user, selectedVoting.id);
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

    public void setSelectedVoting(Long id) {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            try {
                selectedVoting = server.getVotingById(user, id);
                this.userVotes = new HashMap<>();
                for (Map.Entry<String, Boolean> entry : selectedVoting.userVotes.entrySet()) {
                    this.userVotes.put(entry.getKey(), new ImBoolean(entry.getValue()));
                }

            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        voidCompletableFuture.exceptionally(e -> {
            System.out.println(e.getMessage());
            return null;
        });
    }

    public void requestRegistration(String host, int port, String name, String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry(host,port);
            server = (Server) registry.lookup("voting-server");
            User responseUser = server.addUser(name, username, password);
            setCurrentUser(responseUser);
        } catch (RemoteException | NotBoundException | AuthException e ) {
            throw new RuntimeException(e);
        }
    }

    public void requestLogin(String host, int port, String username, String password) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
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

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public User getUser() {
        return user;
    }
}
