package com.lab.multiplexer.NewsForMe.Activity.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.lab.multiplexer.NewsForMe.Activity.Model.Category;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;

import java.util.ArrayList;

/**
 * Created by USER on 11/10/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    Context c;
    public static final String DB_NAME = "news_content.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        c= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableAttributes tableAttr = new TableAttributes();
        String query = tableAttr.newsBanglaTableCreateQuery();
        String query1 = tableAttr.newsEnglishTableCreateQuery();
        try {
            db.execSQL(query);
            db.execSQL(query1);
            Log.i("Create", "Hoise");
        } catch (SQLException e) {
            Log.e("Create Error", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNews(News news_obj,String language) {

        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.NEWS_ID, news_obj.getId());
        values.put(TableAttributes.NEWS_TITLE, news_obj.getTitle());
        values.put(TableAttributes.NEWS_DESCRIPTION, news_obj.getDescription());
        values.put(TableAttributes.NEWS_IMAGE, news_obj.getImage());
        values.put(TableAttributes.CATEGORY, news_obj.getCategoryName());
        values.put(TableAttributes.LINK, news_obj.getLink());
        values.put(TableAttributes.NEWSPAPER, news_obj.getSource());
        values.put(TableAttributes.PUBLISH_TIME, news_obj.getTime());
        values.put(TableAttributes.SAVED_STATUS, 0);
        try {
            if(checkNews(news_obj.getId(),language)>0){
                //Toast.makeText(c,"You've already saved this news",Toast.LENGTH_LONG).show();
            } else {
                if(language.equals("Bangla")){
                    dbInsert.insert(TableAttributes.NEWS_BANGLA_TABLE_NAME, null, values);
                    Log.i("Data", values.toString());
                } else {
                    dbInsert.insert(TableAttributes.NEWS_ENGLISH_TABLE_NAME, null, values);
                    Log.i("Data", values.toString());
                }

                //Toast.makeText(c,"News saved successfully",Toast.LENGTH_LONG).show();
            }

        } catch (SQLException e) {
            Log.e("Insert Error", e.toString());
        }


    }

    public void saveNews(News news_obj,String language) {

        SQLiteDatabase dbInsert = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableAttributes.NEWS_ID, news_obj.getId());
        values.put(TableAttributes.NEWS_TITLE, news_obj.getTitle());
        values.put(TableAttributes.NEWS_DESCRIPTION, news_obj.getDescription());
        values.put(TableAttributes.NEWS_IMAGE, news_obj.getImage());
        values.put(TableAttributes.CATEGORY, news_obj.getCategoryName());
        values.put(TableAttributes.LINK, news_obj.getLink());
        values.put(TableAttributes.NEWSPAPER, news_obj.getSource());
        values.put(TableAttributes.PUBLISH_TIME, news_obj.getTime());
        values.put(TableAttributes.SAVED_STATUS, 1);
        try {
            if(checkNews(news_obj.getId(),language)>0){
                if(checkSaveNews(news_obj.getId(),language)>0){
                    Toast.makeText(c,"You've already saved this news",Toast.LENGTH_LONG).show();
                } else {
                    changeSaveStatus(news_obj,language);
                    Toast.makeText(c,"News saved successfully",Toast.LENGTH_LONG).show();
                }
            } else {
                if(language.equals("Bangla")){
                    dbInsert.insert(TableAttributes.NEWS_BANGLA_TABLE_NAME, null, values);
                    Log.i("Data", values.toString());
                } else {
                    dbInsert.insert(TableAttributes.NEWS_ENGLISH_TABLE_NAME, null, values);
                    Log.i("Data", values.toString());
                }

                Toast.makeText(c,"News saved successfully",Toast.LENGTH_LONG).show();
            }

        } catch (SQLException e) {
            Log.e("Insert Error", e.toString());
        }


    }

    public ArrayList<News> getAllNews(String cat,String language) {
        ArrayList<News> arrayList = new ArrayList<News>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
             table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
             table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        String query = "SELECT * FROM " + table_name +" ORDER BY id ASC";
        //String phone_number_query = "SELECT "+ TableAttributes.STUDENT_PHONENO+" FROM " + TableAttributes.STUDENT_TABLE_NAME;
        Cursor cur = dbFetch.rawQuery(query, null);
        cur.moveToFirst();
        int i = 0;
        while(!cur.isAfterLast()){
            Category c = new Category(i, cur.getString(cur.getColumnIndex(TableAttributes.CATEGORY)));
            if(cur.getString(cur.getColumnIndex(TableAttributes.CATEGORY)).equals(cat)){
                News n = new News(cur.getInt(cur.getColumnIndex(TableAttributes.NEWS_ID))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_TITLE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_DESCRIPTION))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_IMAGE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWSPAPER))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.LINK))
                        ,c);
                Log.i("Data",cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))+"");
                arrayList.add(n);
            } else if(cat.equals("highlights")){
                News n = new News(cur.getInt(cur.getColumnIndex(TableAttributes.NEWS_ID))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_TITLE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_DESCRIPTION))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_IMAGE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWSPAPER))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.LINK))
                        ,c);
                Log.i("Data",cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))+"");
                arrayList.add(n);
            }
            cur.moveToNext();
            i++;
        }
        dbFetch.close();
        cur.close();
        return arrayList;
    }


    public ArrayList<News> getAllfullNews(String cat,String language) {
        ArrayList<News> arrayList = new ArrayList<News>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        String query = "SELECT * FROM " + table_name +" ORDER BY id ASC";
        //String phone_number_query = "SELECT "+ TableAttributes.STUDENT_PHONENO+" FROM " + TableAttributes.STUDENT_TABLE_NAME;
        Cursor cur = dbFetch.rawQuery(query, null);
        cur.moveToFirst();
        int i = 0;
        while(!cur.isAfterLast()){
            Category c = new Category(i, cur.getString(cur.getColumnIndex(TableAttributes.CATEGORY)));
            if(cur.getString(cur.getColumnIndex(TableAttributes.CATEGORY)).toLowerCase().equals(cat)){
                News n = new News(cur.getInt(cur.getColumnIndex(TableAttributes.NEWS_ID))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_TITLE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_DESCRIPTION))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_IMAGE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWSPAPER))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.LINK))
                        ,c);
                Log.i("Data",cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))+"");
                arrayList.add(n);
            } else if(cat.equals("highlights")){
                News n = new News(cur.getInt(cur.getColumnIndex(TableAttributes.NEWS_ID))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_TITLE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_DESCRIPTION))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_IMAGE))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.NEWSPAPER))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))
                        ,cur.getString(cur.getColumnIndex(TableAttributes.LINK))
                        ,c);
                Log.i("Data",cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))+"");
                arrayList.add(n);
            }
            cur.moveToNext();
            i++;
        }
        dbFetch.close();
        cur.close();
        return arrayList;
    }

    public ArrayList<News> getAllSaveNews(String language) {
        ArrayList<News> arrayList = new ArrayList<News>();
        SQLiteDatabase dbFetch = this.getReadableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        Cursor cur = dbFetch.query(table_name, new String[] { "id",TableAttributes.CATEGORY,
                        TableAttributes.NEWS_ID,
                        TableAttributes.NEWS_TITLE,
                        TableAttributes.NEWS_DESCRIPTION,
                        TableAttributes.NEWS_IMAGE,
                        TableAttributes.NEWSPAPER,
                        TableAttributes.PUBLISH_TIME,
                        TableAttributes.LINK,
                }, TableAttributes.SAVED_STATUS+" =? ",
                new String[] { String.valueOf(1) }, null, null, null, null);
        //String phone_number_query = "SELECT "+ TableAttributes.STUDENT_PHONENO+" FROM " + TableAttributes.STUDENT_TABLE_NAME;
        cur.moveToFirst();
        int i = 0;
        while(!cur.isAfterLast()){
            Category c = new Category(i, cur.getString(cur.getColumnIndex(TableAttributes.CATEGORY)));
            News n = new News(cur.getInt(cur.getColumnIndex(TableAttributes.NEWS_ID))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_TITLE))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_DESCRIPTION))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.NEWS_IMAGE))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.NEWSPAPER))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))
                    ,cur.getString(cur.getColumnIndex(TableAttributes.LINK))
                    ,c);
            Log.i("Data",cur.getString(cur.getColumnIndex(TableAttributes.PUBLISH_TIME))+"");
            arrayList.add(n);
            cur.moveToNext();
            i++;
        }
        dbFetch.close();
        cur.close();
        return arrayList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TableAttributes.NEWS_BANGLA_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public int checkNews(int id,String language){
        SQLiteDatabase db = this.getReadableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        Cursor cur = db.query(table_name, new String[] { "id",
                        }, TableAttributes.NEWS_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();
        while(!cur.isAfterLast()){
            cur.moveToNext();
        }
        Log.i("Data count", cur.getCount()+"");
        // return count
        cur.close();
        return cur.getCount();
    }

    public int checkSaveNews(int id,String language){
        SQLiteDatabase db = this.getReadableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        Cursor cur = db.query(table_name, new String[] { "id",
                }, TableAttributes.NEWS_ID + "=? AND "+TableAttributes.SAVED_STATUS+" =? ",
                new String[] { String.valueOf(id),String.valueOf(1) }, null, null, null, null);
        if (cur != null)
            cur.moveToFirst();
        while(!cur.isAfterLast()){
            cur.moveToNext();
        }
        Log.i("Data count", cur.getCount()+"");
        // return count
        cur.close();
        return cur.getCount();
    }

    public void unSaveNews(News n, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        ContentValues values = new ContentValues();
        values.put(TableAttributes.SAVED_STATUS, 0);
        db.update(table_name,values, TableAttributes.NEWS_ID + " = ?",
                new String[] { String.valueOf(n.getId()) });
        db.close();
    }

    public void changeSaveStatus(News n,String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table_name = "";
        if(language.equals("Bangla")){
            table_name = TableAttributes.NEWS_BANGLA_TABLE_NAME;
        } else {
            table_name = TableAttributes.NEWS_ENGLISH_TABLE_NAME;
        }
        ContentValues values = new ContentValues();
        values.put(TableAttributes.SAVED_STATUS, 1);
        db.update(table_name,values, TableAttributes.NEWS_ID + " = ?",
                new String[] { String.valueOf(n.getId()) });
        db.close();
    }

}
