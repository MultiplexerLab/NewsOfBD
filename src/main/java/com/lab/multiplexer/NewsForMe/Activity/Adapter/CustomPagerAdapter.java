package com.lab.multiplexer.NewsForMe.Activity.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lab.multiplexer.NewsForMe.Activity.CommentActivity;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints;
import com.lab.multiplexer.NewsForMe.Activity.LoginActivity;
import com.lab.multiplexer.NewsForMe.Activity.Model.News;
import com.lab.multiplexer.NewsForMe.Activity.Webview_news;
import com.lab.multiplexer.NewsForMe.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Miral on 3/14/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<News> list;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    ProgressDialog prog_dialog;
    public CustomPagerAdapter(Context context, ArrayList<News> listItem) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = listItem;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        pref = mContext.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        final News news_list = list.get(position);
        TextView Tittle = (TextView) itemView.findViewById(R.id.Title);
        TextView source = (TextView) itemView.findViewById(R.id.source);
        TextView time = (TextView) itemView.findViewById(R.id.time);
        TextView category_news = (TextView) itemView.findViewById(R.id.category_news);
        TextView pageCounter = (TextView) itemView.findViewById(R.id.pageCounter);
        TextView full_description = (TextView) itemView.findViewById(R.id.ful_description);
        ImageView bigImageView = (ImageView) itemView.findViewById(R.id.mBigImage);
        Button viewOnline = (Button) itemView.findViewById(R.id.view_online);
        ImageButton btn_fb = (ImageButton) itemView.findViewById(R.id.btn_fb);
        ImageButton btn_twitter = (ImageButton) itemView.findViewById(R.id.btn_twitter);
        ImageButton btn_google_plus = (ImageButton) itemView.findViewById(R.id.btn_google_plus);
        ImageButton btn_share = (ImageButton) itemView.findViewById(R.id.btn_share);
        Button btn_publisher = (Button) itemView.findViewById(R.id.btn_publisher);
        Button all_comment_btn = (Button) itemView.findViewById(R.id.all_comment_btn);
        Button comment_btn = (Button) itemView.findViewById(R.id.comment_btn);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/solaimanlipi.ttf");
        Typeface font_bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/solaimanlipi_bold.ttf");

        if (pref.getString("language", "").equals("Bangla")) {
            Tittle.setTypeface(font_bold);
            full_description.setTypeface(font);
        }

        category_news.setText(news_list.getCategoryName());
        source.setText(news_list.getSource());
        if(news_list.getTime().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()-21600000));
            CharSequence relativeTimeSpan = DateUtils.getRelativeTimeSpanString( returnInMillis(news_list.getTime().trim()), returnInMillis(timeStamp), 0);
            time.setText(relativeTimeSpan+"");
        } else {
            time.setText(news_list.getTime());
        }
        Tittle.setText(news_list.getTitle());
        full_description.setText(news_list.getDescription());
        if(!news_list.getImage().equals("")){
            Picasso.with(mContext).load(news_list.getImage()).error(R.drawable.no_image).into( bigImageView);
        } else {
            Picasso.with(mContext).load(R.drawable.no_image).into( bigImageView);
        }

        btn_publisher.setText(news_list.getSource());
        pageCounter.setText((position + 1) + "/" + getCount());
        viewOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, Webview_news.class);
                i.putExtra("link", news_list.getLink());
                mContext.startActivity(i);
            }
        });
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFB(news_list.getLink());
            }
        });
        btn_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTwitter(news_list.getTitle(), news_list.getLink());
            }
        });
        btn_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callGooglePlus(news_list.getTitle(), news_list.getLink());
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareIntent(news_list.getTitle(), news_list.getLink());
            }
        });
        all_comment_btn.setText("All Comments ("+news_list.getComment_count()+")");
        all_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.contains("logged_in")) {
                    if (pref.getBoolean("logged_in", true)) {
                        Intent i = new Intent(mContext, CommentActivity.class);
                        i.putExtra("comment_count",news_list.getComment_count());
                        i.putExtra("title",news_list.getTitle());
                        i.putExtra("newsID",news_list.getId()+"");
                        mContext.startActivity(i);
                    } else {
                        Toast.makeText(mContext, "You've to login first to comment", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(i);
                    }
                } else {
                    Toast.makeText(mContext, "You've to login first to comment", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                }
            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.contains("logged_in")) {
                    if (pref.getBoolean("logged_in", true)) {
                        commentAlertBox(news_list.getTitle(),news_list.getId());
                        //Toast.makeText(mContext, "Comment box will show here", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "You've to login first to comment", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(i);
                    }
                } else {
                    Toast.makeText(mContext, "You've to login first to comment", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                }
            }
        });
        container.addView(itemView);

        return itemView;
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

    public void callFB(String url) {
        String urlToShare = url;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing from News For Me"); // NB: has no effect!
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }
// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        mContext.startActivity(intent);
    }

    public void callGooglePlus(String tittle, String url) {
        String urlToShare = url;
        Intent shareIntent = ShareCompat.IntentBuilder.from((Activity) mContext)
                .setType("text/plain")
                .setText("Sharing from News Of BD\n" + tittle + "\n" + urlToShare)
                .setStream(null)
                .getIntent()
                .setPackage("com.google.android.apps.plus");
        boolean googlePlusApp = false;
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(shareIntent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.apps.plus")) {
                googlePlusApp = true;
                break;
            }
        }
        if (!googlePlusApp) {
            String sharerUrl = "https://plus.google.com/share?url=" + urlToShare;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        mContext.startActivity(shareIntent);
    }

    public void callTwitter(String tittle, String url) {
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(tittle),
                urlEncode(url + " from News Of BD"));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        mContext.startActivity(intent);
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }


    public void shareIntent(String title, String url) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + url);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share from News Of BD"));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    public void commentAlertBox(String title, final int newsID) {
        final View view = mLayoutInflater.inflate(R.layout.comment_box, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Please Enter your comment below");


        final EditText etComments = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etComments.getText().toString().equals("")){
                    Toast.makeText(mContext, "You haven't write any comment", Toast.LENGTH_LONG).show();
                } else {
                    prog_dialog = ProgressDialog.show(mContext, "",
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
                                Toast.makeText(mContext, "Your comment is submitted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error in submitting comment", Toast.LENGTH_SHORT).show();
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



}
