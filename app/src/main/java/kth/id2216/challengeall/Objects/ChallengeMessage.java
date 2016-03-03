package kth.id2216.challengeall.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Vyz on 2016-03-03.
 */
public class ChallengeMessage implements Serializable{
    String fromID;
    String toID;
    String challengeID;
    Timestamp timestamp;
    public String getChallengeID() {
        return challengeID;
    }

    public void setChallengeID(String challengeID) {
        this.challengeID = challengeID;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public ChallengeMessage(){}

    public ChallengeMessage(String challengeID, String fromID, Timestamp timestamp, String toID) {
        this.challengeID = challengeID;
        this.fromID = fromID;
        this.timestamp = timestamp;
        this.toID = toID;
    }
}
