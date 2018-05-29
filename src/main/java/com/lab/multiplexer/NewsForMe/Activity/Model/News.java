package com.lab.multiplexer.NewsForMe.Activity.Model;

public class News {

    private int id;
    private String title;
    private String image;
    private String source;
    private String time;
    private String link;
    private String language;
    private String description;
    private String publish_time;
    private int comment_count;
    private Category category;

    public News(int id, String title, String description,String image,String source,String time,String link, Category category) {
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
