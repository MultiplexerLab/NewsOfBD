package com.lab.multiplexer.NewsForMe.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.lab.multiplexer.NewsForMe.Activity.Cell.CommentCell;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints;
import com.lab.multiplexer.NewsForMe.Activity.Model.Comment;
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

public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.gridSequenceRecyclerView)
    SimpleRecyclerView gridSequenceRecyclerView;
    List<Comment> comments;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AVLoadingIndicatorView avi;
    @BindView(R.id.reload_container)
    RelativeLayout reload_containter;
    @BindView(R.id.reload_btn)
    ImageButton reload_btn;
    @BindView(R.id.error_txt)
    TextView error_txt;
    JSONArray dataDetailsObj;

    ProgressDialog prog_dialog;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("Comments ("+getIntent().getIntExtra("comment_count",0)+")");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView Tittle = (TextView) findViewById(R.id.Title);
        Typeface font_bold = Typeface.createFromAsset(getAssets(), "fonts/solaimanlipi_bold.ttf");
        Tittle.setText(getIntent().getStringExtra("title"));
        Tittle.setTypeface(font_bold);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        comments = new ArrayList<>();
        fetchComment(getIntent().getStringExtra("newsID"));
        gridSequenceRecyclerView.setNestedScrollingEnabled(false);
        //bindNews(gridSequenceRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                comments.clear();
                gridSequenceRecyclerView.removeAllCells();
                gridSequenceRecyclerView.removeAllViews();
                reload_containter.setVisibility(View.GONE);
                avi.show();
                fetchComment(getIntent().getStringExtra("newsID"));
            }
        });
        reload_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CommentActivity.this, CommentActivity.class);
                i.putExtra("comment_count",getIntent().getIntExtra("comment_count",0)+1);
                i.putExtra("title",getIntent().getStringExtra("title"));
                i.putExtra("newsID",getIntent().getStringExtra("newsID"));
                startActivity(i);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(), "Refreshed" , Toast.LENGTH_LONG).show();
                finish();
            }
        });
        reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommentActivity.this, CommentActivity.class);
                i.putExtra("comment_count",getIntent().getIntExtra("comment_count",0)+1);
                i.putExtra("title",getIntent().getStringExtra("title"));
                i.putExtra("newsID",getIntent().getStringExtra("newsID"));
                startActivity(i);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(), "Refreshed" , Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }




    private void fetchComment(final String newsID) {
        if (isNetworkAvailable(getApplicationContext())) {
            StringRequest strReq = new StringRequest(Request.Method.POST,EndPoints.FETCH_COMMENT_LINK, new Response.Listener<String>() {
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
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()-21600000));
                                final CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString( returnInMillis(dat_obj.getString("date_time").trim()), returnInMillis(timeStamp), 0);

                                Comment c = new Comment(dat_obj.getInt("user_id"),dat_obj.getString("name").trim(),dat_obj.getString("prof_picture_link").trim(),dat_obj.getString("content").trim(),relativeTimeSpan.toString(),dat_obj.getString("fb_profile_id").trim());
                                comments.add(c);

                                Log.e("description", dat_obj.getString("content"));
                                //Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                            }
                            if(comments.size()==0){
                                reload_containter.setVisibility(View.VISIBLE);
                                error_txt.setText("Add the first comment from the toolbar");
                            }
                            bindComments(gridSequenceRecyclerView);
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
                            error_txt.setText("Add the first comment from the toolbar");
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
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("news_id", newsID);
                    Log.i("Posting params: ", params.toString());

                    return params;
                }

            };

            //Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq);
        } else {
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            avi.hide();
            reload_containter.setVisibility(View.VISIBLE);
            error_txt.setText("Please connect to your internet and try again");
            //Toast.makeText(getActivity().getApplicationContext(), "Please connect to your internet and try again", Toast.LENGTH_LONG).show();
        }

    }
    private void bindComments(SimpleRecyclerView simpleRecyclerView) {
        bindComments(simpleRecyclerView, false);
    }

    private void bindComments(SimpleRecyclerView simpleRecyclerView, boolean fullSpan) {
        //newses = DataUtils.getBooks();
        //List<Ad> ads = DataUtils.getAds();
        List<SimpleCell> cells = new ArrayList<>();

        for (Comment comment : comments) {
            CommentCell cell = new CommentCell(comment);
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


    public void commentAlertBox(String title, final int newsID) {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = mLayoutInflater.inflate(R.layout.comment_box, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Please Enter your comment below");


        final EditText etComments = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etComments.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "You haven't write any comment", Toast.LENGTH_LONG).show();
                } else {
                    prog_dialog = ProgressDialog.show(CommentActivity.this, "",
                            "Please wait...", true);
                    prog_dialog.setCancelable(false);
                    prog_dialog.show();
                    enterComment(newsID,pref.getString("user_id",""),etComments.getText().toString());
                }

            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alertDialog.setView(view);
        alertDialog.show();
    }


    private void enterComment(final int news_id, final String user_id, final String content) {

        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.COMMENT_LINK,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response.toString());
                        try {
                            if(prog_dialog.isShowing()){
                                prog_dialog.dismiss();
                            }
                            JSONObject obj = new JSONObject(response);
                            String getMessageFromServer = obj.getString("message");
                            if(getMessageFromServer.equals("Successful")){
                                Toast.makeText(CommentActivity.this, "Your comment is submitted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CommentActivity.this, CommentActivity.class);
                                i.putExtra("comment_count",getIntent().getIntExtra("comment_count",0)+1);
                                i.putExtra("title",getIntent().getStringExtra("title"));
                                i.putExtra("newsID",getIntent().getStringExtra("newsID"));
                                startActivity(i);
                                overridePendingTransition(0,0);
                                finish();
                            } else {
                                Toast.makeText(CommentActivity.this, "Error in submitting comment", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("local Reg", "Error: " + error.getMessage());
                if(prog_dialog.isShowing()){
                    prog_dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                //  progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("news_id", news_id+"");
                params.put("user_id", user_id+"");
                params.put("content", content);
                Log.i("Posting params: ", params.toString());

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, " ");
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_comment:
                commentAlertBox(getIntent().getStringExtra("title"),Integer.parseInt(getIntent().getStringExtra("newsID")));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.comment_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
