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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.adapter.solution.AdapterSolution;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.history.HistoryData;
import com.example.winteq.model.history.HistoryResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechnicalSupport extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;

    FloatingActionButton addsp;
    SearchView sv;
    Spinner line;

    private ListView lvTech;
    private AdapterSolution adapter;
    private List<HistoryData> listHistory = new ArrayList<>();

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_technical_support);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        drawerLayout = findViewById(R.id.techl);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        addsp = findViewById(R.id.addsp);
        lvTech = findViewById(R.id.listviewsp);
        adapter = new AdapterSolution(this);

        sv = findViewById(R.id.searchsp);
        line = findViewById(R.id.catfil);
        line.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newItem = line.getSelectedItem().toString();
                TechnicalSupport.this.adapter.getFilterLine().filter(newItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                line.setSelection(0);
            }
        });

        retrieveData();

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

        addsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TechnicalSupport.this, SupportAdd.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(TechnicalSupport.this, Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(TechnicalSupport.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(TechnicalSupport.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(TechnicalSupport.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TechnicalSupport.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(TechnicalSupport.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(TechnicalSupport.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(TechnicalSupport.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void retrieveData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HistoryResponseData> showData = aiData.aiTechnicalData();

        showData.enqueue(new Callback<HistoryResponseData>() {
            @Override
            public void onResponse(Call<HistoryResponseData> call, Response<HistoryResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

                listHistory = response.body().getData();

                if(listHistory != null) {
                    adapter = new AdapterSolution(TechnicalSupport.this, listHistory);
                    lvTech.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

//               Toast.makeText(WMS.this, listWms.get(0).getElc_tag(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<HistoryResponseData> call, Throwable t) {
                Toast.makeText(TechnicalSupport.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listHistory != null) {
                    TechnicalSupport.this.adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

    }
}