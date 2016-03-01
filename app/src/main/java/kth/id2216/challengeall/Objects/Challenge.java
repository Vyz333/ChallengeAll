package kth.id2216.challengeall.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Vyz on 2016-02-12.
 */
public class Challenge implements Serializable{
    public String id;
    public String title;
    public String author;
    public String description;
    public Timestamp timestamp;
    public String img;
    public String category;
    public String deadline;


    public Challenge(){
    }
    public Challenge(long id, String title, String description, Timestamp timestamp, String img, String category, String deadline) {
        this.id = String.valueOf(id);
        this.title = title;
        this.author = null;
        this.description = description;
        this.timestamp = timestamp;
        this.img = img;
        this.category = category;
        this.deadline = deadline;
    }
    public Challenge(String id, String title,String author, String description, Timestamp timestamp, String img, String category, String deadline) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.timestamp = timestamp;
        this.img = img;
        this.category = category;
        this.deadline = deadline;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
