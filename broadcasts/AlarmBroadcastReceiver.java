package com.teamproject.plastikproject.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.WritePurchaseListService;
import com.teamproject.plastikproject.activities.PurchaseActivity;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.helpers.ShoppingContentProvider;
import com.teamproject.plastikproject.helpers.SqlDbHelper;
import com.teamproject.plastikproject.plastik.helper.SessionManager;

import java.util.Random;

/**
 * Created by rage on 4/3/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            SessionManager manager =new SessionManager(context);
            int id = Integer.parseInt(manager.getIdincre());
            Log.i("Service Stops", "Ohhhhhhh");
            context.startService(new Intent(context, WritePurchaseListService.class));;
            Long dbId = intent.getExtras().getLong(AppConstants.EXTRA_LIST_ID, -1);
            if (dbId > 0) {
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
                mediaPlayer = MediaPlayer.create(context,R.raw.ringtone);
                mediaPlayer.start();
                String list = "today";
//                PurchaseListModelbar list = ContentHelper.getPurchaseList(cursor);
                showNotification(context, list);
            }
        }
    }

    private void showNotification(Context context, String list) {
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
      //  startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, list.getIdUser());
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
//    private void showNotification(Context context, PurchaseListModelbar list) {
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.ic_notification_icon)
//                        .setLargeIcon(bitmap)
//                        .setContentTitle(list.getDay())
//                        .setContentText(context.getString(R.string.notification_alarm_description))
//                        //.setContentInfo("info")
//                        //.setTicker("ticker")
//                ;
//        Intent startIntent = new Intent(context, PurchaseActivity.class);
//        startIntent.putExtra(AppConstants.NOTIFICATION_LIST_ARGS, list.getIdUser());
//        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent
//                .getActivity(
//                        context,
//                        new Random().nextInt(),
//                        startIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        notification.defaults = Notification.DEFAULT_ALL;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification);
//    }

}
