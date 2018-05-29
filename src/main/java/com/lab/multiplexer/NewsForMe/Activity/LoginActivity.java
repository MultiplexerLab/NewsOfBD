package com.lab.multiplexer.NewsForMe.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lab.multiplexer.NewsForMe.Activity.Helper.AppController;
import com.lab.multiplexer.NewsForMe.Activity.Helper.EndPoints;
import com.lab.multiplexer.NewsForMe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 3/11/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    String name,dp;
    ProgressDialog prog_dialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String accessToken;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        setContentView(R.layout.login_activity);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        if(isNetworkAvailable()){
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
               /* Toast.makeText(LoginActivity.this, "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG);*/

                    accessToken = loginResult.getAccessToken().getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            // Get facebook data from login

                            Bundle bFacebookData = getFacebookData(object);
                            name = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");
                            if(!bFacebookData.containsKey("email")){
                                Log.i("email", "null");
                                prog_dialog = ProgressDialog.show(LoginActivity.this, "",
                                        "Please wait...", true);
                                prog_dialog.setCancelable(false);
                                prog_dialog.show();
                                loginAuth(name,"No email available",dp,bFacebookData.getString("idFacebook"));
                            }else {
                                prog_dialog = ProgressDialog.show(LoginActivity.this, "",
                                        "Please wait...", true);
                                prog_dialog.setCancelable(false);
                                prog_dialog.show();
                                Log.i("email", bFacebookData.getString("email"));
                                loginAuth(name,bFacebookData.getString("email"),dp,bFacebookData.getString("idFacebook"));
                            }

                       /* editor.putString("user_id", bFacebookData.getString("idFacebook"));
                        editor.putString("name", name);
                        editor.putBoolean("status", true);
                        editor.commit();
                        // db.insertUser(name, bFacebookData.getString("email"), bFacebookData.getString("idFacebook"),0,0);
                        Log.i("Bundle Data", bFacebookData.getString("first_name") + bFacebookData.getString("email") + bFacebookData.getString("idFacebook"));
                        String email = bFacebookData.getString("email");
                        String device_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        if (email != null) {
                            backEndUserDataSending(name, bFacebookData.getString("email"), bFacebookData.getString("idFacebook"), 0, profilePicUrl, device_id);
                        } else if (profilePicUrl == null || profilePicUrl.equals("")) {
                            backEndUserDataSending(name, bFacebookData.getString("email"), bFacebookData.getString("idFacebook"), 0, "No Image", device_id);
                        } else {
                            backEndUserDataSending(name, "No Email", bFacebookData.getString("idFacebook"), 0, profilePicUrl, device_id);
                        }

                        editor.putString("photo_url", profilePicUrl);
                        editor.commit();*/

                            // backEndUserDataSending(name, bFacebookData.getString("email"), bFacebookData.getString("idFacebook"), 0);
                                               /* Intent playIntent = new Intent(MainActivity.this, MainActivityPlay.class);
                                                Log.actual_score("Intent", "Dhur");
                                                startActivity(playIntent);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();*/
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                    request.setParameters(parameters);
                    request.executeAsync();


                                        /*if (!db.checkQuestionTableData()) {
                                            new MainActivity.databaseWork().execute();

                                        }
                                        else {
                                            Intent playIntent = new Intent(MainActivity.this, MainActivityPlay.class);
                                            startActivity(playIntent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                        }*/
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG);

                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(LoginActivity.this, "Login attempt Failed.", Toast.LENGTH_LONG);

                }
            });

        } else {
            Toast.makeText(this,"Please turn on your internet first",Toast.LENGTH_LONG).show();
        }

    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                // profilePicUrl = profile_pic + "";
                Log.i("profile_pic", profile_pic + "");
                dp = profile_pic.toString();
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    private void loginAuth(final String method_name, final String email, final String prof_picture_link, final String fb_profile_id) {

        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.AUTH_LINK,
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
                            String user_id = obj.getString("user_id");
                            editor.putBoolean("logged_in",true);
                            editor.putString("user_id",user_id);
                            editor.commit();
                            finish();
                            /*if (getMessageFromServer.equals("User Already Exist")) {
                               //next step
                            } else {
                               //next step
                            }*/
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
                params.put("name", method_name);
                params.put("prof_picture_link", prof_picture_link);
                params.put("email", email);
                params.put("fb_profile_id", fb_profile_id);
                Log.i("Posting params: ", params.toString());

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, " ");
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(accessToken==null){
            editor.putBoolean("logged_in",false);
            editor.remove("user_id");
            editor.commit();
        }
    }
}
