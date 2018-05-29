package com.lab.multiplexer.NewsForMe.Activity.Model;

/**
 * Created by U on 4/8/2017.
 */

public class Newspaper__model {
    private int id;
    private String newspaper_name;
    private String newspaper_type;


    public Newspaper__model(int id, String newspaper_name, String newspaper_type) {
        this.id = id;
        this.newspaper_name = newspaper_name;
        this.newspaper_type = newspaper_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewspaper_name() {
        return newspaper_name;
    }

    public void setNewspaper_name(String newspaper_name) {
        this.newspaper_name = newspaper_name;
    }

    public String getNewspaper_type() {
        return newspaper_type;
    }

    public void setNewspaper_type(String newspaper_type) {
        this.newspaper_type = newspaper_type;
    }
}
