package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.user.UserData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText et_username, et_password;
    Button btn_register;
    FloatingActionButton f_btn;
    String Username, Password;
    //apiinterface biar bs login
    Api_Interface apiInterface;
    SessionManager sessionManager;
    public UserData userData;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("Login", MODE_PRIVATE);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        f_btn = findViewById(R.id.f_btn);
        //auto login
        if(sp.getBoolean("Logged", false)){
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);
        }

        f_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = et_username.getText().toString();
                Password = et_password.getText().toString();

                login(Username,Password);
            }
        });

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
                break;
        }
    }

    private void login(String username, String password) {

        Call<UserData> loginDataCall = apiInterface.loginResponse(username,password);
        loginDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(response.body() != null && response.body().isStatus()){

                    //Toast.makeText(getApplicationContext(), username,Toast.LENGTH_SHORT).show();

                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    userData = response.body();

                    //auto login
                    sp.edit().putBoolean("Logged", true).apply();


                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}