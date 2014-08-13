package com.kirillmangutov.dreameater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by kirillmangutov on 7/20/14.
 */
public class AlarmAdmin {

    public static final int REQUEST_CODE = 1;

    private PendingIntent alarmIntent;
    private Context context;
    private AlarmManager alarmManager;

    public AlarmAdmin(Context ctx) {
        this.context = ctx;

        alarmManager =
                (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public Calendar getMorning() {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c;
    }

    public PendingIntent getPendingIntent() {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent =
                PendingIntent.getBroadcast(
                        context,
                        REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        return pIntent;
    }

    public void setAlarm() {

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                getMorning().getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                getPendingIntent()
        );
    }

    public void cancelAlarm() {

        alarmManager.cancel(getPendingIntent());
    }
}

