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

import com.example.winteq.adapter.help.AdapterHelpMecData;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.help.mekanik.HelpDataMec;
import com.example.winteq.model.help.mekanik.HelpResponseDataMec;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpMech extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    SearchView searchView;
    Api_Interface apiInterface;

    private GridView gv_HelpMec;
    private AdapterHelpMecData adapter;
    private List<HelpDataMec> listHelpMec = new ArrayList<>();

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_mech);

        drawerLayout = findViewById(R.id.hmech);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        gv_HelpMec = findViewById(R.id.gv_Helpmec);
        adapter = new AdapterHelpMecData(HelpMech.this, listHelpMec);
        searchView = findViewById(R.id.search_bar_mec);
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        retrieveDataMec();

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
            Intent intent = new Intent(HelpMech.this,Help.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(HelpMech.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(HelpMech.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HelpMech.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpMech.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(HelpMech.this, Contact.class);
                startActivity(intent3);
                break;

            case R.id.nav_help:
                Intent intent4 = new Intent(HelpMech.this, Help.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveHelpMec(View v){
        Intent intent = new Intent(HelpMech.this, HelpMecAdd.class);
        startActivity(intent);
    }

    public void retrieveDataMec(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpResponseDataMec> showData = aiData.aiHelpMecData();

        showData.enqueue(new Callback<HelpResponseDataMec>() {
            @Override
            public void onResponse(Call<HelpResponseDataMec> call, Response<HelpResponseDataMec> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpMech.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listHelpMec = response.body().getData();
                if(listHelpMec != null) {
                    adapter = new AdapterHelpMecData(HelpMech.this, listHelpMec);
                    gv_HelpMec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HelpResponseDataMec> call, Throwable t) {
                Toast.makeText(HelpMech.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listHelpMec != null) {
                    HelpMech.this.adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }
}