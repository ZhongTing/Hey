package slm2015.hey.entity;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;

public class Issue {
    //todo implement core data structure here
    private Integer id;
    private String subject = "";
    private String description = "";
    private String place = "";
    private Bitmap image = null;
    private boolean like;
    private Date timestamp;
    private boolean incognito;
    private String photoURL = null;

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
        this.place = location.equals("無") ? "" : location;
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
        Calendar calendar = Calendar.getInstance();
        String time = "";
        if (this.timestamp != null) {
            calendar.setTime(this.timestamp);
            Long differenceTime = now.getTimeInMillis() - calendar.getTimeInMillis();
            differenceTime = differenceTime / 1000;
            if (differenceTime > 60) {
                differenceTime = differenceTime / 60;
                if (differenceTime > 60) {
                    differenceTime = differenceTime / 60;
                    if (differenceTime > 24) {
                        differenceTime = differenceTime / 24;
                        time = Long.toString(differenceTime);
                        time = time + "天前";
                    } else {
                        time = Long.toString(differenceTime);
                        time = time + "小時前";
                    }
                } else {
                    time = Long.toString(differenceTime);
                    time = time + "分鐘前";
                }
            } else {
                if (differenceTime > 0) {
                    time = Long.toString(differenceTime);
                    time = time + "秒前";
                } else {
                    time = "0秒前";
                }

            }
        }
        return time;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setIncognito(boolean incognito) {
        this.incognito = incognito;
    }

    public boolean isIncognito() {
        return incognito;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void reset() {
        this.image = null;
        this.description = "";
        this.place = "";
        this.subject = "";
    }
}
