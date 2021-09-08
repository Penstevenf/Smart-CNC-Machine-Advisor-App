package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winteq.model.login.LoginData;

public class Dashboard extends AppCompatActivity {
    LoginData loginData;
    Button btn_logout;
    TextView et_username, et_fullname;
    SessionManager sessionManager;
    String username, fullname;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);

        /*sessionManager = new SessionManager( Dashboard.this);
        if(!sessionManager.isLoggedIn()){
            moveToLogin();
        }*/

        //code textview

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        et_username = findViewById(R.id.textView);
        et_fullname = findViewById(R.id.textView2);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putBoolean("Logged", false).apply();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
            }
        });

//        username = sessionManager.getUserDetail().get("username");
//        fullname = sessionManager.getUserDetail().get("fullname")
        loginData = new LoginData();
        username = loginData.getUserId();

        Toast.makeText(getApplicationContext(),username,Toast.LENGTH_SHORT).show();
        et_username.setText(username);
        et_fullname.setText(fullname);

    }

//    private void moveToLogin() {
//        Intent intent = new Intent(Dashboard.this, Login.class);
//        //buat data di dashboard kosong
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//        finish();
//    }
}