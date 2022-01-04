package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.adapter.monitoring.AdapterMonitoring1;
import com.example.winteq.adapter.monitoring.AdapterMonitoring2;
import com.example.winteq.adapter.monitoring.AdapterMonitoring3;
import com.example.winteq.adapter.monitoring.AdapterMonitoring4;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.monitoring.MonResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Monitoring extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    FloatingActionButton addmon;
    Api_Interface apiInterface;

    private GridView grid1, grid2, grid3, grid4;
    private AdapterMonitoring1 adapter1;
    private AdapterMonitoring2 adapter2;
    private AdapterMonitoring3 adapter3;
    private AdapterMonitoring4 adapter4;
    private List<MonData> listMon1 = new ArrayList<>();
    private List<MonData> listMon2 = new ArrayList<>();
    private List<MonData> listMon3 = new ArrayList<>();
    private List<MonData> listMon4 = new ArrayList<>();

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.mon);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        addmon = findViewById(R.id.AddMon);

        grid1 = findViewById(R.id.ll1);
        grid2 = findViewById(R.id.ll2);
        grid3 = findViewById(R.id.ll3);
        grid4 = findViewById(R.id.ll4);

        adapter1 = new AdapterMonitoring1(Monitoring.this, listMon1);
        adapter2 = new AdapterMonitoring2(Monitoring.this, listMon2);
        adapter3 = new AdapterMonitoring3(Monitoring.this, listMon3);
        adapter4 = new AdapterMonitoring4(Monitoring.this, listMon4);

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

        //Move to Another Layout
        addmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Monitoring.this, MonitoringAdd.class);
                startActivity(intent);
            }
        });

        gridview1();
        gridview2();
        gridview3();
        gridview4();

        mRepeatHandler = new Handler();
        mRepeatRunnable = new Runnable() {
            @Override
            public void run() {
                gridview1();
                gridview2();
                gridview3();
                gridview4();
                mRepeatHandler.postDelayed(mRepeatRunnable, 20000);
            }
        };
        mRepeatHandler.postDelayed(mRepeatRunnable,20000);
    }


    //call data for gridview
    public void gridview1(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon1Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon1 = response.body().getData();
                if(listMon1 != null) {
                    adapter1 = new AdapterMonitoring1(Monitoring.this, listMon1);
                    grid1.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview2(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon2Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon2 = response.body().getData();
                if(listMon2 != null) {
                    adapter2 = new AdapterMonitoring2(Monitoring.this, listMon2);
                    grid2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview3(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon3Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon3 = response.body().getData();
                if(listMon3 != null) {
                    adapter3 = new AdapterMonitoring3(Monitoring.this, listMon3);
                    grid3.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview4(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon4Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon4 = response.body().getData();
                if(listMon4 != null) {
                    adapter4 = new AdapterMonitoring4(Monitoring.this, listMon4);
                    grid4.setAdapter(adapter4);
                    adapter4.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Setup onBack Press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Monitoring.this, Dashboard.class);
            startActivity(intent);
        }
    }

    //Setup Drawer move layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(Monitoring.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(Monitoring.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Monitoring.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Monitoring.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(Monitoring.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(Monitoring.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(Monitoring.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}