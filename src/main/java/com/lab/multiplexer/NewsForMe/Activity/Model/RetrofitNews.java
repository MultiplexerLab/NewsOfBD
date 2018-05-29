package com.lab.multiplexer.NewsForMe.Activity.Model;

import com.google.gson.annotations.SerializedName;

public class RetrofitNews {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("file")
    private String image;
    @SerializedName("news_paper")
    private String source;
    @SerializedName("publish_time")
    private String time;
    @SerializedName("link")
    private String link;
    @SerializedName("description")
    private String description;
    @SerializedName("publish_time")
    private String publish_time;
    @SerializedName("comment_count")
    private int comment_count;
    @SerializedName("category_id")
    private Category category;
    @SerializedName("language")
    private String language;
    public RetrofitNews(int id, String title, String description,String image,String source,String time,String link, Category category) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.image = image;
        this.source = source;
        this.time = time;
        this.link = link;
        this.description =description;
    }

    public String getDescription(){
        return description;
    }
    public int getId() {
        return id;
    }
    public  String getTime(){
        return time;
    }
    public String getSource(){
        return source;
    }
    public String getLink(){
        return link;
    }
    public String getTitle() {
        return title;
    }


    public long getCategoryId() {
        return category.getId();
    }

    public String getCategoryName() {
        return category.getName();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getImage (){
        return image;
    }
    public int getComment_count(){
        return comment_count;
    }
    public void setComment_count(int comment_count){
        this.comment_count = comment_count;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
}
