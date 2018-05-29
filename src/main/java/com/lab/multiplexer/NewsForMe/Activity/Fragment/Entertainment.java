package com.lab.multiplexer.NewsForMe.Activity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.lab.multiplexer.NewsForMe.Activity.Cell.NonHighlightedNewsCell;
import com.lab.multiplexer.NewsForMe.Activity.Database.DatabaseHelper;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Model.Ad;
import com.lab.multiplexer.NewsForMe.Activity.Model.Category;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.Activity.Utils.DataUtils;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints.API_LINK;
import static com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints.FILE_LINK_PATH;

public class Entertainment extends Fragment {
    @BindView(R.id.gridSequenceRecyclerView)
    SimpleRecyclerView gridSequenceRecyclerView;
    List<News> newses;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONArray dataDetailsObj;
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
    public Entertainment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.highlights, container, false);
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        db = new DatabaseHelper(getActivity());
        avi = (AVLoadingIndicatorView) rootView.findViewById(R.id.avi);
        ButterKnife.bind(this,rootView);
        newses = new ArrayList<>();
        getAllNews(pref.getString("language",""));
        //bindNews(gridSequenceRecyclerView);
        gridSequenceRecyclerView.setNestedScrollingEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newses.clear();
                gridSequenceRecyclerView.removeAllCells();
                gridSequenceRecyclerView.removeAllViews();
                reload_containter.setVisibility(View.GONE);
                avi.show();
                getAllNews(pref.getString("language",""));
            }
        });
        reload_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        return rootView;
    }





    private void getAllNews(final String language) {
        if (isNetworkAvailable(getActivity().getApplicationContext())) {
            StringRequest strReq = new StringRequest(Request.Method.POST,API_LINK, new Response.Listener<String>() {
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
                                Category c = new Category(i,dat_obj.getString("category_id"));
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()-21600000));
                                final CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString( returnInMillis(dat_obj.getString("publish_time").trim()), returnInMillis(timeStamp), 0);
                                if(dat_obj.getString("category_id").toLowerCase().equals("entertainment")){
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
                                        n.setPublish_time(dat_obj.getString("publish_time"));
                                        newses.add(n);
                                    }
                                }
                                Log.e("description", dat_obj.getString("publish_time"));
                                //Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                            }
                            if(newses.size()==0){
                                reload_containter.setVisibility(View.VISIBLE);
                                error_txt.setText("There is no news available right now. Try again later.");
                            }
                            bindNews(gridSequenceRecyclerView);
                            avi.hide();
                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        } else {
                            avi.hide();
                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            reload_containter.setVisibility(View.VISIBLE);
                            error_txt.setText("There is no news available right now. Try again later.");
                        }

                    } catch (JSONException e) {
                        Log.e("Get all data", "json parsing error: " + e.getMessage());
                        if(mSwipeRefreshLayout.isRefreshing()){
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        avi.hide();
                        reload_containter.setVisibility(View.VISIBLE);
                        error_txt.setText("Server is busy! Try again!");
                        //Toast.makeText(getActivity().getApplicationContext(), "Server is busy! Try again later!", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("get all data", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    avi.hide();
                    reload_containter.setVisibility(View.VISIBLE);
                    error_txt.setText("Server is busy! Try again!");
                    //Toast.makeText(getActivity().getApplicationContext(), "Server Error! Try again later!", Toast.LENGTH_LONG).show();
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
            AppController.getInstance().getRequestQueue().getCache().remove(API_LINK);
            AppController.getInstance().getRequestQueue().getCache().invalidate(API_LINK, true);
        } else {
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            avi.hide();
            newses = db.getAllNews("Entertainment",pref.getString("language",""));
            if(newses.size()==0){
                reload_containter.setVisibility(View.VISIBLE);
                error_txt.setText("Connect to internet to see latest News");
            }
            bindNews(gridSequenceRecyclerView);
            //Toast.makeText(getActivity().getApplicationContext(), "Please connect to your internet and try again", Toast.LENGTH_LONG).show();
        }

    }
    public long returnInMillis(String time){
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

    private void bindNews(SimpleRecyclerView simpleRecyclerView) {
        bindNews(simpleRecyclerView, false);
    }

    private void bindNews(SimpleRecyclerView simpleRecyclerView, boolean fullSpan) {
        //newses = DataUtils.getBooks();
        List<Ad> ads = DataUtils.getAds();
        List<SimpleCell> cells = new ArrayList<>();

        for (News news : newses) {
            NonHighlightedNewsCell cell = new NonHighlightedNewsCell(news);
            cells.add(cell);
        }
 /*       if(dataDetailsObj.length()>7){
            for (Ad ad : ads) {
                NewsAdCell cell = new NewsAdCell(ad);
                int position = (ads.indexOf(ad) + 1) * 3;
                if (fullSpan) {
                    cell.setSpanSize(simpleRecyclerView.getGridSpanCount());
                }
                cells.add(position, cell);
            }
        }
*/

        simpleRecyclerView.addCells(cells);
    }


}



