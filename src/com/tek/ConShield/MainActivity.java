package com.tek.ConShield;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.R.layout;
        import android.graphics.Color;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.app.Activity;
        import android.util.Log;
        import android.view.Menu;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private boolean isTrackee = false;
    private boolean isTracker = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons

        builder.setTitle("Who do you want to be?");

        builder.setPositiveButton(R.string.trackee, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isTrackee=true;
                isTracker=false;

                sendData(isTrackee, isTracker);
            }
        });

        builder.setNegativeButton(R.string.tracker, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                isTracker=true;
                isTrackee=false;

                sendData(isTrackee, isTracker);
            }
        });


        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void sendData(boolean trackee, boolean tracker) {

        if(tracker) {
            //Log.d("ConShield", "tracker");

        }
        else if(trackee){
            //Log.d("ConShield", "trackee");

        }
        else{
            // neither were picked somehow
        }

    }
}
