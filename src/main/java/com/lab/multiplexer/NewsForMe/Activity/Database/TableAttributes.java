package com.lab.multiplexer.NewsForMe.Activity.Database;

/**
 * Created by Miral on 11/10/2016.
 */
public class TableAttributes {

    public static final String NEWS_BANGLA_TABLE_NAME = "news_bn";
    public static final String NEWS_ENGLISH_TABLE_NAME = "news_en";
    public static final String NEWS_ID = "news_id";
    public static final String NEWS_TITLE= "title";
    public static final String NEWS_DESCRIPTION = "description";
    public static final String NEWS_IMAGE = "image";
    public static final String NEWSPAPER = "news_paper";
    public static final String PUBLISH_TIME = "publish_time";
    public static final String LINK = "link";
    public static final String CATEGORY = "category";
    public static final String SAVED_STATUS = "status";

    public String newsBanglaTableCreateQuery(){
        return "CREATE TABLE "+ NEWS_BANGLA_TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NEWS_ID+" INTEGER," +
                NEWS_TITLE+" TEXT," +
                NEWS_DESCRIPTION+" TEXT," +
                NEWS_IMAGE+" TEXT," +
                NEWSPAPER+" TEXT," +
                PUBLISH_TIME+" TEXT," +
                LINK+" TEXT," +
                SAVED_STATUS+" INTEGER," +
                CATEGORY+" TEXT)";
    }
    public String newsEnglishTableCreateQuery(){
        return "CREATE TABLE "+ NEWS_ENGLISH_TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                NEWS_ID+" INTEGER," +
                NEWS_TITLE+" TEXT," +
                NEWS_DESCRIPTION+" TEXT," +
                NEWS_IMAGE+" TEXT," +
                NEWSPAPER+" TEXT," +
                PUBLISH_TIME+" TEXT," +
                LINK+" TEXT," +
                SAVED_STATUS+" INTEGER," +
                CATEGORY+" TEXT)";
    }
}
