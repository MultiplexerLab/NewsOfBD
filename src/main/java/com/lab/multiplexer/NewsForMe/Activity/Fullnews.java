package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lab.multiplexer.NewsForMe.Activity.Adapter.CustomPagerAdapter;
import com.lab.multiplexer.NewsForMe.Activity.Database.DatabaseHelper;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Model.Category;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints.API_LINK;
import static com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints.FILE_LINK_PATH;

public class Fullnews extends AppCompatActivity {
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    ArrayList<News> newses;
    JSONArray dataDetailsObj;
    int pos;
    String categoryName="";
    AVLoadingIndicatorView avi;
    @BindView(R.id.reload_container)
    RelativeLayout reload_containter;
    @BindView(R.id.reload_btn)
    ImageButton reload_btn;
    @BindView(R.id.error_txt)
    TextView error_txt;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    DatabaseHelper db;
    ArrayList<News> local_newses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_news);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("from"));
        pos = getIntent().getIntExtra("position", 0);
        if(getIntent().hasExtra("category")){
            categoryName = getIntent().getStringExtra("category");
        } else {
            categoryName ="";
        }
        mViewPager = (ViewPager) findViewById(R.id.pager);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        newses = new ArrayList<>();
        if(getIntent().hasExtra("language")){
            getAllNews(getIntent().getStringExtra("language"));
        } else if(categoryName.equals("sqlite_news")){
           getSavedNews();
        }else{
            getAllNews(pref.getString("language",""));
        }

        mCustomPagerAdapter = new CustomPagerAdapter(this, newses);
        reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(Fullnews.this,Fullnews.class);
                i.putExtra("category", categoryName);
                i.putExtra("from",getIntent().getStringExtra("from"));
                i.putExtra("position",pos);
                if(getIntent().hasExtra("language")){
                    i.putExtra("language",(getIntent().getStringExtra("language")));
                }
                startActivity(i);
                finish();
            }
        });
    }

    public void getSavedNews(){
        local_newses = db.getAllSaveNews(pref.getString("language",""));
        if(local_newses.size()==0){
            reload_containter.setVisibility(View.VISIBLE);
            error_txt.setText("You haven't saved any news yet. Long press on the news to save news and read it offline");
        }
        CustomPagerAdapter  nCustomPagerAdapter = new CustomPagerAdapter(this, local_newses);
        mViewPager.setAdapter(nCustomPagerAdapter);
        mViewPager.setCurrentItem(pos);
        avi.hide();

    }
    public void getLocalNews(){
        if(categoryName.equals("")){
            local_newses = db.getAllfullNews("highlights",pref.getString("language",""));
        } else {
            local_newses = db.getAllfullNews(categoryName,pref.getString("language",""));
        }

        if(local_newses.size()==0){
            reload_containter.setVisibility(View.VISIBLE);
            error_txt.setText("No data found this time. Please try again later.");
        }
        CustomPagerAdapter  nCustomPagerAdapter = new CustomPagerAdapter(this, local_newses);
        mViewPager.setAdapter(nCustomPagerAdapter);
        mViewPager.setCurrentItem(pos);
        avi.hide();

    }

    private void getAllNews(final String language) {
        if (isNetworkAvailable(this)) {
            StringRequest strReq = new StringRequest(Request.Method.POST, API_LINK, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("get all data", "response: " + response);
                    //  Toast.makeText(getApplicationContext(), "Fetch: " +response, Toast.LENGTH_LONG).show();
                    try {
                        dataDetailsObj = new JSONArray(response);
                        // check for error
                        //JSONArray dataDetailsObj = obj.getJSONArray("data");
                        if(dataDetailsObj.length()>0){
                            for (int i = 0; i < dataDetailsObj.length(); i++) {
                                JSONObject dat_obj = (JSONObject) dataDetailsObj.get(i);
                                //String details = dat_obj.getString("description");
                                Category c = new Category(i, dat_obj.getString("category_id"));
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()-21600000));
                                final CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString( returnInMillis(dat_obj.getString("publish_time").trim()), returnInMillis(timeStamp), 0);
                                if(dat_obj.getString("category_id").toLowerCase().equals(categoryName)){
                                    //language thik hole open korte hobe
                                   /* if(pref.getString("language","").equals("Bangla")){
                                        if(dat_obj.getString("language").equals("bangla")){
                                            News n = new News(dat_obj.getInt("id"),dat_obj.getString("title").trim(),dat_obj.getString("summary").trim(),dat_obj.getString("description").trim(),"http://newsofbd.com/beta/news/admin/"+dat_obj.getString("file").trim()
                                                    ,dat_obj.getString("news_paper").trim(),relativeTimeSpan.toString(),dat_obj.getString("link").trim(),c);
                                            n.setComment_count(dat_obj.getInt("comment_count"));
                                            newses.add(n);
                                        }
                                    } else {
                                        if(dat_obj.getString("language").equals("english")){
                                            News n = new News(dat_obj.getInt("id"),dat_obj.getString("title").trim(),dat_obj.getString("summary").trim(),dat_obj.getString("description").trim(),"http://newsofbd.com/beta/news/admin/"+dat_obj.getString("file").trim()
                                                    ,dat_obj.getString("news_paper").trim(),relativeTimeSpan.toString(),dat_obj.getString("link").trim(),c);
                                            n.setComment_count(dat_obj.getInt("comment_count"));
                                            newses.add(n);
                                        }
                                    }*/
                                    if(returnInMillis(dat_obj.getString("publish_time"))<= returnInMillis(timeStamp)){
                                        String imageString ="";
                                        if(dat_obj.getString("file").trim().contains("http://")||dat_obj.getString("file").trim().contains("https://")){
                                            imageString = dat_obj.getString("file").trim();
                                        } else if(dat_obj.getString("file").trim().equals("")){
                                            imageString ="";
                                        } else {
                                            imageString = FILE_LINK_PATH+dat_obj.getString("file").trim();
                                        }
                                        News n = new News(dat_obj.getInt("id"),dat_obj.getString("title").trim(),dat_obj.getString("description").trim(),imageString
                                                ,dat_obj.getString("news_paper").trim(),relativeTimeSpan.toString(),dat_obj.getString("link").trim(),c);
                                        newses.add(n);
                                    }
                                } else if(categoryName.equals("")){
                                    if(returnInMillis(dat_obj.getString("publish_time"))<= returnInMillis(timeStamp)){
                                        String imageString ="";
                                        if(dat_obj.getString("file").trim().contains("http://")||dat_obj.getString("file").trim().contains("https://")){
                                            imageString = dat_obj.getString("file").trim();
                                        } else if(dat_obj.getString("file").trim().equals("")){
                                            imageString ="";
                                        } else {
                                            imageString = FILE_LINK_PATH+dat_obj.getString("file").trim();
                                        }
                                        News n = new News(dat_obj.getInt("id"),dat_obj.getString("title").trim(),dat_obj.getString("description").trim(),imageString
                                                ,dat_obj.getString("news_paper").trim(),relativeTimeSpan.toString(),dat_obj.getString("link").trim(),c);
                                        newses.add(n);
                                    }
                                } else if(categoryName.equals("Newspaper")){

                                    if(dat_obj.getString("news_paper").equals(getIntent().getStringExtra("from"))){
                                        if(returnInMillis(dat_obj.getString("publish_time"))<= returnInMillis(timeStamp)){
                                            String imageString ="";
                                            if(dat_obj.getString("file").trim().contains("http://")||dat_obj.getString("file").trim().contains("https://")){
                                                imageString = dat_obj.getString("file").trim();
                                            } else if(dat_obj.getString("file").trim().equals("")){
                                                imageString ="";
                                            } else {
                                                imageString = FILE_LINK_PATH+dat_obj.getString("file").trim();
                                            }
                                            News n = new News(dat_obj.getInt("id"),dat_obj.getString("title").trim(),dat_obj.getString("description").trim(),imageString
                                                    ,dat_obj.getString("news_paper").trim(),relativeTimeSpan.toString(),dat_obj.getString("link").trim(),c);
                                            newses.add(n);
                                        }
                                    }
                                }
                                Log.e("description", dat_obj.getString("publish_time"));
                                //Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                            }
                            if(newses.size()==0){
                                reload_containter.setVisibility(View.VISIBLE);
                                error_txt.setText("There is no news available right now. Try again later.");
                            }
                            mViewPager.setAdapter(mCustomPagerAdapter);
                            mViewPager.setCurrentItem(pos);
                            avi.hide();
                        } else {
                            avi.hide();
                            reload_containter.setVisibility(View.VISIBLE);
                            error_txt.setText("There is no news available right now. Try again later.");
                        }

                        //bindNews(gridSequenceRecyclerView);
                    } catch (JSONException e) {
                        Log.e("Get all data", "json parsing error: " + e.getMessage());
                        avi.hide();
                        reload_containter.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "Server is busy! Try again later!", Toast.LENGTH_LONG).show();
                        error_txt.setText("Server is busy! Try again later!");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("get all data", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    avi.hide();
                    reload_containter.setVisibility(View.VISIBLE);
                    error_txt.setText("Server is busy! Try again later!");
                    //Toast.makeText(Fullnews.this, "Server Error! Try again later!", Toast.LENGTH_LONG).show();
                }
            }){

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("language", language);
                    Log.i("Posting params: ", params.toString());

                    return params;
                }

            };

            //Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        } else {
            avi.hide();
            getLocalNews();
            //reload_containter.setVisibility(View.VISIBLE);
            //error_txt.setText("Please connect to your internet and try again");
            //Toast.makeText(getApplicationContext(), "Please connect to your internet and try again", Toast.LENGTH_LONG).show();
        }

    }

    public long returnInMillis(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        return millis;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.view_online:
                if(categoryName.equals("sqlite_news")){
                    News n = local_newses.get(mViewPager.getCurrentItem());
                    Intent i = new Intent(this, Webview_news.class);
                    i.putExtra("link", n.getLink());
                    startActivity(i);
                } else {
                    News n = newses.get(mViewPager.getCurrentItem());
                    Intent i = new Intent(this, Webview_news.class);
                    i.putExtra("link", n.getLink());
                    startActivity(i);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fullnews_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
