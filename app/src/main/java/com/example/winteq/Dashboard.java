package com.example.winteq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetResponseData;
import com.example.winteq.model.user.UserData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.ZoneId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    UserData userData;
    Button btn_logout;
    TextView et_username, et_fullname, tv_ouput, tv_notifdate;
    String username, fullname, npk, email;
    SharedPreferences sp;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView profile, stats;
    Button ARCU,Mon,AstM,WMS,Ass,Tech,Sol,His;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String ID = "id";
    private static final String NPK = "npk";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String TELP = "no_telp";
    private static final String STAT = "stat";
    private static final String IMAGE = "image";
    private static final String LINE = "asset_line";
    private static final String STATION = "asset_station";
    private static final String MACHINE = "machine_name";


    //notif
    public static final String CHANNEL_1_ID = "channel1";
    private NotificationManagerCompat notificationManager;

    //loop
    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;
    private static final String DONE = "done?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);


        drawerLayout = findViewById(R.id.dash);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        ARCU = findViewById(R.id.ARCU);
        Mon = findViewById(R.id.Monitoring);
        AstM = findViewById(R.id.AssetM);
        WMS = findViewById(R.id.WMS);
        Ass = findViewById(R.id.Assistance);
        Tech = findViewById(R.id.TechSP);
        Sol = findViewById(R.id.Solution);
        His = findViewById(R.id.History);
        stats = findViewById(R.id.stat);
        profile = findViewById(R.id.profile);

        tv_ouput = findViewById(R.id.output);
        tv_notifdate = findViewById(R.id.notifdate);

        View header = navigationView.getHeaderView(0);

        et_username = (TextView) findViewById(R.id.Name);
        et_fullname = (TextView) header.findViewById(R.id.fname);

        //code textview
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        npk = sp.getString(NPK, null);
        username = sp.getString(USERNAME, null);
        fullname = sp.getString(FULLNAME, null);
        email = sp.getString(EMAIL, null);
        sp.getString(ID, null);
        sp.getString(TELP, null);

        //Set Sharedpreference asset Management data to Null
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LINE, null);
        editor.putString(STATION, null);
        editor.putString(MACHINE, null);
        editor.apply();


//        Toast.makeText(getApplicationContext(), fullname,Toast.LENGTH_SHORT).show();
        et_username.setText(username + " - " + npk + " - " + email);
        et_fullname.setText(fullname);


        navigationView.bringToFront();

        setSupportActionBar(toolbar);

        navigationView.setCheckedItem(R.id.nav_home);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //notif
        createNotificationChannels();
        notificationManager = NotificationManagerCompat.from(this);

        if(sp.getString(DONE, null) == null){
            (new loading()).execute();
            sp.edit().putString(DONE, "1").apply();
        }

        mRepeatHandler = new Handler();
        mRepeatRunnable = new Runnable() {
            @Override
            public void run() {
                (new loading()).execute();
                mRepeatHandler.postDelayed(mRepeatRunnable, 20000);
            }
        };
        mRepeatHandler.postDelayed(mRepeatRunnable,20000);


        //set image
        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image1 = (ImageView) findViewById(R.id.profile);
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image1);
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            profile.setImageBitmap(bitmap);
            pfph.setImageBitmap(bitmap);
        }


        ARCU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,CheckUp.class);
                startActivity(intent);
            }
        });

        Mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Monitoring.class);
                startActivity(intent);
            }
        });

        AstM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,AssetManagement.class);
                startActivity(intent);
            }
        });

        WMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,WMS.class);
                startActivity(intent);
            }
        });

        Ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Assistance.class);
                startActivity(intent);
            }
        });

        Tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,TechnicalSupport.class);
                startActivity(intent);
            }
        });

        Sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Solution.class);
                startActivity(intent);
            }
        });

        His.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,History.class);
                startActivity(intent);
            }
        });

        String sta = sp.getString(STAT, null);

        if (sta.equals("1")){
            stats.setImageResource(R.drawable.rectangle_green);
        }
        else{
            stats.setImageResource(R.drawable.rectangle_red);
        }

        return;


    }

    //run di background loop
//    private class onBackgroound extends AsyncTask<String, String, String>{
//
//    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Dashboard.this, Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Dashboard.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent intent1 = new Intent(Dashboard.this, Profile.class);
                startActivity(intent1);
                break;

            case R.id.nav_grafik:
                Intent intent2 = new Intent(Dashboard.this, Graph.class);
                startActivity(intent2);
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(Dashboard.this, Contact.class);
                startActivity(intent3);
                break;


            case R.id.nav_help:
                Intent intent4 = new Intent(Dashboard.this, Help.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Machine Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Problem Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    public void sendOnChannel1() {
        String title = "WARNING";
        String message = "Machine Critical Part Lifetime Runs Out In 7 Days!";
        Intent activityIntent = new Intent(this, SwipeProblem.class);
//        String value = "CNC Machine";
//        activityIntent.putExtra("key", value);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setFullScreenIntent(contentIntent, true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground, "Check", contentIntent)
                .build();

        notificationManager.notify(1, notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;

    }


    public class loading extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            retrieveNotifData();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

    public void retrieveNotifData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiAssetNotifData();

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();
                String notifdata = response.body().getNotifdata();
//                listAsset = response.body().getData();

                //get current date and change to string
                ZoneId zoneId = ZoneId.of( "Asia/Jakarta" ) ;  // Or ZoneOffset.UTC or ZoneId.systemDefault()
                LocalDate today = LocalDate.now( zoneId ) ;
                String output = today.toString() ;


//                tv_ouput.setText(output);
//                tv_notifdate.setText(notifdata);

                if(notifdata.equals(output)){
                    sendOnChannel1();
                }if(notifdata.equals("None")){
                    Toast.makeText(Dashboard.this, "For now there is no critical part whose lifespan is running out", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Failed To Get Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}