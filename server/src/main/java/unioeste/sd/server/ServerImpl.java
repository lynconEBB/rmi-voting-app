package unioeste.sd.server;

import unioeste.sd.common.*;
import unioeste.sd.common.exceptions.AuthException;
import unioeste.sd.common.exceptions.InvalidDataException;

import java.rmi.RemoteException;
import java.util.*;

public class ServerImpl implements Server {
    private HashMap<String,User> users = new HashMap<>();
    private HashMap<Long,Voting> votings = new HashMap<>();
    private List<VotingSimpleDTO> votingDTOS = new ArrayList<>();
    private Long lastId;

    public ServerImpl() {
        this.lastId = 0L;
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

    private boolean isValidUser(User user) {
        if (!users.containsKey(user.username))
            return false;

        User savedUser = users.get(user.username);
        return savedUser.password.equals(user.password);
    }

    @Override
    public Voting createVoting(User user, VotingFormDTO votingDTO) throws RemoteException {
        if (!isValidUser(user))
            throw new AuthException("invalid User!");
        if (votingDTO.title.isBlank() || votingDTO.description.isBlank()) {
            throw new InvalidDataException();
        }
        for (String option : votingDTO.options){
            if (option.isBlank()) {
                throw new InvalidDataException();
            }
        }

        Voting voting = new Voting(lastId,user, votingDTO.title, votingDTO.description, votingDTO.options);

        votings.put(lastId, voting);
        votingDTOS.add(new VotingSimpleDTO(lastId, user, votingDTO. title));
        lastId++;

        return voting;
    }

    @Override
    public List<VotingSimpleDTO> getAllVotings() throws RemoteException {
        return votingDTOS;
    }

    @Override
    public VotingUniqueDTO getVotingById(User user, Long id) throws RemoteException {
        if (!isValidUser(user))
            throw new AuthException("invalid User!");

        if (!votings.containsKey(id))
            throw new InvalidDataException("Could not found voting with id provided!");

        Voting voting = votings.get(id);
        return new VotingUniqueDTO(voting, user);
    }

    @Override
    public void advanceVoting(User user, Long id) throws RemoteException {
        return;
    }

    @Override
    public void vote(User user, Long id, String option) {
        if (!isValidUser(user))
            throw new AuthException("invalid User!");
        if (!votings.containsKey(id))
            throw new InvalidDataException("Could not found voting with id provided!");

        Voting voting = votings.get(id);
        if (voting.status != VotingStatus.STARTED)
            throw new InvalidDataException("Voting has ended or not started");

        if (voting.voters.containsKey(user.username)) {
            String optionVoted = voting.voters.get(user.username);
            voting.votes.put(optionVoted, voting.votes.get(optionVoted) - 1);
        }

        voting.voters.put(user.username, option);
        voting.votes.put(option, voting.votes.get(option) + 1);
    }

}
