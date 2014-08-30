package com.kirillmangutov.dreameater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

    private static AlarmAdmin instance = null;

    public static AlarmAdmin getInstance(Context ctx) {
        if(instance == null) {
            instance = new AlarmAdmin(ctx);
        }
        return instance;
    }

    protected AlarmAdmin(Context ctx) {
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
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
            Log.d("ALARM", "Date: " + c.get(Calendar.DATE));
        }

        return c;
    }

    public PendingIntent getPendingIntent(int flag) {

        Intent intent = new Intent(AlarmReceiver.INTENT_RECORD_DREAM);
        PendingIntent pIntent =
                PendingIntent.getBroadcast(
                        mContext,
                        REQUEST_CODE,
                        intent,
                        flag);

        return pIntent;
    }

    public void setAlarm() {

        if(!debug) {
            mAlarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    getMorning().getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT)
            );
        } else {
            mAlarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    getMorning().getTimeInMillis(),
                    getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT)
            );
        }
    }

    public boolean existsAlarm() {
        return getPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT) != null;
    }

    public void cancelAlarm() {
        mAlarmManager.cancel(getPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT));
    }
}

