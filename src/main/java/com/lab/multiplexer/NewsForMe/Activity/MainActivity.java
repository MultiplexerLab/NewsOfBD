package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.lab.multiplexer.NewsForMe.Activity.Fragment.Bangladesh;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Business;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Economy;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Entertainment;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Highlights;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.International;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.LiveCricket;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Politics;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Sports;
import com.lab.multiplexer.NewsForMe.Activity.Fragment.Technology;
import com.lab.multiplexer.NewsForMe.R;
import com.melnykov.fab.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab;
    private SpringFloatingActionMenu springFloatingActionMenu;
    BottomBar bottomBar;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String login_txt = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        if(!pref.contains("language")){
            editor.putString("language","Bangla");
            editor.commit();
        }
        if(pref.contains("logged_in")){
            if(pref.getBoolean("logged_in",true)){
                login_txt = "Log out";
            }
        }
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        final TextView switchText = (TextView) toolbar.findViewById(R.id.switch_text);
        Switch lang_switch = (Switch)toolbar.findViewById(R.id.pin);
        if(pref.getString("language","").equals("English")){
            lang_switch.toggle();
            switchText.setText("English");
        }
        lang_switch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Animation a = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_in_right);
                    switchText.setText("English");
                    switchText.setAnimation(a);
                    editor.putString("language","English");
                    editor.commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Write code for your refresh logic
                            Intent i = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            finish();
                        }
                    }, 1000);

                } else {
                    Animation a = AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_in_right);
                    switchText.setText("বাংলা");
                    switchText.setAnimation(a);
                    editor.putString("language","Bangla");
                    editor.commit();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Write code for your refresh logic
                            Intent i = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(0,0);
                            finish();
                        }
                    }, 1000);
                }
            }
        });

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_home);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }

            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_newspaper) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(MainActivity.this,Newspaper.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_stream) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(MainActivity.this,Stream.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                }
                if (tabId == R.id.tab_save) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent i = new Intent(MainActivity.this,Bookmark.class);
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
  /*      tabLayout.setBackgroundColor(Color.parseColor("#4D8DCE"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));*/

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
                        Intent i = new Intent(MainActivity.this,Settings.class);
                        startActivity(i);

                    }
                })
                .addMenuItem(R.color.Navy, R.drawable.ic_newspaper, "Newspaper", android.R.color.white, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this,Newspaper.class);
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
                        Intent i = new Intent(MainActivity.this,MainActivity.class);
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
                        bottomBar.setDefaultTab(R.id.tab_home);
                    }
                })
                .build();

        springFloatingActionMenu.setVisibility(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(pref.getString("language","").equals("Bangla")){
            adapter.addFragment(new Highlights(), "হাইলাইটস");
            adapter.addFragment(new Bangladesh(), "বাংলাদেশ");
            adapter.addFragment(new International(), "আন্তর্জাতিক");
            adapter.addFragment(new Sports(), "খেলা");
            adapter.addFragment(new LiveCricket(), "লাইভ ক্রিকেট");
            adapter.addFragment(new Technology(), "বিজ্ঞান ও প্রযুক্তি");
            adapter.addFragment(new Politics(), "রাজনীতি");
            adapter.addFragment(new Business(), "ব্যাবসায়ীক খবর");
            adapter.addFragment(new Economy(), "অর্থনীতি");
            adapter.addFragment(new Entertainment(), "বিনোদন");
            viewPager.setAdapter(adapter);
        } else {
            adapter.addFragment(new Highlights(), "Highlights");
            adapter.addFragment(new Bangladesh(), "Bangladesh");
            adapter.addFragment(new International(), "International");
            adapter.addFragment(new Sports(), "Sports");
            adapter.addFragment(new LiveCricket(), "Live Cricket");
            adapter.addFragment(new Technology(), "Technology");
            adapter.addFragment(new Politics(), "Politics");
            adapter.addFragment(new Business(), "Business");
            adapter.addFragment(new Economy(), "Economy");
            adapter.addFragment(new Entertainment(), "Entertainment");
            viewPager.setAdapter(adapter);
        }

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
