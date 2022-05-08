package com.example.winteq;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;


public class SwipeProblemMonitoring extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    String Response_Time_Start;
    private String xLine, xStation, xMachine;

    SharedPreferences sp;
    private static final String SHARE_PREF_NAME = "mypref";
    public static final String NOTIFICATION_NO = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.swipe_problem_mon);

        //Setup Shared Preference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        if(sp.getString(NOTIFICATION_NO, null) == "1"){
            Intent activityIntent1 = getIntent();
            xLine = activityIntent1.getStringExtra("xLine");
            xStation = activityIntent1.getStringExtra("xStation");
            xMachine = activityIntent1.getStringExtra("xMachine");
        }

        if(sp.getString(NOTIFICATION_NO, null) == "2") {
            Intent activityIntent2 = getIntent();
            xLine = activityIntent2.getStringExtra("xLine");
            xStation = activityIntent2.getStringExtra("xStation");
            xMachine = activityIntent2.getStringExtra("xMachine");
        }

        if(sp.getString(NOTIFICATION_NO, null) == "3") {
            Intent activityIntent3 = getIntent();
            xLine = activityIntent3.getStringExtra("xLine");
            xStation = activityIntent3.getStringExtra("xStation");
            xMachine = activityIntent3.getStringExtra("xMachine");
        }

        if(sp.getString(NOTIFICATION_NO, null) == "4") {
            Intent activityIntent4 = getIntent();
            xLine = activityIntent4.getStringExtra("xLine");
            xStation = activityIntent4.getStringExtra("xStation");
            xMachine = activityIntent4.getStringExtra("xMachine");
        }

//        Toast.makeText(SwipeProblemMonitoring.this, xLine, Toast.LENGTH_SHORT).show();

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String value = extras.getString("key");
//            //The key argument here must match that used in the other activity
//        }

        Response_Time_Start = getIntent().getStringExtra("Response_Time_Start");
        SwipeButton enableButton = findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
            }
        });

        enableButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {

                Intent i = new Intent(getApplicationContext(), Sensor.class);
                i.putExtra("xLine", xLine);
                i.putExtra("xStation", xStation);
                i.putExtra("xMachine", xMachine);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

}
