package com.example.winteq.adapter.monitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.winteq.Monitoring;
import com.example.winteq.MonitoringView;
import com.example.winteq.R;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.monitoring.MonData;
import com.example.winteq.model.monitoring.MonResponseData;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMonitoring2 extends BaseAdapter {
    private Context context;
    private List<MonData> listMon2;
    private String idMon2;
    MonData monData;

    public AdapterMonitoring2(Context context, List<MonData> listMon2) {
        this.context = context;
        this.listMon2 = listMon2;
    }

    @Override
    public int getCount() {
        return listMon2.size();
    }


    @Override
    public Object getItem(int position) {
        return listMon2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_layout_mon2, parent, false);
        }

        MonData md2 = (MonData) this.getItem(position);

        TextView id;
        ImageView iv_station2;
        CardView rline2;
        ConstraintLayout c_linet2;
        id = itemView.findViewById(R.id.tv_mon2_id);
        idMon2 = id.getText().toString();
        iv_station2 = itemView.findViewById(R.id.iv_station2);
        c_linet2 = itemView.findViewById(R.id.c_linet2);
        rline2 = itemView.findViewById(R.id.rline2);

        if(md2.getMon_image() != null && md2.getMon_image().length()>0){
            Picasso.get().load(md2.getMon_image()).into(iv_station2);
        }else{
            Picasso.get().load(R.drawable.machine).into(iv_station2);
        }

//        if(md2.getMon_status().equals("Working")) {
//            c_linet2.setBackgroundResource(R.drawable.linetgreen);
//        }
//        else if(md2.getMon_status().equals("Repairing")) {
//            c_linet2.setBackgroundResource(R.drawable.linetyellow);
//        }
//        else if(md2.getMon_status().equals("Ready To Use")) {
//            c_linet2.setBackgroundResource(R.drawable.linetblue);
//        }
//        else if(md2.getMon_status().equals("Breakdown")) {
//            c_linet2.setBackgroundResource(R.drawable.linetred);
//        }

        if(md2.getMon_status().equals("Working")) {
            rline2.setBackgroundColor(Color.GREEN);
        }
        else if(md2.getMon_status().equals("Repairing")) {
            rline2.setBackgroundColor(Color.YELLOW);
        }
        else if(md2.getMon_status().equals("Ready To Use")) {
            rline2.setBackgroundColor(Color.BLUE);
        }
        else if(md2.getMon_status().equals("Breakdown")) {
            rline2.setBackgroundColor(Color.RED);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        MonData md = (MonData) getItem(position);
        viewHolder.bind(md);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idMon2 = id.getText().toString();
                viewData();
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                idMon2 = id.getText().toString();
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.onlongmenu, popup.getMenu());
                popup.show();popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.delete:
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Are you sure you want to delete this Station "+md2.getStation()+" on "+md2.getLine()+" data?")
                                        .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                                        .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                                                Call<MonData> deleteData = aiData.mondeleteData(idMon2, md2.getStation(), md2.getLine());

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
                                                            Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<MonData> call, Throwable t) {
                                                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                Handler handler = new android.os.Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((Monitoring)context).gridview2();
                                                    }
                                                }, 500);
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
                                break;

//                            case R.id.edit:
//                                Toast.makeText(context,idMon2, Toast.LENGTH_SHORT).show();
////                                Intent intent = new Intent(context, MonitoringUpdate.class);
////                                context.startActivity(intent);
//                                break;
                        }
                        return false;
                    }
                });
                return true;
            }
        });

        return itemView;
    }

    private class ViewHolder {
        private TextView station2, tv_mon2_id;

        ViewHolder (View v){
            station2 = v.findViewById(R.id.station2);
            tv_mon2_id = v.findViewById(R.id.tv_mon2_id);
        }

        void bind(MonData md){
            station2.setText(md.getStation());
            tv_mon2_id.setText(md.getMon_id());
        }
    }

    private void viewData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> getData = aiData.monviewData(idMon2);

        getData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listMon2 = response.body().getData();

                    String Vmon_id = listMon2.get(0).getMon_id();
                    String Vmon_no = listMon2.get(0).getMon_no();
                    String Vmon_date = listMon2.get(0).getMon_date();
                    String Vplant = listMon2.get(0).getPlant();
                    String Vline = listMon2.get(0).getLine();
                    String Vstation = listMon2.get(0).getStation();
                    String Vmon_status = listMon2.get(0).getMon_status();
                    String Vmon_pic = listMon2.get(0).getMon_pic();
                    String Vmon_image = listMon2.get(0).getMon_image();
                    String Vmon_desc = listMon2.get(0).getMon_desc();
                    String Vparameter1 = listMon2.get(0).getParameter1();
                    String Vparameter2 = listMon2.get(0).getParameter2();
                    String Vparameter3 = listMon2.get(0).getParameter3();
                    String Vparameter4 = listMon2.get(0).getParameter4();

//                    Toast.makeText(context, "Status: "+status+" | Message: "+message+ " | Data : "+varImage, Toast.LENGTH_SHORT).show();

                    Intent send = new Intent(context, MonitoringView.class);
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

                    context.startActivity(send);
                }
            }
            @Override
            public void onFailure(Call<MonResponseData> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

