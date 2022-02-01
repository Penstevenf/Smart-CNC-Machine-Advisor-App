package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.winteq.model.asset.AssetData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetManagementAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    RadioGroup radioGroup;
    RadioButton radioButton, rb_elc, rb_mec;
    EditText et_qty, et_part, et_assetunit;
    TextView tv_machine, machine_no_add;
    AssetData assetData;
    FloatingActionButton fb_assetadd;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";
    private static final String MACHINENAME = "machine_name";
    private static final String MACHINENO = "asset_no";
    private static final String LINE = "asset_line";
    private static final String STATION = "asset_station";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asset_management_add);

        //call API
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        //Setup Shared Preference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        tv_machine = findViewById(R.id.tv_assetaddmachine);
        tv_machine.setText(sp.getString(MACHINENAME, null));

        machine_no_add = findViewById(R.id.machine_no_add);
        machine_no_add.setText(sp.getString(MACHINENO, null));

        et_part = findViewById(R.id.et_assetaddpart);
        et_qty = findViewById(R.id.et_assetaddqty);
        radioGroup = findViewById(R.id.rg_add_asset);
        rb_elc = findViewById(R.id.rb_add_elektrik_asset);
        rb_mec = findViewById(R.id.rb_add_mekanik_asset);
        fb_assetadd = findViewById(R.id.fb_assetadd);
        et_assetunit = findViewById(R.id.et_assetunit);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.amadd);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

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

        fb_assetadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetManagementAdd();
            }
        });
    }

    private void AssetManagementAdd() {
        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String assetNo = machine_no_add.getText().toString();
        String machine_name = tv_machine.getText().toString();
        String asset_part = et_part.getText().toString();
        String asset_category = radioButton.getText().toString();
        String asset_qty = et_qty.getText().toString();
        String asset_line = sp.getString(LINE, null);
        String asset_station = sp.getString(STATION, null);
        String asset_unit = et_assetunit.getText().toString();
        ProgressDialog pd = new ProgressDialog(AssetManagementAdd.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Call<AssetData> amAddCall = apiInterface.aiAssetAddData(assetNo, machine_name, asset_part, asset_category, asset_qty, asset_line, asset_station, asset_unit);
        amAddCall.enqueue(new Callback<AssetData>() {
            @Override
            public void onResponse(Call<AssetData> call, Response<AssetData> response) {
                //sr untuk menampung array message dalam bentuk string
                //loop isi data dari array message lalu di append ke dalam string sr
                //if else untuk mencegah mengambil value awal dari string sr ("")
                String sr = "";
                for(int i=0 ; i<response.body().getMessage().length ; i++){
                    if(sr.length() == 0){
                        sr = response.body().getMessage()[i];
                    }else{
                        sr = sr + "\n" + response.body().getMessage()[i];
                    }
                }
                if(response.body() != null && response.body().isStatus()){
                    assetData = response.body();
                    pd.dismiss();

                    Toast.makeText(AssetManagementAdd.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AssetManagementAdd.this, AssetManagementView.class);
                    startActivity(intent);
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(AssetManagementAdd.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssetData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(AssetManagementAdd.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(AssetManagementAdd.this, AssetManagementView.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(AssetManagementAdd.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(AssetManagementAdd.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(AssetManagementAdd.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssetManagementAdd.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(AssetManagementAdd.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(AssetManagementAdd.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(AssetManagementAdd.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}