package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    Button btn_register, btn_forgot;
    FloatingActionButton f_btn;
    String Username, Password;
    //apiinterface biar bs login
    Api_Interface apiInterface;
    UserData userData;
    SharedPreferences sp;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String ID = "id";
    private static final String NPK = "npk";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String TELP = "no_telp";
    private static final String STAT = "stat";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);



        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_forgot = findViewById(R.id.btn_Forgot);
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        f_btn = findViewById(R.id.f_btn);

//        auto login
        if(sp.getBoolean(SHARE_PREF_NAME, false)){
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
                //sr untuk menampung array message dalam bentuk string
                //loop isi data dari array message lalu di append ke dalam string sr
                //if else untuk mencegah mengambil value awal dari string sr ("")
                String sr = "";
                for(int i=0 ; i<response.body().getMessage().length ; i++){
                    if(sr.length() == 0){
                        sr = response.body().getMessage()[i];
                    }else{
                        sr = sr + "\n" + response.body().getMessage()[i];
                    }
                }
                if(response.body() != null && response.body().isStatus()){

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(ID,response.body().getId());
                    editor.putString(NPK,response.body().getNpk());
                    editor.putString(USERNAME,response.body().getUsername());
                    editor.putString(FULLNAME,response.body().getFullname());
                    editor.putString(EMAIL,response.body().getEmail());
                    editor.putString(TELP,response.body().getNo_telp());
                    editor.putString(STAT,response.body().getStat());
                    editor.putString(IMAGE,response.body().getImage());
                    editor.apply();

//                   Toast.makeText(getApplicationContext(), sp.getString(ID, null),Toast.LENGTH_SHORT).show();

                    Toast.makeText(Login.this, sr, Toast.LENGTH_SHORT).show();
                    userData = response.body();

//                    auto login
                    sp.edit().putBoolean(SHARE_PREF_NAME, true).apply();


                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}