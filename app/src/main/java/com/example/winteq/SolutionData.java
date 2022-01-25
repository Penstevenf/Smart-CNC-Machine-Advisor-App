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

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.history.HistoryData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class SolutionData extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;

    ImageView ipic;
    Button up;
    TextView line,station,machine,pic,part,desc,date,idproblem;
    FloatingActionButton riad;
    Api_Interface apiInterface;
    HistoryData historyData;
    Bitmap bitmap;

    private String xId, xLine, xStation, xMachine, xDate, xPic, xTitle, xProblem, xImage;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_solution_data);

        //Get SharedPReference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        //Find Variable
        drawerLayout = findViewById(R.id.soldata); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Call ApiInterface
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        line = findViewById(R.id.tv1);
        station = findViewById(R.id.pstation);
        machine = findViewById(R.id.pmachine);
        pic = findViewById(R.id.ppic);
        desc = findViewById(R.id.itemdesc);
        part = findViewById(R.id.partreq);
        ipic = findViewById(R.id.itempic);
        up = findViewById(R.id.upreq);
        riad = findViewById(R.id.req);
        date = findViewById(R.id.tvdate);
        idproblem = findViewById(R.id.idproblem);

        //getIntent
        Intent sendPr = getIntent();
        xId = sendPr.getStringExtra("xId");
        xLine = sendPr.getStringExtra("xLine");
        xStation = sendPr.getStringExtra("xStation");
        xMachine = sendPr.getStringExtra("xMachine");
        xDate = sendPr.getStringExtra("xDate");
        xPic = sendPr.getStringExtra("xPic");
        xTitle = sendPr.getStringExtra("xTitle");
        xProblem = sendPr.getStringExtra("xProblem");
        xImage = sendPr.getStringExtra("xImage");

        //setText
        line.setText(xLine);
        station.setText(xStation);
        machine.setText(xMachine);
        pic.setText(xPic);
        desc.setText(xProblem);
        part.setText(xTitle);
        date.setText(xDate);
        idproblem.setText(xId);

        //Setup Header
        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        //Setup Toolbar
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Input Name and Profile to Drawer
        nama.setText(sp.getString(FULLNAME, null));

        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image2);
        }

        if(!(xImage.isEmpty())) {
            String imageUri = xImage;
            ImageView Image2 = ipic;
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            pfph.setImageBitmap(bitmap);
        }


        riad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SolutionData.this, SolutionAdd.class);
                intent.putExtra("xId", xId);
                intent.putExtra("xTitle", xTitle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(SolutionData.this, Solution.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(SolutionData.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(SolutionData.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(SolutionData.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SolutionData.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(SolutionData.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(SolutionData.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(SolutionData.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}