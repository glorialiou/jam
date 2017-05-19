package hu.ait.jam.data;

import java.io.Serializable;

public class Match implements Serializable {

    private String matchName;
    private String matchPhone;

    public Match(String matchName, String matchPhone) {
        this.matchName = matchName;
        this.matchPhone = matchPhone;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getMatchPhone() {
        return matchPhone;
    }

    public void setMatchPhone(String matchPhone) {
        this.matchPhone = matchPhone;
    }
}
