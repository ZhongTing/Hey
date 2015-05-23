package slm2015.hey.entity;

import android.graphics.Bitmap;

public class Issue {
    //todo implement core data structure here
    private Integer id;
    private String subject = "";
    private String description = "";
    private String place = "";
    private Bitmap image;
    private boolean like;

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
}
