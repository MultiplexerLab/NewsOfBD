package com.lab.multiplexer.NewsForMe.Activity.Helper;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static AppController application;
	private static PlaylistManager playlistManager;
	private static AppController mInstance;
	private MyPreferenceManager pref;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		enableStrictMode();
		application = this;
		playlistManager = new PlaylistManager();
		/*AutoErrorReporter.get(this)
				.setEmailAddresses("37185akif@gmail.com")
				.setEmailSubject("Auto Crash Report")
				.start();*/
	}

	@Override
	public void onTerminate() {
		super.onTerminate();

		application = null;
		playlistManager = null;
	}

	public static PlaylistManager getPlaylistManager() {
		return playlistManager;
	}
	public static AppController getApplication() {
		return application;
	}
	private void enableStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
				.build());
	}
	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}



	public MyPreferenceManager getPrefManager() {
		if (pref == null) {
			pref = new MyPreferenceManager(this);
		}

		return pref;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}



	/*public void logout() {
		//pref.clear();
		Intent intent = new Intent(this, LoginActivity_hudai.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}*/
}