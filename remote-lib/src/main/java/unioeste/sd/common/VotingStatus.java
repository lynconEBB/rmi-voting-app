package unioeste.sd.common;

public enum VotingStatus {
    CREATED("Created", -16711681),
    STARTED("Started", -16711936),
    FINISHED("Finished", -16776961);

    public String text;

    public int col;

    VotingStatus(String text, int col) {
        this.text = text;
        this.col = col;
    }
}
