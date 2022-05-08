package com.example.winteq;


import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.swipe_problem_mon);

        Intent activityIntent = getIntent();
        xLine = activityIntent.getStringExtra("xLine");
        xStation = activityIntent.getStringExtra("xStation");
        xMachine = activityIntent.getStringExtra("xMachine");

        Toast.makeText(SwipeProblemMonitoring.this, xLine, Toast.LENGTH_SHORT).show();

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
