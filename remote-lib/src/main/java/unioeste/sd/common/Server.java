package unioeste.sd.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server extends Remote {
    User addUser(String name, String username, String password) throws RemoteException;
    User login(String username, String password) throws RemoteException;
    Voting createVoting(User user, VotingFormDTO votingDTO) throws RemoteException;
    List<VotingSimpleDTO> getAllVotings() throws RemoteException;
    VotingUniqueDTO getVotingById(User user, Long id) throws RemoteException;
    void advanceVoting(User user, Long id) throws RemoteException;
    void vote(User user, Long id, String option) throws RemoteException;

}
