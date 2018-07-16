package com.teamproject.plastikproject;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.teamproject.plastikproject.helpers.SharedPrefHelper;
import com.teamproject.plastikproject.helpers.ShoppingContentProvider;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.services.GpsAppointmentService;

import io.fabric.sdk.android.Fabric;

/**
 * Created by rage on 06.02.15.
 *
 * Application Class
 */
public class AppClass extends Application {

    private static final String TAG = AppClass.class.getSimpleName();
    private Handler handler;
    private int count = 0;


    private RequestQueue mRequestQueue;
    private static AppClass mInstance;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SharedPrefHelper.getInstance(getApplicationContext());
        SqlDbHelper.getInstance(getApplicationContext());

        readCount();
        mInstance = this;
        handler = new Handler();

        getContentResolver().registerContentObserver(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI,
                true,
                contentObserver
        );

    }

    public static synchronized AppClass getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private ContentObserver contentObserver = new ContentObserver(handler) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.d(TAG, "onChange: " + uri.toString());
            readCount();
        }
    };

    private void readCount() {
        Cursor cursor = getContentResolver().query(
                ShoppingContentProvider.PURCHASE_LIST_CONTENT_COUNT_WITH_PLACE_URI,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        Log.d(TAG, "COUNT: " + count);

        if (this.count == 0 && count > 0) {
            startGpsService(true);
        } /*else if (this.count > 0 && count ==0 ) {
            startGpsService(false);
        }*/
        this.count = count;
    }

    private void startGpsService(boolean start) {
        Log.d(TAG, "GpsAppointment - start: " + start);
        Intent intent = new Intent(getApplicationContext(), GpsAppointmentService.class);
        if (start) {
            getApplicationContext().startService(intent);
        } else {
            getApplicationContext().stopService(intent);
        }
    }

}
