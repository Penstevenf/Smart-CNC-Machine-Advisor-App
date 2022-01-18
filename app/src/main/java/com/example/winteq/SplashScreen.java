package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnim;
    ImageView image;
    SharedPreferences sp;
    private static final String DONE = "done?";
    private static final String SHARE_PREF_NAME = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //foreground
//        Intent intent = new Intent(this, NotificationManager.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            startForegroundService(intent);
//        }
//        else
//        {
//            startService(intent);
//        }

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        //reset loading notification
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        sp.edit().putString(DONE, null).apply();

        image = findViewById(R.id.imageView);
        image.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}