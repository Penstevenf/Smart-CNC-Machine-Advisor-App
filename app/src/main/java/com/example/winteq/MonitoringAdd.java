package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.monitoring.MonData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    EditText sstation, et_pic, et_desc;
    TextView tv_defaultimagemon;
    Button btn_monimageadd;
    ImageView iv_monadd;
    Spinner sline, sstat, sname;
    FloatingActionButton monadd;
    Api_Interface apiInterface;
    MonData monData;
    Bitmap bitmap;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring_add);

        //get client
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        //Find Variable
        drawerLayout = findViewById(R.id.monadd); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        sline = findViewById(R.id.sline);
        sname = findViewById(R.id.sname);
        sstat = findViewById(R.id.sstat);
        et_pic = findViewById(R.id.et_pic);
        et_desc = findViewById(R.id.et_descmon);
        btn_monimageadd = findViewById(R.id.btn_monimageadd);
        iv_monadd = findViewById(R.id.iv_monadd);
        monadd = findViewById(R.id.monad);
        tv_defaultimagemon = findViewById(R.id.tv_defaultimagemon);

        //Setup Header
        View header = navigationView.getHeaderView(0);
        TextView nama = (TextView) header.findViewById(R.id.fname);

        //Setup Shared Preference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Input Name and Profile to Drawer
        nama.setText(sp.getString(FULLNAME, null));
        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);
        String profileS = sp.getString(IMAGE, null);

        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            pfph.setImageBitmap(bitmap);
        }

        monadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monadd();
            }
        });

        btn_monimageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 0:
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 0 && resultCode == RESULT_OK && data != null){

                    Uri filepath = data.getData();
                    InputStream imageStream;

                    try {
                        imageStream = getContentResolver().openInputStream(filepath);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        this.iv_monadd.setImageBitmap(bitmap);
                        View header = navigationView.getHeaderView(0);
                        tv_defaultimagemon.setText("Preview Image");

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) iv_monadd.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

    private void Monadd() {
        String line = sline.getSelectedItem().toString();
        String station = sname.getSelectedItem().toString();
        String mon_image = getStringImage(bitmap);
//        String mon_status = sstat.getSelectedItem().toString();
        String mon_pic = et_pic.getText().toString();
        String mon_desc = et_desc.getText().toString();
        ProgressDialog pd = new ProgressDialog(MonitoringAdd.this);
        pd.setMessage("Uploading...");
        pd.setCancelable(false);
        pd.show();

        Call<MonData> MonitoringAddcall = apiInterface.aiMonAddData(line, station, mon_image, mon_pic, mon_desc);
        MonitoringAddcall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
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
                    pd.dismiss();

                    Toast.makeText(MonitoringAdd.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MonitoringAdd.this, Monitoring.class);
                    startActivity(intent);
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(MonitoringAdd.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(MonitoringAdd.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Setup onBack Press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(MonitoringAdd.this, Monitoring.class);
            startActivity(intent);
        }
    }

    //Setup Drawer move layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(MonitoringAdd.this, Dashboard.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent intent1 = new Intent(MonitoringAdd.this, Profile.class);
                startActivity(intent1);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean("logged", false).apply();
                Intent intent2 = new Intent(MonitoringAdd.this, Login.class);
                startActivity(intent2);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(MonitoringAdd.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(MonitoringAdd.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(MonitoringAdd.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}