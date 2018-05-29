package com.lab.multiplexer.NewsForMe.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lab.multiplexer.NewsForMe.R;

public class Settings extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public static String WIFI_MOBILE = "Wifi or mobile";
    public static String WIFI= "Wifi only";
    public static String NEVER= "Never";
    public static String IMAGE_DOWNLOAD_PREF= "img_download";
    public static String OFFLINE_CONTENT_PREF= "offline_content_download";
    TextView img_txt,offline_content_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        if(!pref.contains(IMAGE_DOWNLOAD_PREF)){
            editor.putString(IMAGE_DOWNLOAD_PREF,WIFI_MOBILE);
            editor.commit();
        }
        if(!pref.contains(OFFLINE_CONTENT_PREF)){
            editor.putString(OFFLINE_CONTENT_PREF,WIFI);
            editor.commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        LinearLayout img_download = (LinearLayout) findViewById(R.id.image_download);
        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAlertBox("Image download preference",pref.getString(IMAGE_DOWNLOAD_PREF,""),IMAGE_DOWNLOAD_PREF);
            }
        });
        LinearLayout offline_newspaper = (LinearLayout) findViewById(R.id.offline_content);
        offline_newspaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAlertBox("Offline content download preference",pref.getString(OFFLINE_CONTENT_PREF,""),OFFLINE_CONTENT_PREF);
            }
        });

        img_txt = (TextView) findViewById(R.id.img_pref_txt);
        offline_content_txt = (TextView) findViewById(R.id.offline_content_pref_txt);
        img_txt.setText(pref.getString(IMAGE_DOWNLOAD_PREF,""));
        offline_content_txt.setText(pref.getString(OFFLINE_CONTENT_PREF,""));

    }

    public void commentAlertBox(String title, String check, final String sharePref_string) {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = mLayoutInflater.inflate(R.layout.newspaper_settings_layout, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(true);
        RadioButton never = (RadioButton) view.findViewById(R.id.never);
        RadioButton wifi_mobile = (RadioButton) view.findViewById(R.id.wifi_mobile);
        RadioButton wifi = (RadioButton) view.findViewById(R.id.wifi);

        if(check.equals(WIFI_MOBILE)){
            wifi_mobile.setChecked(true);
        } else if(check.equals(WIFI)){
            wifi.setChecked(true);
        } else {
            never.setChecked(true);
        }

        final RadioGroup radioPrefGroup =(RadioGroup)view.findViewById(R.id.radioGroup);
        radioPrefGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioPrefButton=(RadioButton)view.findViewById(checkedId);
                if(sharePref_string.equals(IMAGE_DOWNLOAD_PREF)){
                    editor.putString(IMAGE_DOWNLOAD_PREF,radioPrefButton.getText().toString());
                    editor.commit();
                    img_txt.setText(pref.getString(IMAGE_DOWNLOAD_PREF,""));
                } else {
                    editor.putString(OFFLINE_CONTENT_PREF,radioPrefButton.getText().toString());
                    editor.commit();
                    offline_content_txt.setText(pref.getString(OFFLINE_CONTENT_PREF,""));
                }
                alertDialog.dismiss();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
