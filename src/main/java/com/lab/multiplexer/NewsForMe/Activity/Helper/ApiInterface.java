package com.lab.multiplexer.NewsForMe.Activity.Helper;

/**
 * Created by U on 6/2/2017.
 */

import com.lab.multiplexer.NewsForMe.Activity.Model.RetrofitNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiInterface {
    @GET("newscontent.php")
    Call<RetrofitNews> getAllNews(@Path("language") String language);

}