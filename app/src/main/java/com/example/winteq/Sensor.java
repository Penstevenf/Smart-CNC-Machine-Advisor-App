package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.adapter.sensor.AdapterSensor;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.sensor.SensorData;
import com.example.winteq.model.sensor.SensorResponseData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sensor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    SearchView search_assetz;
    TextView machine_name, sen_line, sen_station;

    private ListView listAssetViewz;
    private AdapterSensor adapter;
    private List<SensorData> listSensor = new ArrayList<>();

    private String xMachine, xLine, xStation;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";
    private static final String LINE = "asset_line";
    private static final String STATION = "asset_station";
    private static final String MACHINE = "machine_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sensor);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.amnp);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        xLine = i.getStringExtra("xLine");
        xStation = i.getStringExtra("xStation");
        xMachine = i.getStringExtra("xMachine");

        listAssetViewz = findViewById(R.id.listAssetViewz);
        adapter = new AdapterSensor(this);

        search_assetz = findViewById(R.id.search_sensor);
        machine_name = findViewById(R.id.machine_namez);
        sen_line = findViewById(R.id.sen_line);
        sen_station = findViewById(R.id.sen_station);

        sen_line.setText(xLine);
        sen_station.setText(xStation);
        machine_name.setText(xMachine);

        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

        retrieveData();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Sensor.this, Monitoring.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(Sensor.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(Sensor.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Sensor.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Sensor.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(Sensor.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(Sensor.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(Sensor.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void retrieveData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<SensorResponseData> showData = aiData.aiSensorData(xLine, xStation, xMachine);

        showData.enqueue(new Callback<SensorResponseData>() {
            @Override
            public void onResponse(Call<SensorResponseData> call, Response<SensorResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

                listSensor = response.body().getData();

                if(listSensor != null) {
                    adapter = new AdapterSensor(Sensor.this, listSensor);
                    listAssetViewz.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

//               Toast.makeText(AssetManagement.this, listWms.get(0).getElc_tag(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<SensorResponseData> call, Throwable t) {
                Toast.makeText(Sensor.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });

        search_assetz.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listSensor != null) {
                    Sensor.this.adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}