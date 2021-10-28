package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    Button btn_forgotlogin;
    EditText et_forgotemail;
    FloatingActionButton f_forgotbtn;
    Api_Interface apiInterface;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inisialisasi view
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        et_forgotemail = findViewById(R.id.et_forgotemail);
        btn_forgotlogin = findViewById(R.id.btn_forgotlogin);
        btn_forgotlogin.setOnClickListener(this);

        f_forgotbtn = findViewById(R.id.f_forgotbtn);
        f_forgotbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_forgotemail.getText().toString();
                forgot(email);
            }
        });

    }

    private void forgot(String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<UserData> ForgotDataCall = apiInterface.forgotResponse(email);
        ForgotDataCall.enqueue(new Callback<UserData>() {

            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                progressDialog.dismiss();
                //sr untuk menampung array message dalam bentuk string
                //loop isi data dari array message lalu di append ke dalam string sr
                //if else untuk mencegah mengambil value awal dari string sr ("")
                String sr = "";
                for(int i=0 ; i<response.body().getMessage().length ; i++) {
                    if (sr.length() == 0) {
                        sr = response.body().getMessage()[i];
                    } else {
                        sr = sr + "\n" + response.body().getMessage()[i];
                    }
                }
                if (response.body() != null && response.body().isStatus()) {
                    Toast.makeText(ForgotPassword.this, sr, Toast.LENGTH_SHORT).show();
                    userData = response.body();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ForgotPassword.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forgotlogin:
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                break;
        }
    }
}



