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
import android.widget.Spinner;
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
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.wms.WmsData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailError extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    TextView tv_btitle, tv_bline, tv_bstation;
    TextView tv_stitle, tv_sline, tv_sstation, tv_srpm, tv_sfeed, tv_sspeed, tv_stime, tv_sdate;
    TextView tv_etitle, tv_erpm, tv_efeed, tv_espeed, tv_etime, tv_etotal;
    TextView tv_desc;
    TextView errorbox, errortext, errorresult;
    Spinner preset;
    TextView feed;

    private String xId, xStatus, xCutSpeed, xRpm, xFeed, xCutTime, xError, xErrorSpeed, xErrorFeed, xErrorRpm, xErrorTime, xDate, xLine, xStation, xMachine;
    private List<WmsData> listGetWms;
    private List<MonData> listGetMon;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";
    private static final String STATIMAGEWMS = "imagewms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_error);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.derror);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        preset = findViewById(R.id.preset);
        feed = findViewById(R.id.dateregisterto);

        preset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newItem = preset.getSelectedItem().toString();
                if(newItem.equals("Preset 2")){
                    feed.setText("6");
                }else{
                    feed.setText("4");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent sendSP = getIntent();
        xId = sendSP.getStringExtra("xId");
        xStatus = sendSP.getStringExtra("xStatus");
        xCutSpeed = sendSP.getStringExtra("xCutSpeed");
        xRpm = sendSP.getStringExtra("xRpm");
        xFeed = sendSP.getStringExtra("xFeed");
        xCutTime = sendSP.getStringExtra("xCutTime");
        xError = sendSP.getStringExtra("xError");
        xErrorSpeed = sendSP.getStringExtra("xErrorSpeed");
        xErrorFeed = sendSP.getStringExtra("xErrorFeed");
        xErrorRpm  = sendSP.getStringExtra("xErrorRpm");
        xErrorTime = sendSP.getStringExtra("xErrorTime");
        xDate = sendSP.getStringExtra("xDate");
        xLine = sendSP.getStringExtra("xLine");
        xStation = sendSP.getStringExtra("xStation");
        xMachine = sendSP.getStringExtra("xMachine");

        //base data id
        tv_btitle = findViewById(R.id.itemnamewzh);
        tv_bline = findViewById(R.id.tv1zh);
        tv_bstation = findViewById(R.id.itemqtyzh);

        //sensor data id
        tv_stitle = findViewById(R.id.itemnamewzg);
        tv_sline = findViewById(R.id.tv1zg);
        tv_sstation = findViewById(R.id.itemqtyzg);
        tv_srpm = findViewById(R.id.itemtypeg);
        tv_sfeed = findViewById(R.id.itemlifetimeg);
        tv_sspeed = findViewById(R.id.itemlifetimeg2);
        tv_stime = findViewById(R.id.itemlifetimeg3);
        tv_sdate = findViewById(R.id.itemlifetimeg4);

        //error data id
        tv_etitle = findViewById(R.id.itemnamewzg2);
        tv_erpm = findViewById(R.id.itemtypeg2);
        tv_efeed = findViewById(R.id.itemtypeg3);
        tv_espeed = findViewById(R.id.itemtypeg4);
        tv_etime = findViewById(R.id.itemtypeg5);
        tv_etotal = findViewById(R.id.itemtypeg6);

        //description id
        tv_desc = findViewById(R.id.enterc);
        errorbox = findViewById(R.id.judulkiri3);
        errortext = findViewById(R.id.judulkiri2);
        errorresult = findViewById(R.id.itemtypeg6);

        //set base data
        tv_btitle.setText(xMachine);
        tv_bline.setText(xLine);
        tv_bstation.setText(xStation);

        //set sensor data
        tv_stitle.setText(xMachine);
        tv_sline.setText(xLine);
        tv_sstation.setText(xStation);
        tv_srpm.setText(xRpm);
        tv_sfeed.setText(xFeed);
        tv_sspeed.setText(xCutSpeed);
        tv_stime.setText(xCutTime);
        tv_sdate.setText(xDate);

        //convert error data
        if(xErrorRpm != null && xErrorFeed != null && xErrorSpeed != null && xErrorTime != null && xError != null) {
            //convert string to float
            float erpm = Float.parseFloat(xErrorRpm);
            float efeed = Float.parseFloat(xErrorFeed);
            float espeed = Float.parseFloat(xErrorSpeed);
            float etime = Float.parseFloat(xErrorTime);
            float etotal = Float.parseFloat(xError);

            //change float number into percentage
            float rpm = erpm * 100;
            float feed = efeed * 100;
            float speed = espeed * 100;
            float time = etime * 100;
            float total = etotal * 100;

            //convert float to string
            String errorrpm = Float.toString(rpm) + "%";
            String errorfeed = Float.toString(feed) + "%";
            String errorspeed = Float.toString(speed) + "%";
            String errortime = Float.toString(time) + "%";
            String errortotal = Float.toString(total) + "%";

            //set error data
            String Good = "Good";
            String Ok = "Ok";
            String Error = "Error!";
            if (etotal >= 0 && etotal <= 0.101) {
                tv_etitle.setTextColor(getResources().getColor(R.color.colorGreen));
                errorbox.setBackgroundResource(R.drawable.linetgreen);
                errortext.setTextColor(getResources().getColor(R.color.colorGreen));
                errorresult.setTextColor(getResources().getColor(R.color.colorGreen));
                tv_etitle.setText(Good);
            } else if (etotal > 0.10 && etotal <= 0.201) {
                tv_etitle.setTextColor(getResources().getColor(R.color.color_yellow));
                errorbox.setBackgroundResource(R.drawable.linetyellow);
                errortext.setTextColor(getResources().getColor(R.color.color_yellow));
                errorresult.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_etitle.setText(Ok);
            } else if (etotal > 0.20) {
                tv_etitle.setTextColor(getResources().getColor(R.color.red));
                errorbox.setBackgroundResource(R.drawable.linetred);
                errortext.setTextColor(getResources().getColor(R.color.red));
                errorresult.setTextColor(getResources().getColor(R.color.red));
                tv_etitle.setText(Error);
            }
            tv_erpm.setText(errorrpm);
            tv_efeed.setText(errorfeed);
            tv_espeed.setText(errorspeed);
            tv_etime.setText(errortime);
            tv_etotal.setText(errortotal);

            //set description
            String RPM = "Error might be located in RPM Sensor";
            String Feed = "Error might be located in Feed Sensor";
            String Both = "Error might be located in RPM Sensor and Feed Sensor";
            String Okai1 = "RPM Sensor should be checked";
            String Okai2 = "Feed Sensor should be checked";
            String Okai3 = "RPM Sensor and Feed Sensor should be checked";
            String None = "RPM Sensor and Feed Sensor working as it should be";

            if(rpm > feed && feed > 10 && feed <= 20 && rpm > 10 && rpm <= 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(RPM);
            }
            else if(feed > rpm && feed > 10 && feed <= 20 && rpm > 10 && rpm <= 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(Feed);
            }
            else if(feed > 20 && rpm <= 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(Feed);
            }
            else if(rpm > 20 && feed <= 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(RPM);
            }
            else if(feed > 20 && rpm > 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(Both);
            }
            else if(feed == rpm && feed > 20 && rpm > 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(Both);
            }
            else if(feed == rpm && feed > 10 && feed <= 20 && rpm > 10 && rpm <= 20) {
                tv_desc.setTextColor(getResources().getColor(R.color.red));
                tv_desc.setText(Both);
            }
            else if(feed > 10 && feed <= 20){
                tv_desc.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_desc.setText(Okai2);
            }
            else if(rpm > 10 && rpm <= 20){
                tv_desc.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_desc.setText(Okai1);
            }
            else if(feed > 10 && feed <= 20 && rpm > 10 && rpm <= 20){
                tv_desc.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_desc.setText(Okai3);
            }
            else{
                tv_desc.setTextColor(getResources().getColor(R.color.colorGreen));
                tv_desc.setText(None);
            }
        }

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


        if (!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS, Base64.DEFAULT);
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
            Intent intent = new Intent(DetailError.this, Monitoring.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(DetailError.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(DetailError.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(DetailError.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailError.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(DetailError.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(DetailError.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(DetailError.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
