package com.lab.multiplexer.NewsForMe.Activity.Model;

import com.google.gson.annotations.SerializedName;

public class RetrofitCategory {

    private long id;
    @SerializedName("category_id")
    private String name;

    public RetrofitCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
