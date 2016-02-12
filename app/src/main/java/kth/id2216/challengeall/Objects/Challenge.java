package kth.id2216.challengeall.Objects;

import java.sql.Timestamp;

/**
 * Created by Vyz on 2016-02-12.
 */
public class Challenge {
    public long id;
    public String title;
    public String description;
    public Timestamp timestamp;
    public String img;
    public String category;
    public String deadline;


    Challenge(){
    }
    public Challenge(long id, String title, String description, Timestamp timestamp, String img, String category, String deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.img = img;
        this.category = category;
        this.deadline = deadline;
    }
}
