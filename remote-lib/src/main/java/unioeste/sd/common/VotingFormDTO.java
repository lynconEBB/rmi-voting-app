package unioeste.sd.common;

import java.io.Serializable;
import java.util.List;

public class VotingFormDTO implements Serializable {
    public String title;
    public String description;
    public List<String> options;

    public VotingFormDTO(String title, String description, List<String> options) {
        this.title = title;
        this.description = description;
        this.options = options;
    }
}
