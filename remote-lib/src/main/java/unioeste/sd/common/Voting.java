package unioeste.sd.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voting implements Serializable {
    public Long id;
    public User owner;

    public String title;
    public VotingStatus status;
    public String description;
    public LocalDateTime creationDate;
    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public Map<String, Integer> votes;
    public Map<String, String> voters;

    public Voting(Long id, User owner, String title, String description, List<String> options) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.voters = new HashMap<>();
        this.votes = new HashMap<>();
        for (String option : options) {
            votes.put(option, 0);
        }
        this.status = VotingStatus.CREATED;
        this.creationDate = LocalDateTime.now();
    }

}
