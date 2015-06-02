package slm2015.hey.entity;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Issue {
    //todo implement core data structure here
    private Integer id;
    private String subject = "";
    private String description = "";
    private String place = "";
    private Bitmap image;
    private boolean like;
    private Date timestamp;
    private String photoURL;

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
        Calendar now = Calendar.getInstance();

        if (this.timestamp != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.timestamp);
            Long differenceTime = now.getTimeInMillis() - calendar.getTimeInMillis();
            Log.e("Issue Time Difference", differenceTime.toString());
        }

        // textView is the TextView view that should display it
        return "Test";
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
