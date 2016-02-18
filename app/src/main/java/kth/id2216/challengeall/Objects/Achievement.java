package kth.id2216.challengeall.Objects;

/**
 * Created by Vyz on 2016-02-18.
 */
public class Achievement {
    public String date;
    public String title;
    public int icon;
    public Achievement(){}
    public Achievement(String date, int icon, String title) {
        this.date = date;
        this.icon = icon;
        this.title = title;
    }
}
