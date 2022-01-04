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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.adapter.help.AdapterHelpElcData;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.help.elektrik.HelpDataElc;
import com.example.winteq.model.help.elektrik.HelpResponseDataElc;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpElec extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    SearchView searchView;
    Api_Interface apiInterface;

    private GridView gv_HelpElc;
    private AdapterHelpElcData adapter;
    private List<HelpDataElc> listHelpElc = new ArrayList<>();

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_elec);

        drawerLayout = findViewById(R.id.helec);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        gv_HelpElc = findViewById(R.id.gv_HelpElc);
        adapter = new AdapterHelpElcData(HelpElec.this, listHelpElc);
        searchView = findViewById(R.id.search_bar_elc);
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        retrieveDataElc();

        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.nav_grafik);
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

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(HelpElec.this,Help.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(HelpElec.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(HelpElec.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HelpElec.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpElec.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(HelpElec.this, Contact.class);
                startActivity(intent3);
                break;

            case R.id.nav_help:
                Intent intent4 = new Intent(HelpElec.this, Help.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveHelpElc(View v){
        Intent intent = new Intent(HelpElec.this, HelpElcAdd.class);
        startActivity(intent);
    }

    public void retrieveDataElc(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpResponseDataElc> showData = aiData.aiHelpElcData();

        showData.enqueue(new Callback<HelpResponseDataElc>() {
            @Override
            public void onResponse(Call<HelpResponseDataElc> call, Response<HelpResponseDataElc> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listHelpElc = response.body().getData();
                if(listHelpElc != null) {
                    adapter = new AdapterHelpElcData(HelpElec.this, listHelpElc);
                    gv_HelpElc.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HelpResponseDataElc> call, Throwable t) {
                Toast.makeText(HelpElec.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listHelpElc != null) {
                    HelpElec.this.adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}