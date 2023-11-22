package unioeste.sd.common;

import java.io.Serializable;

public class VotingSimpleDTO implements Serializable {
    public Long id;
    public User owner;

    public String title;

    public VotingSimpleDTO(Long id, User owner, String title) {
        this.id = id;
        this.owner = owner;
        this.title = title;
    }
}
