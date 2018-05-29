package com.lab.multiplexer.NewsForMe.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.lab.multiplexer.NewsForMe.Activity.Cell.NewspaperCell;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints;
import com.lab.multiplexer.NewsForMe.Activity.Helper.Variables;
import com.lab.multiplexer.NewsForMe.Activity.Model.Newspaper__model;
import com.lab.multiplexer.NewsForMe.R;
import com.melnykov.fab.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Newspaper extends AppCompatActivity {
    @BindView(R.id.gridSequenceRecyclerView)
    SimpleRecyclerView gridSequenceRecyclerView;
    List<Newspaper__model> newspaperModels;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AVLoadingIndicatorView avi;
    @BindView(R.id.reload_container)
    RelativeLayout reload_containter;
    @BindView(R.id.reload_btn)
    ImageButton reload_btn;
    @BindView(R.id.error_txt)
    TextView error_txt;
    JSONArray dataDetailsObj;
    BottomBar bottomBar;
    ProgressDialog prog_dialog;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    FloatingActionButton fab;
    String login_txt = "Login";
    private SpringFloatingActionMenu springFloatingActionMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newspaper);
        ButterKnife.bind(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("Newspapers");
        if(pref.contains("logged_in")){
            if(pref.getBoolean("logged_in",true)){
                login_txt = "Log out";
            }
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        newspaperModels = new ArrayList<>();
        fetchNewspapers();
        gridSequenceRecyclerView.setNestedScrollingEnabled(false);
        //bindNews(gridSequenceRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newspaperModels.clear();
                gridSequenceRecyclerView.removeAllCells();
                gridSequenceRecyclerView.removeAllViews();
                reload_containter.setVisibility(View.GONE);
                avi.show();
                fetchNewspapers();
            }
        });
        reload_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Newspaper.this, Newspaper.class);
                startActivity(i);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(), "Refreshed" , Toast.LENGTH_LONG).show();
                finish();
            }
        });

        reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Newspaper.this, Newspaper.class);
                startActivity(i);
                overridePendingTransition(0,0);
                Toast.makeText(getApplicationContext(), "Refreshed" , Toast.LENGTH_LONG).show();
                finish();
            }
        });

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_newspaper);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_newspaper) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Newspaper.this,Newspaper.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }

            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Newspaper.this,MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_stream) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Newspaper.this,Stream.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_save) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Newspaper.this,Bookmark.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_menu) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    fab.setVisibility(View.VISIBLE);
                    springFloatingActionMenu.setVisibility(View.VISIBLE);
                    springFloatingActionMenu.showMenu();
                }

            }
        });
        createFloatingMenu();

    }


    public void createFloatingMenu(){
        //create your own FAB
        fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageResource(R.drawable.ic_menu);
        fab.setColorPressedResId(R.color.colorPrimaryDark);
        fab.setColorNormalResId(R.color.Red);
        fab.setColorRippleResId(android.R.color.white);
        fab.setShadow(true);
        fab.setVisibility(View.GONE);

        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                .revealColor(android.R.color.white)
                //add menu item via addMenuItem(bgColor,icon,label,label color,onClickListener)
                .addMenuItem(R.color.Purplish, R.drawable.ic_gift, "Invite", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .addMenuItem(R.color.Green, R.drawable.ic_login, login_txt, android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Test", "Dhukse");
                        Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(loginIntent);

                    }
                })

                .addMenuItem(R.color.Orange, R.drawable.ic_settings, "Settings", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Newspaper.this,Settings.class);
                        startActivity(i);

                    }
                })
                .addMenuItem(R.color.Navy, R.drawable.ic_newspaper, "Newspaper", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Newspaper.this,Newspaper.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                    }
                })
                .addMenuItem(android.R.color.black, R.drawable.ic_bangla, pref.getString("language",""), android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pref.getString("language","").equals("English")){
                            editor.putString("language","Bangla");
                            editor.commit();
                        } else {
                            editor.putString("language","English");
                            editor.commit();
                        }
                        Intent i = new Intent(Newspaper.this,Newspaper.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0,0);
                    }
                })
                .addMenuItem(R.color.pink, R.drawable.ic_rate_us, "Rate Us", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })


                //you can choose menu layout animation
                //设置动画类型
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                //setup reveal color while the menu opening
                //设置reveal效果的颜色
                .revealColor(R.color.colorPrimary)
                //set FAB location, only support bottom center and bottom right
                //设置FAB的位置,只支持底部居中和右下角的位置
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        //set FAB icon when the menu opened
                        //设置FAB的icon当菜单打开的时候
                        fab.setImageResource(R.drawable.ic_delete);
                    }

                    @Override
                    public void onMenuClose() {
                        //set back FAB icon when the menu closed
                        //设置回FAB的图标当菜单关闭的时候
                        fab.setImageResource(R.drawable.ic_menu);
                        springFloatingActionMenu.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        bottomBar.setDefaultTab(R.id.tab_newspaper);
                    }
                })
                .build();

        springFloatingActionMenu.setVisibility(View.GONE);
    }


    private void fetchNewspapers() {
        if (isNetworkAvailable(getApplicationContext())) {
            StringRequest strReq = new StringRequest(Request.Method.GET,EndPoints.FETCH_NEWSPAPERS, new Response.Listener<String>() {
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

                                Newspaper__model n = new Newspaper__model(dat_obj.getInt(Variables.NEWSPAPER_ID),dat_obj.getString(Variables.NEWSPAPER_NAME).trim(),dat_obj.getString(Variables.NEWSPAPER_TYPE).trim());
                                newspaperModels.add(n);

                                Log.e("description", dat_obj.getString(Variables.NEWSPAPER_NAME));
                                //Toast.makeText(getActivity(), details, Toast.LENGTH_LONG).show();
                            }
                            if(newspaperModels.size()==0){
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

        for (Newspaper__model newspaperModel : newspaperModels) {
            NewspaperCell cell = new NewspaperCell(newspaperModel);
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

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}
