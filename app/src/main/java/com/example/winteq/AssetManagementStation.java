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
import android.widget.GridView;
import android.widget.ImageView;
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

import com.example.winteq.adapter.asset.AdapterAssetStation;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetData;
import com.example.winteq.model.asset.AssetResponseData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetManagementStation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    SearchView searchView;
    Spinner statsp;
    AssetData assetData;
    TextView tv_saveline;

    private GridView gridviewAMS;
    private AdapterAssetStation adapter;
    private List<AssetData> listAsset = new ArrayList<>();

    private String  xLine;;

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
        setContentView(R.layout.activity_asset_management_station);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.asmstation);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        tv_saveline = findViewById(R.id.tv_saveline);

        if(sp.getString(LINE, null) == null) {
            //UNPACK INTENT
            Intent nameSend = getIntent();
            xLine = nameSend.getStringExtra("xLine");
            tv_saveline.setText(xLine);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(LINE, xLine);
            editor.apply();
        }else{
            tv_saveline.setText(sp.getString(LINE, null));
        }

        adapter = new AdapterAssetStation(AssetManagementStation.this, listAsset);
        gridviewAMS = findViewById(R.id.gridviewAMS);
        searchView = findViewById(R.id.SearchAMS);

        statsp = findViewById(R.id.statsp);
        statsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newItems = statsp.getSelectedItem().toString();
                AssetManagementStation.this.adapter.getFilterStation().filter(newItems);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                statsp.setSelection(0);
                String newItems = statsp.getSelectedItem().toString();
                AssetManagementStation.this.adapter.getFilterStation().filter(newItems);
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
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(AssetManagementStation.this, AssetManagement.class);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(LINE, null);
            editor.putString(STATION, null);
            editor.putString(MACHINE, null);
            editor.apply();
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(AssetManagementStation.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(AssetManagementStation.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(AssetManagementStation.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssetManagementStation.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(AssetManagementStation.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(AssetManagementStation.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(AssetManagementStation.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void retrieveData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiAssetStationData(tv_saveline.getText().toString());

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

                listAsset = response.body().getData();

                if(listAsset != null) {
                    adapter = new AdapterAssetStation(AssetManagementStation.this, listAsset);
                    gridviewAMS.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(AssetManagementStation.this, listAsset.get(0).getAsset_line(), Toast.LENGTH_SHORT).show();
                }

//               Toast.makeText(AssetManagementStation.this, listWms.get(0).getElc_tag(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(AssetManagementStation.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listAsset != null) {
                    AssetManagementStation.this.adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

//    private void setSimpleDateFormat(){
//        String strDate = assetData.getDate_notif();
////        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////        try{
////            Date date = dateFormat.parse(strDate);
//        String datetimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(strDate);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, Integer.parseInt(datetimeString));
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//        Toast.makeText(AssetManagementStation.this, datetimeString, Toast.LENGTH_SHORT).show();
//    }
//
//    private void createNotificationChannel(){
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = assetData.getMachine_name();
//            String description = assetData.getAsset_part();
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("notifyme", name, importance);
//        }
//    }

}