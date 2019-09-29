package com.example.ThiefBoy.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;

import com.example.ThiefBoy.EMail.Var;
import com.example.ThiefBoy.MainActivity;
import com.example.ThiefBoy.Notif.Noti_fication;
import com.example.myapplication.R;

public class for_service extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, Noti_fication.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.thiefboy)
                .setContentTitle("ThiefBoy")
                .setContentText("Location : "+ Var.lattitude+", "+ Var.lontitude)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .build();

        startForeground(1, notification);


        //do heavy work on a background thread
        //stopSelf();

        //*******CLOSE ALL DIALOGUE
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//                sendBroadcast(closeDialog);
//
//                // Close every kind of system dialog
//            }
//        }).start();

        return START_REDELIVER_INTENT;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
