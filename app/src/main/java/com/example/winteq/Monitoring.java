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
import android.widget.GridView;
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

import com.example.winteq.adapter.monitoring.AdapterMonitoring1;
import com.example.winteq.adapter.monitoring.AdapterMonitoring2;
import com.example.winteq.adapter.monitoring.AdapterMonitoring3;
import com.example.winteq.adapter.monitoring.AdapterMonitoring4;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetData;
import com.example.winteq.model.asset.AssetResponseData;
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.monitoring.MonResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Monitoring extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    FloatingActionButton addmon;
    Api_Interface apiInterface;
    String id1, id2, id3, id4, line1, line2, line3, line4, stat1, stat2, stat3, stat4, station1, station2, station3, station4, machine1, machine2, machine3, machine4;

    private GridView grid1, grid2, grid3, grid4;
    private AdapterMonitoring1 adapter1;
    private AdapterMonitoring2 adapter2;
    private AdapterMonitoring3 adapter3;
    private AdapterMonitoring4 adapter4;

    private List<MonData> listMon1 = new ArrayList<>();
    private List<MonData> listMon2 = new ArrayList<>();
    private List<MonData> listMon3 = new ArrayList<>();
    private List<MonData> listMon4 = new ArrayList<>();

    private List<AssetData> listAsset1 = new ArrayList<>();
    private List<AssetData> listAsset2 = new ArrayList<>();
    private List<AssetData> listAsset3 = new ArrayList<>();
    private List<AssetData> listAsset4 = new ArrayList<>();

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    public static final String NOTIFICATION_NO = "number";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    public static final String CHANNEL_4_ID = "channel4";
    public static final String CHANNEL_5_ID = "channel5";
    private NotificationManagerCompat notificationManager;
    private static final String DONE2 = "done2?";

    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitoring);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        drawerLayout = findViewById(R.id.mon);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        addmon = findViewById(R.id.AddMon);

        grid1 = findViewById(R.id.ll1);
        grid2 = findViewById(R.id.ll2);
        grid3 = findViewById(R.id.ll3);
        grid4 = findViewById(R.id.ll4);

        adapter1 = new AdapterMonitoring1(Monitoring.this, listMon1);
        adapter2 = new AdapterMonitoring2(Monitoring.this, listMon2);
        adapter3 = new AdapterMonitoring3(Monitoring.this, listMon3);
        adapter4 = new AdapterMonitoring4(Monitoring.this, listMon4);

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
        addmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Monitoring.this, MonitoringAdd.class);
                startActivity(intent);
            }
        });


        //notif
        createNotificationChannels();
        createNotificationChannels2();
        createNotificationChannels3();
        createNotificationChannels4();
        notificationManager = NotificationManagerCompat.from(this);

        //run once only
        gridview1();
        gridview2();
        gridview3();
        gridview4();
        statusView1();
        statusView2();
        statusView3();
        statusView4();
        if(sp.getString(DONE2, null) == null){
            (new loading()).execute();
            sp.edit().putString(DONE2, "1").apply();
        }
//        if(id1 != "None"){
//            monaddStatus1();
//        }
//        if(id2 != "None"){
//            monaddStatus2();
//        }
//        if(id3 != "None"){
//            monaddStatus3();
//        }
//        if(id4 != "None"){
//            monaddStatus4();
//        }

        mRepeatHandler = new Handler();
        mRepeatRunnable = new Runnable() {
            @Override
            public void run() {
                gridview1();
                gridview2();
                gridview3();
                gridview4();
                statusView1();
                statusView2();
                statusView3();
                statusView4();
                (new loading()).execute();
//                if(id1 != "None"){
//                    monaddStatus1();
//                }
//                if(id2 != "None"){
//                    monaddStatus2();
//                }
//                if(id3 != "None"){
//                    monaddStatus3();
//                }
//                if(id4 != "None"){
//                    monaddStatus4();
//                }
                mRepeatHandler.postDelayed(mRepeatRunnable, 20000);
            }
        };
        mRepeatHandler.postDelayed(mRepeatRunnable,20000);
    }


    //call data for gridview
    public void gridview1(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon1Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon1 = response.body().getData();
                if(listMon1 != null) {
                    adapter1 = new AdapterMonitoring1(Monitoring.this, listMon1);
                    grid1.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview2(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon2Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon2 = response.body().getData();
                if(listMon2 != null) {
                    adapter2 = new AdapterMonitoring2(Monitoring.this, listMon2);
                    grid2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview3(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon3Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon3 = response.body().getData();
                if(listMon3 != null) {
                    adapter3 = new AdapterMonitoring3(Monitoring.this, listMon3);
                    grid3.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gridview4(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> showData = aiData.aiMon4Data();

        showData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();

//                Toast.makeText(HelpElec.this, "Status: "+status+" | Message: "+message, Toast.LENGTH_SHORT).show();

                listMon4 = response.body().getData();
                if(listMon4 != null) {
                    adapter4 = new AdapterMonitoring4(Monitoring.this, listMon4);
                    grid4.setAdapter(adapter4);
                    adapter4.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void statusView1(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiStat1Data();

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();
                listAsset1 = response.body().getData();
                stat1 = "Working";
                id1 = "None";
                line1 = "None";
                station1 = "None";
                machine1 = "None";
                if(listAsset1 != null){
                    id1 = response.body().getData().get(0).getAsset_id();
                    line1 = response.body().getData().get(0).getAsset_line();
                    station1 = response.body().getData().get(0).getAsset_station();
                    machine1 = response.body().getData().get(0).getMachine_name();
                    if (response.body().getData().get(0).getAsset_status().equals("2")) {
                        stat1 = "Breakdown";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("3")) {
                        stat1 = "Repairing";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("4")) {
                        stat1= "Ready To Use";
                    }
                }

//                 Toast.makeText(Monitoring.this, stat1+id1, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void statusView2(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiStat2Data();

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();
                listAsset2 = response.body().getData();
                stat2 = "Working";
                id2 = "None";
                line2 = "None";
                station2 = "None";
                machine2 = "None";
                if(listAsset2 != null){
                    id2 = response.body().getData().get(0).getAsset_id();
                    line2 = response.body().getData().get(0).getAsset_line();
                    station2 = response.body().getData().get(0).getAsset_station();
                    machine2 = response.body().getData().get(0).getMachine_name();
                    if (response.body().getData().get(0).getAsset_status().equals("2")) {
                        stat2 = "Breakdown";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("3")) {
                        stat2 = "Repairing";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("4")) {
                        stat2 = "Ready To Use";
                    }
                }

//                Toast.makeText(Monitoring.this, stat2+id2, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void statusView3(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiStat3Data();

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();
                listAsset3 = response.body().getData();
                stat3 = "Working";
                id3 = "None";
                line3 = "None";
                station3 = "None";
                machine3 = "None";
                if(listAsset3 != null){
                    id3 = response.body().getData().get(0).getAsset_id();
                    line3 = response.body().getData().get(0).getAsset_line();
                    station3 = response.body().getData().get(0).getAsset_station();
                    machine3 = response.body().getData().get(0).getMachine_name();
                    if (response.body().getData().get(0).getAsset_status().equals("2")) {
                        stat3 = "Breakdown";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("3")) {
                        stat3 = "Repairing";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("4")) {
                        stat3 = "Ready To Use";
                    }
                }

//                Toast.makeText(Monitoring.this, stat3+id3, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void statusView4(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<AssetResponseData> showData = aiData.aiStat4Data();

        showData.enqueue(new Callback<AssetResponseData>() {
            @Override
            public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {
                boolean status = response.body().isStatus();
                String message = response.body().getMessage();
                listAsset4 = response.body().getData();
                stat4 = "Working";
                id4 = "None";
                line4 = "None";
                station4 = "None";
                machine4 = "None";
                if(listAsset4 != null){
                    id4 = response.body().getData().get(0).getAsset_id();
                    line4 = response.body().getData().get(0).getAsset_line();
                    station4 = response.body().getData().get(0).getAsset_station();
                    machine4 = response.body().getData().get(0).getMachine_name();
                    if (response.body().getData().get(0).getAsset_status().equals("2")) {
                        stat4 = "Breakdown";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("3")) {
                        stat4 = "Repairing";
                    }
                    if (response.body().getData().get(0).getAsset_status().equals("4")) {
                        stat4 = "Ready To Use";
                    }
                }

//                Toast.makeText(Monitoring.this, stat4+id4, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(Monitoring.this, "Failed To Display Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void monaddStatus1(){
        String id = id1;
        String status = stat1;

        Call<MonData> MonitoringAddcall = apiInterface.monstatusData(id, status);
        MonitoringAddcall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
                if(stat1 != "Working"){
                    sendOnChannel1();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
//                Toast.makeText(Monitoring.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void monaddStatus2(){
        String id = id2;
        String status = stat2;

        Call<MonData> MonitoringAddcall = apiInterface.monstatusData(id, status);
        MonitoringAddcall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
                if(stat2 != "Working"){
                    sendOnChannel2();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
//                Toast.makeText(Monitoring.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void monaddStatus3(){
        String id = id3;
        String status = stat3;

        Call<MonData> MonitoringAddcall = apiInterface.monstatusData(id, status);
        MonitoringAddcall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
                if(stat3 != "Working"){
                    sendOnChannel3();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
//                Toast.makeText(Monitoring.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void monaddStatus4(){
        String id = id4;
        String status = stat4;

        Call<MonData> MonitoringAddcall = apiInterface.monstatusData(id, status);
        MonitoringAddcall.enqueue(new Callback<MonData>() {
            @Override
            public void onResponse(Call<MonData> call, Response<MonData> response) {
                if(stat4 != "Working"){
                    sendOnChannel4();
                }
            }

            @Override
            public void onFailure(Call<MonData> call, Throwable t) {
//                Toast.makeText(Monitoring.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Setup onBack Press
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Monitoring.this, Dashboard.class);
            startActivity(intent);
        }
    }

    //Setup Drawer move layout
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(Monitoring.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(Monitoring.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Monitoring.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Monitoring.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(Monitoring.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(Monitoring.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(Monitoring.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Station Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Status Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    private void createNotificationChannels2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Station Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Status Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    private void createNotificationChannels3() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_4_ID,
                    "Station Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Status Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    private void createNotificationChannels4() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_5_ID,
                    "Station Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Status Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    public void sendOnChannel1() {
        String title = "WARNING";
        String message = "Line 1, Station "+station1+" Status Changed to "+stat1;
        Intent activityIntent1 = new Intent(this, SwipeProblemMonitoring.class);
        activityIntent1.putExtra("xLine", line1);
        activityIntent1.putExtra("xStation", station1);
        activityIntent1.putExtra("xMachine", machine1);

        PendingIntent contentIntent1 = PendingIntent.getActivity(this, 1, activityIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setFullScreenIntent(contentIntent1, true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground, "Detail", contentIntent1)
                .build();
        //check if pendingintent called or applied (when Detail clicked)
        //purpose to differentiate each putextra data in each line when Detail clicked
        Intent checkIntent = new Intent(getApplicationContext(), Monitoring.class);
        if((PendingIntent.getBroadcast(getBaseContext(), 1, checkIntent, PendingIntent.FLAG_NO_CREATE) == null)) {
            SharedPreferences.Editor editor1 = sp.edit();
            editor1.putString(NOTIFICATION_NO, "1");
            editor1.apply();
        }
        notificationManager.notify(2, notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public void sendOnChannel2() {
        String title = "WARNING";
        String message = "Line 2, Station "+station2+" Status Changed to "+stat2;
        Intent activityIntent2 = new Intent(this, SwipeProblemMonitoring.class);
        activityIntent2.putExtra("xLine", line2);
        activityIntent2.putExtra("xStation", station2);
        activityIntent2.putExtra("xMachine", machine2);

        PendingIntent contentIntent2 = PendingIntent.getActivity(this, 2, activityIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setFullScreenIntent(contentIntent2, true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground, "Detail", contentIntent2)
                .build();
        Intent checkIntent = new Intent(getApplicationContext(), Monitoring.class);
        if((PendingIntent.getBroadcast(getBaseContext(), 2, checkIntent, PendingIntent.FLAG_NO_CREATE) == null)) {
            SharedPreferences.Editor editor2 = sp.edit();
            editor2.putString(NOTIFICATION_NO, "2");
            editor2.apply();
        }
        notificationManager.notify(3, notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public void sendOnChannel3() {
        String title = "WARNING";
        String message = "Line 3, Station "+station3+" Status Changed to "+stat3;
        Intent activityIntent3 = new Intent(this, SwipeProblemMonitoring.class);
        activityIntent3.putExtra("xLine", line3);
        activityIntent3.putExtra("xStation", station3);
        activityIntent3.putExtra("xMachine", machine3);

        PendingIntent contentIntent3 = PendingIntent.getActivity(this, 3, activityIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_4_ID)
                .setFullScreenIntent(contentIntent3, true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground, "Detail", contentIntent3)
                .build();
        Intent checkIntent = new Intent(getApplicationContext(), Monitoring.class);
        if((PendingIntent.getBroadcast(getBaseContext(), 3, checkIntent, PendingIntent.FLAG_NO_CREATE) == null)) {
            SharedPreferences.Editor editor3 = sp.edit();
            editor3.putString(NOTIFICATION_NO, "3");
            editor3.apply();
            Toast.makeText(Monitoring.this, sp.getString(NOTIFICATION_NO, null), Toast.LENGTH_SHORT).show();
        }
        notificationManager.notify(4, notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    public void sendOnChannel4() {
        String title = "WARNING";
        String message = "Line 4, Station "+station4+" Status Changed to "+stat4;
        Intent activityIntent4 = new Intent(this, SwipeProblemMonitoring.class);
        activityIntent4.putExtra("xLine", line4);
        activityIntent4.putExtra("xStation", station4);
        activityIntent4.putExtra("xMachine", machine4);

        PendingIntent contentIntent4 = PendingIntent.getActivity(this, 4, activityIntent4, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_5_ID)
                .setFullScreenIntent(contentIntent4, true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_launcher_foreground, "Detail", contentIntent4)
                .build();
        Intent checkIntent = new Intent(getApplicationContext(), Monitoring.class);
        if((PendingIntent.getBroadcast(getBaseContext(), 4, checkIntent, PendingIntent.FLAG_NO_CREATE) == null)) {
            SharedPreferences.Editor editor4 = sp.edit();
            editor4.putString(NOTIFICATION_NO, "4");
            editor4.apply();
        }
        notificationManager.notify(5, notification);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
    }


    public class loading extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if(id1 != "None"){
                monaddStatus1();
            }
            if(id2 != "None"){
                monaddStatus2();
            }
            if(id3 != "None"){
                monaddStatus3();
            }
            if(id4 != "None"){
                monaddStatus4();
            }
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

}