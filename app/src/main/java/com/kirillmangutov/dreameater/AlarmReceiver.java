package com.kirillmangutov.dreameater;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;

/**
 * Created by kirillmangutov on 7/20/14.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_MORN = 0;

    public static final String INTENT_RECORD_DREAM = "com.kirillmangutov.dreameater.RECORD_DREAM";
    public static final IntentFilter INTENT_FILTER = new IntentFilter(INTENT_RECORD_DREAM);

    @Override
    public void onReceive(Context ctx, Intent intent) {
        installNotification(ctx);
    }

    protected void installNotification(Context ctx) {
        Intent notifyIntent = new Intent(ctx, ListActivity.class);
        notifyIntent.putExtra("TODAY", true);

        PendingIntent futureIntent =
                PendingIntent.getActivity(ctx, 0, notifyIntent,  0);

        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Dream Eater")
                .setContentText("Log your dream")
                .setAutoCancel(true)
                .setContentIntent(futureIntent);

        NotificationManager manager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_MORN, builder.build());
    }
}
