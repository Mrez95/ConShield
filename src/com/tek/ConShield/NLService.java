package com.tek.ConShield;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    Every time a notification is posted, destination and timeMinutes will be updated.

 */

public class NLService extends NotificationListenerService {
    private String destination="";
    private long timeMinutes=0;


    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;

    public String getDestination() {
        return destination;
    }

    public long getTimeMinutes() {
        return timeMinutes;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tek.conshield.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        /*
       // doesnt work
        RemoteViews remoteView = sbn.getNotification().contentView;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup localView = (ViewGroup) inflater.inflate(remoteView.getLayoutId(), null);
        remoteView.reapply(getApplicationContext(), localView);
        TextView tv = (TextView) localView.findViewById(16908358);
        Log.d("NLService", ""+tv.getText());
*/

  /*      Notification notification = sbn.getNotification();
        RemoteViews views = notification.contentView;
        Class secretClass = views.getClass();

        try {
            Map<Integer, String> text = new HashMap<Integer, String>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions")) continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
                        .get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    Integer type = null;
                    Integer viewId = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        } else if (field.getName().equals("viewId")) {
                            viewId = field.getInt(action);
                        }
                    }

                    if (type == 9 || type == 10) {
                        text.put(viewId, value.toString());
                    }
                }

               Log.d("NLService", "title is: " + text.get(16908310));
                Log.d("NLService","info is: " + text.get(16909082));
                Log.d("NLService", "text is: " + text.get(16908358));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/



        Log.i(TAG,"**********  onNotificationPosted");
        //  Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
//        Log.i(TAG,sbn.toString() + "\t" + sbn.getPackageName());
        String s = ""+getText(sbn.getNotification());
        String timeUntilDest;
        String startTime;
        String endTime;
        // pull out the time, destination and store it
        if(s!=null) {
            // this is a Google Maps notification
            if (s.contains("to destination")) {

                Log.i(TAG,"getText: "+getText(sbn.getNotification()));
                // take all string information before comma
                int firstComma = s.indexOf(",");
                // need to truncate the [ so i'm taking at index 1 not index 0
                destination=s.substring(1,firstComma);

                Log.i("NLService", "destinationText: "+destination);


                // one space after the first comma until second comma

                // firstComma
                int secondComma=s.indexOf(",", firstComma+1);
                startTime=s.substring(firstComma+1,secondComma);
                endTime=s.substring(s.length()-(1+8),s.length()-1);
                Log.i("NLService", "startTime: "+startTime+"  endTime:"+endTime);

                //perform string subtraction endTime-startTime
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

                try {
                    Date date1 = sdf.parse(startTime);
                    Date date2 = sdf.parse(endTime);
                    Log.i("NLService", "date1: "+date1+"  date2: "+date2);
                    // this can be changed to minutes/seconds/etc
                    long differenceInMillis= date2.getTime() - date1.getTime();
                    // divide ms by 1000 to get s, divide by 60 to get m

                    timeMinutes = (differenceInMillis/1000L)/(60L);
                    Log.i("NLService", "differenceInMinutes: "+(timeMinutes));

                }
                catch(ParseException e){

                }

            }
        }
        Intent i = new  Intent("com.tek.conshield.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        Intent i = new  Intent("com.tek.conshield.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.tek.conshield.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.tek.conshield.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.tek.conshield.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }


    //http://stackoverflow.com/questions/9292032/extract-notification-text-from-parcelable-contentview-or-contentintent
    // MAGIC to get the text from the Notification
    // specifically, this gets the time and destination name/address
    public static List<String> getText(Notification notification)
    {
        // We have to extract the information from the view
        RemoteViews        views = notification.bigContentView;
        if (views == null) views = notification.contentView;
        if (views == null) return null;

        // Use reflection to examine the m_actions member of the given RemoteViews object.
        // It's not pretty, but it works.
        List<String> text = new ArrayList<String>();
        try
        {
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);

            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions)
            {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);

                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
                if (tag != 2) continue;

                // View ID
                parcel.readInt();

                String methodName = parcel.readString();
                if (methodName == null) continue;

                    // Save strings
                else if (methodName.equals("setText"))
                {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();

                    // Store the actual string
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    //  Log.i("NLService", "setText: "+t);
                    text.add(t);
                }

                // Save times. Comment this section out if the notification time isn't important
                else if (methodName.equals("setTime"))
                {
                    // Parameter type (5 = Long)
                    parcel.readInt();

                    String t = new SimpleDateFormat("h:mm a").format(new Date(parcel.readLong()));
                    text.add(t);
                }

                parcel.recycle();
            }
        }

        // It's not usually good style to do this, but then again, neither is the use of reflection...
        catch (Exception e)
        {
            Log.e("NotificationClassifier", e.toString());
        }

        return text;
    }

}