package com.teamproject.plastikproject.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.teamproject.plastikproject.broadcasts.AlarmBroadcastReceiver;
import com.teamproject.plastikproject.helpers.AppConstants;
import com.teamproject.plastikproject.model.PurchaseListModelbar;
import com.teamproject.plastikproject.modelupdateskedule.Responseupdateskdle;

/**
 * Created by rage on 4/4/15.
 */
public class AlarmUtils {
    private Context context;

    public AlarmUtils(Context context) {
        this.context = context;
    }

    public void setListAlarm(PurchaseListModelbar list) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getIdUser());
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                list.getTimeAlarm(),
                pendingIntent
        );
    }
    public void setListAlarm2(PurchaseListModelbar list, Responseupdateskdle dataskdle) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getIdUser());
        intent.putExtra(AppConstants.EXTRA_LIST_ID2,  (Parcelable) dataskdle);
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                list.getTimeAlarm(),
                pendingIntent
        );
    }
//    public void setListAlarmske(Responseupdateskdle list) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmBroadcastReceiverUbah.class);
//        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getIdUser());
//        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
//        intent.setData(data);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context,
//                0,
//                intent,
//                PendingIntent.FLAG_CANCEL_CURRENT
//        );
//        alarmManager.set(
//                AlarmManager.RTC_WAKEUP,
//                list.getTimeAlarm(),
//                pendingIntent
//        );
//    }

    public void cancelListAlarm(PurchaseListModelbar list) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(AppConstants.EXTRA_LIST_ID, list.getIdUser());
        Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(data);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        alarmManager.cancel(pendingIntent);
    }
}
