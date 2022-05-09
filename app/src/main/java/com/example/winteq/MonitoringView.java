package com.example.winteq;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.monitoring.MonResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    FloatingActionButton editdatamon, deletedatamonz, mesindatamonz;
    Api_Interface apiInterface;
    private List<MonData> listMonData;
    MonData monData;

    TextView montag, monstation, monline, monstat, monp1, monp2, monp3, monp4, monpersonincharge, mondate, mondesc;
    ImageView monpic;

    private String Xid, Xno, Xdate, Xplant, Xline, Xstation, Xstatus, Xpic, Ximage, Xdesc, Xp1, Xp2, Xp3, Xp4;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring_view);

        //call api
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        //Find Variable
        drawerLayout = findViewById(R.id.monview); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        montag = findViewById(R.id.montag);
        monstation = findViewById(R.id.monname);
        monline = findViewById(R.id.monline);
        monstat = findViewById(R.id.monstat);
        monp1 = findViewById(R.id.monparam1);
        monp2 = findViewById(R.id.monparam2);
        monp3 = findViewById(R.id.monparam3);
        monp4 = findViewById(R.id.monparam4);
//        monday = findViewById(R.id.monday);
        mondate = findViewById(R.id.mondate);
        mondesc = findViewById(R.id.mondesc);
        monpic = findViewById(R.id.monpic);
        editdatamon = findViewById(R.id.editdatamon);
        deletedatamonz = findViewById(R.id.deletedatamonz);
        mesindatamonz = findViewById(R.id.mesindatamonz);
        monpersonincharge = findViewById(R.id.monpersonincharge);

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
        mondesc.setText(Xdesc);
        monpersonincharge.setText(Xpic);

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
            ImageView Image1 = monpic;
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

        //Move to Another Layout
        editdatamon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<MonResponseData> getData = aiData.monviewData(Xid);

                getData.enqueue(new Callback<MonResponseData>() {
                    @Override
                    public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listMonData = response.body().getData();

                            String Vmon_id = listMonData.get(0).getMon_id();
                            String Vmon_no = listMonData.get(0).getMon_no();
                            String Vmon_date = listMonData.get(0).getMon_date();
                            String Vplant = listMonData.get(0).getPlant();
                            String Vline = listMonData.get(0).getLine();
                            String Vstation = listMonData.get(0).getStation();
                            String Vmon_status = listMonData.get(0).getMon_status();
                            String Vmon_pic = listMonData.get(0).getMon_pic();
                            String Vmon_image = listMonData.get(0).getMon_image();
                            String Vmon_desc = listMonData.get(0).getMon_desc();
                            String Vparameter1 = listMonData.get(0).getParameter1();
                            String Vparameter2 = listMonData.get(0).getParameter2();
                            String Vparameter3 = listMonData.get(0).getParameter3();
                            String Vparameter4 = listMonData.get(0).getParameter4();

//                    Toast.makeText(MonitoringView.this, "Status: "+status+" | Message: "+message+ " | Data : "+varImage, Toast.LENGTH_SHORT).show();

                            Intent send = new Intent(MonitoringView.this, MonitoringUpdate.class);
                            send.putExtra("Xid", Vmon_id);
                            send.putExtra("Xno", Vmon_no);
                            send.putExtra("Xdate", Vmon_date);
                            send.putExtra("Xplant", Vplant);
                            send.putExtra("Xline", Vline);
                            send.putExtra("Xstation", Vstation);
                            send.putExtra("Xstatus", Vmon_status);
                            send.putExtra("Xpic", Vmon_pic);
                            send.putExtra("Ximage", Vmon_image);
                            send.putExtra("Xdesc", Vmon_desc);
                            send.putExtra("Xp1", Vparameter1);
                            send.putExtra("Xp2", Vparameter2);
                            send.putExtra("Xp3", Vparameter3);
                            send.putExtra("Xp4", Vparameter4);

                            MonitoringView.this.startActivity(send);
                        }
                    }
                    @Override
                    public void onFailure(Call<MonResponseData> call, Throwable t) {
                        Toast.makeText(MonitoringView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deletedatamonz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MonitoringView.this);
                alertDialogBuilder.setTitle("Are you sure you want to delete this Station "+Xstation+" on "+Xline+" data?")
                        .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                        .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                                Call<MonData> deleteData = aiData.mondeleteData(Xid, Xline, Xstation);

                                deleteData.enqueue(new Callback<MonData>() {
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
                                            Toast.makeText(MonitoringView.this, sr, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MonitoringView.this, Monitoring.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MonitoringView.this, sr, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MonData> call, Throwable t) {
                                        Toast.makeText(MonitoringView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        });

        mesindatamonz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(MonitoringView.this, SensorMachine.class);
                m.putExtra("xLine", Xline);
                m.putExtra("xStation", Xstation);
                MonitoringView.this.startActivity(m);
            }
        });

    }

    //Setup onBack Press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(MonitoringView.this, Monitoring.class);
            startActivity(intent);
        }
    }

    //Setup Drawer move layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(MonitoringView.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(MonitoringView.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MonitoringView.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MonitoringView.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(MonitoringView.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(MonitoringView.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(MonitoringView.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}