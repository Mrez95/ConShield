package com.tek.ConShield;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Tyler Adams on 31/05/2014.
 */
public class NotificationListener extends NotificationListenerService {
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        NotificationListener getService() {
            // Return this instance of LocalService so clients can call public methods
            return NotificationListener.this;
        }
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("NotificationListener", "sbn received");
        String s = ""+sbn.getNotification().tickerText;
        Log.d("NotificationListener", s);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("NotificationListener", "Service is bounded");


        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
