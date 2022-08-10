package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnim;
    ImageView image, image2;
    TextView judl;
    SharedPreferences sp;
    private static final String DONE = "done?";
    private static final String SHARE_PREF_NAME = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        //reset loading notification
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        sp.edit().putString(DONE, null).apply();

        image = findViewById(R.id.imageView);
        judl = findViewById(R.id.judl);
//        image2 = findViewById(R.id.imageView2);
//        image.setAnimation(topAnim);
        judl.setAnimation(topAnim);
//        image2.setAnimation(topAnim);

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