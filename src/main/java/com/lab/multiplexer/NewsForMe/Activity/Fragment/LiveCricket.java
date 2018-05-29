package com.lab.multiplexer.NewsForMe.Activity.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.lab.multiplexer.NewsForMe.Activity.Cell.LiveCricketCell;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints.LIVE_CRIC_LINK;

public class LiveCricket extends Fragment {

    @BindView(R.id.gridSequenceRecyclerView)
    SimpleRecyclerView gridSequenceRecyclerView;
    List<News> newses;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.reload_container)
    RelativeLayout reload_containter;
    @BindView(R.id.reload_btn)
    ImageButton reload_btn;
    @BindView(R.id.error_txt)
    TextView error_txt;
    @BindView(R.id.activity_main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    public LiveCricket() {
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
        ButterKnife.bind(this,rootView);
        newses = new ArrayList<>();
        getAllCricData();
        gridSequenceRecyclerView.setNestedScrollingEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newses.clear();
                gridSequenceRecyclerView.removeAllCells();
                gridSequenceRecyclerView.removeAllViews();
                reload_containter.setVisibility(View.GONE);
                avi.show();
                getAllCricData();
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


    private void bindNews(SimpleRecyclerView simpleRecyclerView) {
        bindNews(simpleRecyclerView, false);
    }

    private void bindNews(SimpleRecyclerView simpleRecyclerView, boolean fullSpan) {
        //newses = DataUtils.getBooks();
        List<Ad> ads = DataUtils.getAds();
        List<SimpleCell> cells = new ArrayList<>();

        for (News news : newses) {
            LiveCricketCell cell = new LiveCricketCell(news);
            cell.setShowHandle(true);
            cells.add(cell);
        }

        //ad!

      /*  for (Ad ad : ads) {
            NewsAdCell cell = new NewsAdCell(ad);
            int position = (ads.indexOf(ad) + 1) * 3;
            if (fullSpan) {
                cell.setSpanSize(simpleRecyclerView.getGridSpanCount());
            }
            cells.add(position, cell);
        }
*/
        simpleRecyclerView.addCells(cells);
    }

    private void getAllCricData() {
        if (isNetworkAvailable(getActivity())) {
            StringRequest strReq = new StringRequest(Request.Method.GET,LIVE_CRIC_LINK, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e("get all data", "response: " + response);
                    //  Toast.makeText(getApplicationContext(), "Fetch: " +response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject obj = new JSONObject(response);
                        // check for error
                        JSONArray dataDetailsObj = obj.getJSONArray("data");
                        if(dataDetailsObj.length()>0){
                            for (int i = 0; i < dataDetailsObj.length(); i++) {
                                JSONObject dat_obj = (JSONObject) dataDetailsObj.get(i);
                                String details = dat_obj.getString("description");
                                Category c = new Category(i,"Live Cricket"+i);
                                News n = new News(i,details,"","","","","",c);
                                newses.add(n);
                                Log.e("description", details);
                                //Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                            }
                            bindNews(gridSequenceRecyclerView);
                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            avi.hide();
                        } else {
                            avi.hide();
                            if(mSwipeRefreshLayout.isRefreshing()){
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
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
                }
            });

            //Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        } else {
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            avi.hide();
            reload_containter.setVisibility(View.VISIBLE);
            error_txt.setText("Please connect to your internet and try again");
        }

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}



