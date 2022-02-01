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

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class HistoryView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    TextView prtitle, prdate, prline, prstation, prmachine, prdesc;
    TextView sltitle, sldate, slpic, sldesc;
    ImageView propic, solpic;

    private String xId, xLine, xStation, xMachine, xDatep, xPic, xDates, xPro, xImgp, xImgs, xPartp
            , xParts, xSol, xQtyp, xQtys, xStatus, xType, xTitle;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history_view);

        //set SharedPreferences
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        drawerLayout = findViewById(R.id.historyview);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        prtitle = findViewById(R.id.prtitle);
        prdate = findViewById(R.id.prdate);
        prline = findViewById(R.id.tv3);
        prstation = findViewById(R.id.prstation);
        prmachine = findViewById(R.id.prmachine);
        prdesc = findViewById(R.id.prdesc);

        sltitle = findViewById(R.id.sltitle);
        sldate = findViewById(R.id.sldate);
        slpic = findViewById(R.id.slpic);
        sldesc = findViewById(R.id.sldesc);

        propic = findViewById(R.id.prpic);
        solpic = findViewById(R.id.solpic);

        //get intent
        Intent sendAP = getIntent();
        xId = sendAP.getStringExtra("xId");
        xLine = sendAP.getStringExtra("xLine");
        xStation = sendAP.getStringExtra("xStation");
        xMachine = sendAP.getStringExtra("xMachine");
        xDatep = sendAP.getStringExtra("xDatep");
        xPic = sendAP.getStringExtra("xPic");
        xDates = sendAP.getStringExtra("xDates");
        xPro = sendAP.getStringExtra("xPro");
        xImgp = sendAP.getStringExtra("xImgp");
        xImgs = sendAP.getStringExtra("xImgs");
        xPartp = sendAP.getStringExtra("xPartp");
        xParts = sendAP.getStringExtra("xParts");
        xSol = sendAP.getStringExtra("xSol");
        xQtyp = sendAP.getStringExtra("xQtyp");
        xQtys = sendAP.getStringExtra("xQtys");
        xStatus = sendAP.getStringExtra("xStatus");
        xType = sendAP.getStringExtra("xType");
        xTitle = sendAP.getStringExtra("xTitle");

        //set text
        prtitle.setText(xTitle);
        prdate.setText(xDatep);
        prline.setText(xLine);
        prstation.setText(xStation);
        prmachine.setText(xMachine);
        prdesc.setText(xPro);

        sltitle.setText(xTitle);
        sldate.setText(xDates);
        slpic.setText(xPic);
        sldesc.setText(xSol);

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

        if(!(xImgp.isEmpty())) {
            String imageUri = xImgp;
            ImageView Imagepro = propic;
            Picasso.get().load(imageUri).into(Imagepro);
        }

        if(!(xImgs.isEmpty())) {
            String imageUri = xImgs;
            ImageView Imagesol = solpic;
            Picasso.get().load(imageUri).into(Imagesol);
        }

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
            Intent intent = new Intent(HistoryView.this, History.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(HistoryView.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(HistoryView.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HistoryView.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HistoryView.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(HistoryView.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(HistoryView.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(HistoryView.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}