package com.kirillmangutov.dreameater;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by kirillmangutov on 7/20/14.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_MORN = 0;

    public void onReceive(Context ctx, Intent intent) {

    }

    public void installNotification(Context ctx) {

        Intent notifyIntent = new Intent(ctx, WriteActivity.class);
        PendingIntent futureIntent =
                PendingIntent.getActivity(ctx, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(ctx)
                .setContentTitle("Dream Eater")
                .setContentText("Log your dream")
                .setAutoCancel(true)
                .setContentIntent(futureIntent);

        NotificationManager manager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_MORN, builder.build());
    }
}
