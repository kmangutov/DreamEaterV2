package com.kirillmangutov.dreameater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by kirillmangutov on 7/20/14.
 */
public class AlarmAdmin {

    boolean debug = false;

    public static final int REQUEST_CODE = 1;

    private PendingIntent mAlarmIntent;
    private Context mContext;
    private AlarmManager mAlarmManager;


    public AlarmAdmin(Context ctx) {
        this.mContext = ctx;

        mAlarmManager =
                (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public Calendar getMorning() {
        Calendar c = Calendar.getInstance();

        if(debug) {
            c.add(Calendar.SECOND, 20);
        } else {
            c.set(Calendar.HOUR_OF_DAY, 6);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
        }

        return c;
    }

    public PendingIntent getPendingIntent() {

        Intent intent = new Intent(AlarmReceiver.INTENT_RECORD_DREAM);
        PendingIntent pIntent =
                PendingIntent.getBroadcast(
                        mContext,
                        REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        return pIntent;
    }

    public void setAlarm() {

        if(!debug) {
            mAlarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    getMorning().getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    getPendingIntent()
            );
        } else {
            mAlarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    getMorning().getTimeInMillis(),
                    getPendingIntent()
            );
        }

    }

    public void cancelAlarm() {
        mAlarmManager.cancel(getPendingIntent());
    }
}

