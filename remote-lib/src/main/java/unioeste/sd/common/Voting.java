package unioeste.sd.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Voting {
    public Long id;
    public User owner;
    public String title;
    public String description;
    public LocalDateTime creationDate;
    public LocalDateTime startDate;
    public LocalDateTime endDate;

    public Voting(User owner, String title) {
        this.owner = owner;
        this.title = title;
    }
}
