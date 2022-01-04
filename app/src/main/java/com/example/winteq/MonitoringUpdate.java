package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.monitoring.MonData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringUpdate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    FloatingActionButton fb_edit_mon;
    Api_Interface apiInterface;
    Button btn_moneditimage;
    Bitmap bitmap;
    MonData monData;

    TextView montag, monstation, monline, monstat, monp1, monp2, monp3, monp4, monpersonincharge, mondate, monid;
    EditText et_mondesc;
    ImageView iv_monpic;

    private String Xid, Xno, Xdate, Xplant, Xline, Xstation, Xstatus, Xpic, Ximage, Xdesc, Xp1, Xp2, Xp3, Xp4;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring_update);

        //call api
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        //Find Variable
        drawerLayout = findViewById(R.id.monupdate); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        montag = findViewById(R.id.montagz);
        monstation = findViewById(R.id.monnamez);
        monline = findViewById(R.id.monlinez);
        monstat = findViewById(R.id.monstatz);
        monp1 = findViewById(R.id.monparam1z);
        monp2 = findViewById(R.id.monparam2z);
        monp3 = findViewById(R.id.monparam3z);
        monp4 = findViewById(R.id.monparam4z);
        monid = findViewById(R.id.monid);
//        monday = findViewById(R.id.monday);
        mondate = findViewById(R.id.mondatez);
        et_mondesc = findViewById(R.id.et_mondesc);
        iv_monpic = findViewById(R.id.iv_monpic);
        fb_edit_mon = findViewById(R.id.fb_edit_mon);
        monpersonincharge = findViewById(R.id.monpersoninchargez);
        btn_moneditimage = findViewById(R.id.btn_moneditimage);

        //UNPACK INTENT
        Intent send = getIntent();
        Xid = send.getStringExtra("Xid");
        Xno = send.getStringExtra("Xno");
        Xdate = send.getStringExtra("Xdate");
        Xplant = send.getStringExtra("Xplant");
        Xline = send.getStringExtra("Xline");
        Xstation = send.getStringExtra("Xstation");
        Xstatus = send.getStringExtra("Xstatus");
        Xpic = send.getStringExtra("Xpic");
        Ximage = send.getStringExtra("Ximage");
        Xdesc = send.getStringExtra("Xdesc");
        Xp1 = send.getStringExtra("Xp1");
        Xp2 = send.getStringExtra("Xp2");
        Xp3 = send.getStringExtra("Xp3");
        Xp4 = send.getStringExtra("Xp4");

        //Set Text From Intent Data
        montag.setText(Xno);
        monline.setText(Xline);
        monstation.setText(Xstation);
        monstat.setText(Xstatus);
        monp1.setText(Xp1);
        monp2.setText(Xp2);
        monp3.setText(Xp3);
        monp4.setText(Xp4);
        mondate.setText(Xdate);
        et_mondesc.setText(Xdesc);
        monpersonincharge.setText(Xpic);
        monid.setText(Xid);

        //Setup Header
        View header = navigationView.getHeaderView(0);
        TextView nama = (TextView) header.findViewById(R.id.fname);

        //Setup Shared Preference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

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

        if(!(Ximage.isEmpty())) {
            String imageUri1 = Ximage;
            ImageView Image1 = iv_monpic;
            Picasso.get().load(imageUri1).into(Image1);
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

        btn_moneditimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        //Update Data
        fb_edit_mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MONUpdate();
            }
        });

    }

    //Setup onBack Press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(MonitoringUpdate.this, Monitoring.class);
            startActivity(intent);
        }
    }

    //Setup Drawer move layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(MonitoringUpdate.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(MonitoringUpdate.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MonitoringUpdate.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MonitoringUpdate.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(MonitoringUpdate.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(MonitoringUpdate.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(MonitoringUpdate.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 0:
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 0 && resultCode == RESULT_OK && data != null){

                    Uri filepath = data.getData();
                    InputStream imageStream;

                    try {
                        imageStream = getContentResolver().openInputStream(filepath);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        this.iv_monpic.setImageBitmap(bitmap);
                        View header = navigationView.getHeaderView(0);

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) iv_monpic.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

    private void MONUpdate() {
        String id = monid.getText().toString();
        String description = et_mondesc.getText().toString();
        String image = getStringImage(bitmap);
        ProgressDialog pd = new ProgressDialog(MonitoringUpdate.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        Call<MonData> MonitoringUpdatecall = apiInterface.aiMonUpdateData(id, description, image);
        MonitoringUpdatecall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
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
                    monData = response.body();
                    pd.dismiss();

                    Toast.makeText(MonitoringUpdate.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MonitoringUpdate.this, Monitoring.class);
                    startActivity(intent);
                    finish();

                } else {
                    pd.dismiss();
                    Toast.makeText(MonitoringUpdate.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(MonitoringUpdate.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}