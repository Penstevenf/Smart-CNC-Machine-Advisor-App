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

import com.example.winteq.adapter.asset.AdapterAssetMachine;
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

public class AssetManagementMachine extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    SearchView searchView;
    Spinner statsp;
    AssetData assetData;
    TextView tv_savestation, tv_savelines;

    private GridView gridviewAMSM;
    private AdapterAssetMachine adapter;
    private List<AssetData> listAsset = new ArrayList<>();

    private String  xStation;;

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
        setContentView(R.layout.activity_asset_management_machine);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.asmMachine);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        tv_savestation = findViewById(R.id.tv_savestation);

        if(sp.getString(STATION, null) == null) {
            //UNPACK INTENT
            Intent nameSends = getIntent();
            xStation = nameSends.getStringExtra("xStation");
            tv_savestation.setText(xStation);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(STATION, xStation);
            editor.apply();
        }else{
            tv_savestation.setText(sp.getString(STATION, null));
        }

        tv_savelines = findViewById(R.id.tv_savelines);
        tv_savelines.setText(sp.getString(LINE, null));
//        Toast.makeText(AssetManagementMachine.this, sp.getString(LINE, null), Toast.LENGTH_SHORT).show();

        adapter = new AdapterAssetMachine(AssetManagementMachine.this, listAsset);
        gridviewAMSM = findViewById(R.id.gridviewAMSM);
        searchView = findViewById(R.id.SearchAMSM);

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
            Intent intent = new Intent(AssetManagementMachine.this, AssetManagementStation.class);
            SharedPreferences.Editor editor = sp.edit();
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
                Intent intent1 = new Intent(AssetManagementMachine.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(AssetManagementMachine.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(AssetManagementMachine.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssetManagementMachine.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(AssetManagementMachine.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(AssetManagementMachine.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(AssetManagementMachine.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void retrieveData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiAssetMachineData(tv_savelines.getText().toString(), tv_savestation.getText().toString());

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

                listAsset = response.body().getData();

                if(listAsset != null) {
                    adapter = new AdapterAssetMachine(AssetManagementMachine.this, listAsset);
                    gridviewAMSM.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

//               Toast.makeText(AssetManagementMachine.this, listWms.get(0).getElc_tag(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(AssetManagementMachine.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
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
                    AssetManagementMachine.this.adapter.getFilter().filter(newText);
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
//        Toast.makeText(AssetManagementMachine.this, datetimeString, Toast.LENGTH_SHORT).show();
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