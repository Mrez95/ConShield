package com.tek.ConShield;

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;


    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    private final long startTime = 10 * 1000;
    private final long interval = 1000;
    public TextView text;
    public AlertDialog alertDialog;
    public NotificationManager notificationManager;
    public Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tek.ConShield.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);

        // kelvin
        alertDialog = new AlertDialog.Builder(this).create();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.ic_launcher, "New Message", System.currentTimeMillis());
        if (Context.NOTIFICATION_SERVICE!=null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(0);
        }
        setContentView(R.layout.loaded);

    }

    public void startTiming() {
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
            txtView.setText(temp);
        }
    }

    public void openGoogleMap(View view) {
        // String uri = "http://maps.google.com/maps?saddr=" + "9982878" + "," + "76285774" + "&daddr=" + "9992084" + "," + "76286455";
        String uri = "http://maps.google.com/maps?saddr=" + "101 College Street" + "&daddr=" + "40 Brookshire Cir";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public void activation(View view) {
        setContentView(R.layout.core);
        startB = (Button) this.findViewById(R.id.active);
        text = (TextView) this.findViewById(R.id.timer);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(30));
        startTiming();
    }

    public void Notify(String notificationTitle, String notificationMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher,
                "New Message", System.currentTimeMillis());

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(MainActivity.this, notificationTitle,
                notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);

        }

        @Override
        public void onFinish() {
            text.setText("Time's up!");
            alertDialog.setTitle("The Time had ran out.");
            alertDialog.setMessage("Do you want to extend the time? Note:if you do not response " +
                    "in 30 seconds, your message will be send to the given emergency contact.");
            alertDialog.setButton("Extend.", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.show();
            Notify("Title: Alert",
                    "Msg:Running out of time.");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }


    }
}