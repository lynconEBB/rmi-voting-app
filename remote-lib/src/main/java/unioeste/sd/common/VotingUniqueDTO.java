package unioeste.sd.common;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingUniqueDTO implements Serializable {
    public Long id;
    public String ownerUsername;

    public String title;
    public VotingStatus status;
    public String description;
    public LocalDateTime creationDate;
    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public Map<String, Integer> votes;
    public Map<String, String> voters;
    public Map<String, Boolean> userVotes;
    public List<String> winners;

    public VotingUniqueDTO(Voting voting, User user) {
        this.id = voting.id;
        this.ownerUsername = voting.owner.username;
        this.title = voting.title;
        this.status = voting.status;
        this.description = voting.description;
        this.creationDate = voting.creationDate;
        this.startDate = voting.startDate;
        this.endDate = voting.endDate;
        this.voters = voting.voters;
        this.votes = voting.votes;
        this.winners = voting.winners;

        this.userVotes = new HashMap<>();
        for (Map.Entry<String, Integer> option : voting.votes.entrySet()) {
            userVotes.put(option.getKey(), false);
        }
        if (voting.voters.containsKey(user.username)) {
            String optionVoted = voting.voters.get(user.username);
            userVotes.put(optionVoted, true);
        }
    }
}
