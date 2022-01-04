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

import com.example.winteq.adapter.asset.AdapterAssetView;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AssetManagementNotifPart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    SearchView search_assetz;
    Spinner asset_categoryz;
    TextView machine_name;

    private ListView listAssetViewz;
    private AdapterAssetView adapter;
    private List<AssetData> listAsset = new ArrayList<>();

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
        setContentView(R.layout.activity_asset_management_notif_part);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.amnp);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        machine_name = findViewById(R.id.machine_namez);

//        if(sp.getString(MACHINE, null) == null) {
//            //UNPACK INTENT
//            Intent nameSendm = getIntent();
//            xMachine = nameSendm.getStringExtra("xMachine");
//            machine_name.setText(xMachine);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString(MACHINE, machine_name.getText().toString());
//            editor.apply();
//        }else{
//            machine_name.setText(sp.getString(MACHINE, null));
//        }
//        tv_saveliness = findViewById(R.id.tv_saveliness);
//        tv_saveliness.setText(sp.getString(LINE, null));
//        tv_savestations = findViewById(R.id.tv_savestations);
//        tv_savestations.setText(sp.getString(STATION, null));

        listAssetViewz = findViewById(R.id.listAssetViewz);
//        adapter = new AdapterAssetView(this);

        search_assetz = findViewById(R.id.search_assetz);
        asset_categoryz = findViewById(R.id.asset_categoryz);


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

//        asset_categoryz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String newItem = asset_category.getSelectedItem().toString();
//                AssetManagementNotifPart.this.adapter.getFilterCategory().filter(newItem);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(AssetManagementNotifPart.this, AssetManagementMachine.class);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(MACHINE, null);
            editor.apply();
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(AssetManagementNotifPart.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(AssetManagementNotifPart.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(AssetManagementNotifPart.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssetManagementNotifPart.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(AssetManagementNotifPart.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(AssetManagementNotifPart.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(AssetManagementNotifPart.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}