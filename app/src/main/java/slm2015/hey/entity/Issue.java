package slm2015.hey.entity;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;

public class Issue {
    //todo implement core data structure here
    private Integer id;
    private String subject = "";
    private String description = "";
    private String place = "";
    private Bitmap image;
    private boolean like;
    private Date timestamp;

    public Issue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String location) {
        this.place = location;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getTimestamp() {
        Date now = new Date();
        Long differenceTime = now.getTime() - this.timestamp.getTime();

        Log.e("Issue Time Difference", differenceTime.toString());
        // textView is the TextView view that should display it
        return "Test";
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
