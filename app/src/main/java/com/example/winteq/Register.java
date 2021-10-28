package com.example.winteq;

import android.content.Intent;
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

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText et_fullname, et_username, et_password, et_email, et_npk;
    Button btn_login;
    FloatingActionButton f_btn;
    String Fullname, Username, Password, Email, NPK;
    Api_Interface apiInterface;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        et_username = findViewById(R.id.et_Registerusername);
        et_password = findViewById(R.id.et_Registerpassword);
        et_fullname = findViewById(R.id.et_Registerfullname);
        et_email = findViewById(R.id.et_Registeremail);
        et_npk = findViewById(R.id.et_Registernpk);
        btn_login = findViewById(R.id.btn_Registerlogin);
        btn_login.setOnClickListener(this);
        f_btn = findViewById(R.id.f_Registerbtn);
        f_btn.setOnClickListener(this);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.f_Registerbtn:
                NPK = et_npk.getText().toString();
                Username = et_username.getText().toString();
                Password = et_password.getText().toString();
                Fullname = et_fullname.getText().toString();
                Email = et_email.getText().toString();
                register(NPK, Username, Password, Fullname, Email);
                break;
            case R.id.btn_Registerlogin:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void register(String npk, String username, String password, String fullname, String email) {
        Call<UserData> call = apiInterface.registerResponse(npk, username,password,fullname,email);
        call.enqueue(new Callback<UserData>() {
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

                    userData = response.body();

                    Toast.makeText(Register.this, sr, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Register.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Register.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}