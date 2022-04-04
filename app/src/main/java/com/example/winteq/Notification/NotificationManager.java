package com.example.winteq.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.winteq.R;
import com.example.winteq.SwipeProblem;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetResponseData;

import java.time.LocalDate;
import java.time.ZoneId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationManager extends Service{
    //notif
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private NotificationManagerCompat notificationManager;

    //loop
    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;

    Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //notif
        context.startForegroundService(intent);
        createNotificationChannels();
        createNotificationChannels2();
        notificationManager = NotificationManagerCompat.from(this);

        (new loading()).execute();
//        mRepeatHandler = new Handler();
//        mRepeatRunnable = new Runnable() {
//            @Override
//            public void run() {
//                    (new loading()).execute();
//                mRepeatHandler.postDelayed(mRepeatRunnable, 20000);
//            }
//        };
//        mRepeatHandler.postDelayed(mRepeatRunnable,20000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Machine Notification Channel",
                    android.app.NotificationManager.IMPORTANCE_HIGH
            );

            channel1.setDescription("Problem Channel");

            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    private void createNotificationChannels2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Machine Notification Channel",
                    android.app.NotificationManager.IMPORTANCE_HIGH
            );

            channel2.setDescription("Problem Channel");

            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel2);
        }
    }

    public void sendOnChannel1() {
        String title = "ALERT";
        String message = "Machine Problem Detected";
        Intent activityIntent = new Intent(this, SwipeProblem.class);
//        String value = "CNC Machine";
//        activityIntent.putExtra("key", value);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setFullScreenIntent(contentIntent, true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Repair", contentIntent)
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
            sendOnChannel1();
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
                ZoneId zoneId = ZoneId.of( "Indonesia" ) ;  // Or ZoneOffset.UTC or ZoneId.systemDefault()
                LocalDate today = LocalDate.now( zoneId ) ;
                String output = today.toString();

                if(output == notifdata && notifdata != "None"){
                    try {
                        Thread.sleep(500);
                        Toast.makeText(NotificationManager.this, output, Toast.LENGTH_SHORT).show();
                        Toast.makeText(NotificationManager.this, notifdata, Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        // We were cancelled; stop sleeping!
                    }
                }
            }

            @Override
            public void onFailure(Call<AssetResponseData> call, Throwable t) {
                Toast.makeText(NotificationManager.this, "Failed To Get Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }
}
