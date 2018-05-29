package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.lab.multiplexer.NewsForMe.R;
import com.melnykov.fab.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

public class Stream extends AppCompatActivity {
    FloatingActionButton fab;
    private SpringFloatingActionMenu springFloatingActionMenu;
    BottomBar bottomBar;
    public String STREAM_TV_LINK = "http://www.jagobd.com/category/bangla-channel";
    public String STREAM_RADIO_LINK = "http://www.jagobd.com/category/bangla-radio";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String login_txt = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        if(pref.contains("logged_in")){
            if(pref.getBoolean("logged_in",true)){
                login_txt = "Log out";
            }
        }

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_stream);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_stream) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Stream.this,Stream.class);
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
                    Intent i = new Intent(Stream.this,MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_newspaper) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Stream.this,Newspaper.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }

                if (tabId == R.id.tab_save) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(Stream.this,Bookmark.class);
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

        LinearLayout tv_container = (LinearLayout) findViewById(R.id.tv_container);
        tv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Stream.this, Webview_news.class);
                i.putExtra("link", STREAM_TV_LINK);
                startActivity(i);*/
                Intent i = new Intent(Stream.this, VideoSelectionActivity.class);
                startActivity(i);
            }
        });

        LinearLayout radio_container = (LinearLayout) findViewById(R.id.radio_container);
        radio_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Stream.this, Webview_news.class);
                i.putExtra("link", STREAM_RADIO_LINK);
                startActivity(i);*/
                Intent i = new Intent(Stream.this, AudioSelectionActivity.class);
                startActivity(i);
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

                .addMenuItem(R.color.Green, R.drawable.ic_login,login_txt, android.R.color.white, new View.OnClickListener() {
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
                        Intent i = new Intent(Stream.this,Settings.class);
                        startActivity(i);
                    }
                })
                .addMenuItem(R.color.Navy, R.drawable.ic_newspaper, "Newspaper", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Stream.this,Newspaper.class);
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
                        Intent i = new Intent(Stream.this,Stream.class);
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
                        bottomBar.setDefaultTab(R.id.tab_stream);
                    }
                })
                .build();

        springFloatingActionMenu.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (springFloatingActionMenu.isMenuOpen()) {
            springFloatingActionMenu.hideMenu();
            springFloatingActionMenu.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }



}
