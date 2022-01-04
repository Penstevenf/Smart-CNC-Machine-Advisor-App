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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.model.user.UserData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    UserData userData;
    Button btn_logout;
    TextView et_username, et_fullname;
    String username, fullname, npk, email;
    SharedPreferences sp;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView profile, stats;
    Button ARCU,Mon,AstM,WMS,Ass,Tech,Sol,His;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String ID = "id";
    private static final String NPK = "npk";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String TELP = "no_telp";
    private static final String STAT = "stat";
    private static final String IMAGE = "image";
    private static final String LINE = "asset_line";
    private static final String STATION = "asset_station";
    private static final String MACHINE = "machine_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);


        drawerLayout = findViewById(R.id.dash);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        ARCU = findViewById(R.id.ARCU);
        Mon = findViewById(R.id.Monitoring);
        AstM = findViewById(R.id.AssetM);
        WMS = findViewById(R.id.WMS);
        Ass = findViewById(R.id.Assistance);
        Tech = findViewById(R.id.TechSP);
        Sol = findViewById(R.id.Solution);
        His = findViewById(R.id.History);
        stats = findViewById(R.id.stat);
        profile = findViewById(R.id.profile);

        View header = navigationView.getHeaderView(0);

        et_username = (TextView) findViewById(R.id.Name);
        et_fullname = (TextView) header.findViewById(R.id.fname);

        //code textview
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        npk = sp.getString(NPK, null);
        username = sp.getString(USERNAME, null);
        fullname = sp.getString(FULLNAME, null);
        email = sp.getString(EMAIL, null);
        sp.getString(ID, null);
        sp.getString(TELP, null);

        //Set Sharedpreference asset Management data to Null
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LINE, null);
        editor.putString(STATION, null);
        editor.putString(MACHINE, null);
        editor.apply();


//        Toast.makeText(getApplicationContext(), fullname,Toast.LENGTH_SHORT).show();
        et_username.setText(username + " - " + npk + " - " + email);
        et_fullname.setText(fullname);


        navigationView.bringToFront();

        setSupportActionBar(toolbar);

        navigationView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image1 = (ImageView) findViewById(R.id.profile);
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image1);
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            profile.setImageBitmap(bitmap);
            pfph.setImageBitmap(bitmap);
        }


        ARCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,CheckUp.class);
                startActivity(intent);
            }
        });

        Mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Monitoring.class);
                startActivity(intent);
            }
        });

        AstM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,AssetManagement.class);
                startActivity(intent);
            }
        });

        WMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,WMS.class);
                startActivity(intent);
            }
        });

        Ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Assistance.class);
                startActivity(intent);
            }
        });

        Tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,TechnicalSupport.class);
                startActivity(intent);
            }
        });

        Sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Solution.class);
                startActivity(intent);
            }
        });

        His.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,History.class);
                startActivity(intent);
            }
        });

        String sta = sp.getString(STAT, null);

        if (sta.equals("1")){
            stats.setImageResource(R.drawable.rectangle_green);
        }
        else{
            stats.setImageResource(R.drawable.rectangle_red);
        }

        return;

    }

    //run di background loop
//    private class onBackgroound extends AsyncTask<String, String, String>{
//
//    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Dashboard.this, Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Dashboard.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent intent1 = new Intent(Dashboard.this, Profile.class);
                startActivity(intent1);
                break;

            case R.id.nav_grafik:
                Intent intent2 = new Intent(Dashboard.this, Graph.class);
                startActivity(intent2);
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(Dashboard.this, Contact.class);
                startActivity(intent3);
                break;


            case R.id.nav_help:
                Intent intent4 = new Intent(Dashboard.this, Help.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}