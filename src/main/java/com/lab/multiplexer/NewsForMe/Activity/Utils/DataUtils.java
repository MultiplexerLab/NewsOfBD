package com.lab.multiplexer.NewsForMe.Activity.Utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.lab.multiplexer.NewsForMe.Activity.Model.Ad;
import com.lab.multiplexer.NewsForMe.Activity.Model.Category;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;

import java.util.ArrayList;
import java.util.List;

public final class DataUtils {

    public interface DataCallback {
        void onSuccess(List<News> books);
    }

    public static List<News> getNewses() {
        List<News> news = getAllNews();
        return news.subList(0, 9);
    }

    public static List<News> getAllNews() {
        List<News> news = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            news.add(newNews(i));
        }
        return news;
    }

    public static News getNews(int id) {
        List<News> news = getAllNews();
        return news.get(id);
    }

    public static void getNewsAsync(final Activity activity, final DataCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(getNewses());
                    }
                });
            }
        });
    }

    public static News newNews(int id) {
        String title = "Book " + id;
        String author = "Foo " + id;
        long categoryId = id / 3;
        Category category = new Category(categoryId, String.valueOf(categoryId));
        return new News(id, title, author,"","","", "",category);
    }

    public static List<Ad> getAds() {
        List<Ad> ads = new ArrayList<>();
        ads.add(new Ad(0, "Ad 0"));
        ads.add(new Ad(1, "Ad 1"));
        return ads;
    }

}
