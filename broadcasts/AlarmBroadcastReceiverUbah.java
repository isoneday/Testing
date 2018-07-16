package com.teamproject.plastikproject.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.activities.PurchaseActivity;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.helpers.ShoppingContentProvider;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.modelupdateskedule.Responseupdateskdle;
import com.teamproject.plastikproject.plastik.helper.SessionManager;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.teamproject.plastikproject.fragments.PurchaseEditFragmentbaru.SHARED_PREFERENCES_FILE_USER_INFO_LIST1;

/**
 * Created by rage on 4/3/15.
 */
public class AlarmBroadcastReceiverUbah extends BroadcastReceiver {
    public static String EXTRA_PERSON = "extra_person";
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            SessionManager manager =new SessionManager(context);
            int id = Integer.parseInt(manager.getIdincre());
            Long dbId = intent.getExtras().getLong(AppConstants.EXTRA_LIST_ID, -1);
            if (id > 0) {
                //showNotification(context, dbId);
                Uri uri = Uri.parse(ShoppingContentProvider.PURCHASE_LIST_CONTENT_URI + "/" + dbId);
                String[] projection = {
                        SqlDbHelper.COLUMN_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_LIST_NAME,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_USER_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_SHOP_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_SHOP,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_PLACE_ID,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_USER_PLACE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_DONE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_IS_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_ALARM,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIME_CREATE,
                        SqlDbHelper.PURCHASE_LIST_COLUMN_TIMESTAMP,
                };
                Cursor cursor = context.getContentResolver().query(
                        uri,
                        projection,
                        null,
                        null,
                        null
                );
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_USER_INFO_LIST1, MODE_PRIVATE);

//                String json = sharedPreferences.getString("datask", "");
//                Responseupdateskdle list = intent.getParcelableExtra(EXTRA_PERSON);


                //Intent intent1 = intent;
                Bundle args = intent.getBundleExtra("DATA");
                Responseupdateskdle list =intent.getParcelableExtra(AppConstants.EXTRA_LIST_ID2);


              //  Responseupdateskdle list = gson.fromJson(json, Responseupdateskdle.class);
              //  Responseupdateskdle list = gson.fromJson(json, Responseupdateskdle.class);
                //  PurchaseListModelbar list = gson.fromJson(json, PurchaseListModelbar.class);
                // PurchaseListModelbar list = ContentHelper.getPurchaseList(cursor);
           //  String list = "hehe";
                showNotification(context, id);

                mediaPlayer = MediaPlayer.create(context,R.raw.ringtone);
                mediaPlayer.start();
                   }
        }
    }


    private void showNotification(Context context, int list) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setLargeIcon(bitmap)
                        .setContentTitle("today")
                        .setContentText(context.getString(R.string.notification_alarm_description))
                        //.setContentInfo("info")
                        //.setTicker("ticker")
                ;
        Intent startIntent = new Intent(context, PurchaseActivity.class);
        startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, list);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(
                        context,
                        new Random().nextInt(),
                        startIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
    }

}
