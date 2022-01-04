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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class AssetManagementReplace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    FloatingActionButton fab_send;
    Api_Interface apiInterface;
    TextView tv_asset_part, tv_line, tv_station, tv_machine_qty, tv_machine, tv_replace,
            tv_regis, tv_update, tv_machine_lifetime, tv_machine_category;
    TextView tv_item, tv_copro, tv_type, tv_category, tv_qty, tv_date, tv_tag, tv_lifetime, tv_desc;
    ImageView itempiczg;

    private String  xMachine;

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
        setContentView(R.layout.activity_asset_management_replace);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.amreplace);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //machine part data id
        tv_asset_part = findViewById(R.id.itemnamewzh);
        tv_line = findViewById(R.id.tv7zh);
        tv_station = findViewById(R.id.stationright);
        tv_machine_qty = findViewById(R.id.itemqtyzh);
        tv_machine = findViewById(R.id.itemcoprozh);
        tv_replace = findViewById(R.id.datereplaceto);
        tv_regis = findViewById(R.id.dateregisterto);
        tv_update = findViewById(R.id.lastdatereplace);
        tv_machine_lifetime = findViewById(R.id.itemlifetimeh);
        tv_machine_category = findViewById(R.id.itemcatzh);

        //warehouse part data id
        tv_item = findViewById(R.id.itemnamewzg);
        tv_copro = findViewById(R.id.itemcoprozg);
        tv_type = findViewById(R.id.itemtypeg);
        tv_category = findViewById(R.id.itemcatzg);
        tv_qty = findViewById(R.id.itemqtyzg);
        tv_date = findViewById(R.id.itemdatezg);
        tv_tag = findViewById(R.id.itemtagzg);
        tv_lifetime = findViewById(R.id.itemlifetimeg);
        tv_desc = findViewById(R.id.itemdesczg);
        itempiczg = findViewById(R.id.itempiczg);

        fab_send = findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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


//        if(!(profileS.isEmpty())) {
//            String imageUri = profileS;
//            ImageView Image2 = itempiczg;
//            Picasso.get().load(imageUri).into(Image2);
//        }

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
            Intent intent = new Intent(AssetManagementReplace.this, AssetManagementNotifPart.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(AssetManagementReplace.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(AssetManagementReplace.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(AssetManagementReplace.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssetManagementReplace.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(AssetManagementReplace.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(AssetManagementReplace.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(AssetManagementReplace.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}