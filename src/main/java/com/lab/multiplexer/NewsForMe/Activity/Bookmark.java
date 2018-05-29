package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.lab.multiplexer.NewsForMe.Activity.Cell.SavedNewsCell;
import com.lab.multiplexer.NewsForMe.Activity.Database.DatabaseHelper;
import com.lab.multiplexer.NewsForMe.Activity.Model.Ad;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.Activity.Utils.DataUtils;
import com.lab.multiplexer.NewsForMe.R;
import com.melnykov.fab.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Bookmark extends AppCompatActivity {
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
    FloatingActionButton fab;
    String login_txt = "Login";
    private SpringFloatingActionMenu springFloatingActionMenu;
    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        getNews();

        gridSequenceRecyclerView.setNestedScrollingEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newses.clear();
                gridSequenceRecyclerView.removeAllCells();
                gridSequenceRecyclerView.removeAllViews();
                reload_containter.setVisibility(View.GONE);
                avi.show();
                getNews();
            }
        });
        reload_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

         bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_save);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_save) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Bookmark.this,Bookmark.class);
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
                    Intent i = new Intent(Bookmark.this,MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_newspaper) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Bookmark.this,Newspaper.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_stream) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Bookmark.this,Stream.class);
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
                        Intent i = new Intent(getBaseContext(),Settings.class);
                        startActivity(i);

                    }
                })
                .addMenuItem(R.color.Navy, R.drawable.ic_newspaper, "Newspaper", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getBaseContext(),Newspaper.class);
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
                        Intent i = new Intent(getBaseContext(),Bookmark.class);
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
                        bottomBar.setDefaultTab(R.id.tab_save);
                    }
                })
                .build();

        springFloatingActionMenu.setVisibility(View.GONE);
    }



    public void getNews(){
        newses = db.getAllSaveNews(pref.getString("language",""));
        if(newses.size()==0){
            reload_containter.setVisibility(View.VISIBLE);
            error_txt.setText("You haven't saved any news yet. Long press on the news to save news and read it offline");
        }
        bindNews(gridSequenceRecyclerView);
        avi.hide();
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void bindNews(SimpleRecyclerView simpleRecyclerView) {
        bindNews(simpleRecyclerView, false);
    }

    private void bindNews(SimpleRecyclerView simpleRecyclerView, boolean fullSpan) {
        //newses = DataUtils.getBooks();
        List<Ad> ads = DataUtils.getAds();
        List<SimpleCell> cells = new ArrayList<>();

        for (News news : newses) {
            SavedNewsCell cell = new SavedNewsCell(news);
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
