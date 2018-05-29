package com.lab.multiplexer.NewsForMe.Activity.Model;

/**
 * Created by U on 4/1/2017.
 */

public class Comment {
    private int id;
    private String name;
    private String image;
    private String comment;
    private String time;
    private String fb_id;

    public Comment(int id, String name,String image,String comment,String time,String fb_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.comment = comment;
        this.time = time;
        this.fb_id = fb_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }
}
