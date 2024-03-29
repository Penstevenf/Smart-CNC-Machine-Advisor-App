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

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class Help extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Button ha, he, hm;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);

        drawerLayout = findViewById(R.id.help);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        ha = findViewById(R.id.ha);
        he = findViewById(R.id.he);
        hm = findViewById(R.id.hm);

        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.nav_help);
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

        ha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this,HelpApp.class);
                startActivity(intent);
            }
        });

        he.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this,HelpElec.class);
                startActivity(intent);
            }
        });

        hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this,HelpMech.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Help.this,Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(Help.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(Help.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Help.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Help.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent4 = new Intent(Help.this, Graph.class);
                startActivity(intent4);
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(Help.this, Contact.class);
                startActivity(intent3);
                break;

            case R.id.nav_help:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}