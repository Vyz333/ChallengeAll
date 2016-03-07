package kth.id2216.challengeall.Objects;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Vyz on 2015-12-29.
 */
public class User implements Serializable {
    private String avatar;
    private String firstName;
    private String lastName;
    private String email;

    private String twitter_username;
    private String twitter_bio;
    private List<String> likes;
    private Map<String,Object> challenges;

    public void setLikes(List<String> likes){this.likes=likes;}
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTwitter_bio(String twitter_bio) {
        this.twitter_bio = twitter_bio;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public List<String> getLikes(){return likes;}
    public String getAvatar() {
        return avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public String getTwitter_bio() {
        return twitter_bio;
    }

    public Map<String,Object> getChallenges() {
        return challenges;
    }

    public void setChallenges(Map<String,Object> challenges) {
        this.challenges = challenges;
    }

    public User(String avatar, String firstName, String lastName, String email, String twitter_username, String twitter_bio) {
        this.avatar = avatar;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.twitter_username = twitter_username;
        this.twitter_bio = twitter_bio;
    }

    public User() {
    }
}
