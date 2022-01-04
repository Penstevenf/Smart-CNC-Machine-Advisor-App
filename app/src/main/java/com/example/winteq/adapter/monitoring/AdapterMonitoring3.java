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

public class AdapterMonitoring3 extends BaseAdapter {
    private Context context;
    private List<MonData> listMon3;
    private String idMon3;
    MonData monData;

    public AdapterMonitoring3(Context context, List<MonData> listMon3) {
        this.context = context;
        this.listMon3 = listMon3;
    }

    @Override
    public int getCount() {
        return listMon3.size();
    }


    @Override
    public Object getItem(int position) {
        return listMon3.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_layout_mon3, parent, false);
        }

        final MonData md3 = (MonData) this.getItem(position);

        TextView id;
        ImageView iv_station3;
        CardView rline3;
        ConstraintLayout c_linet3;
        id = itemView.findViewById(R.id.tv_mon3_id);
        idMon3 = id.getText().toString();
        iv_station3 = itemView.findViewById(R.id.iv_station3);
        c_linet3 = itemView.findViewById(R.id.c_linet3);
        rline3 = itemView.findViewById(R.id.rline3);

        if(md3.getMon_image() != null && md3.getMon_image().length()>0){
            Picasso.get().load(md3.getMon_image()).into(iv_station3);
        }else{
            Picasso.get().load(R.drawable.machine).into(iv_station3);
        }

//        if(md3.getMon_status().equals("Working")) {
//            c_linet3.setBackgroundResource(R.drawable.linetgreen);
//        }
//        else if(md3.getMon_status().equals("Repairing")) {
//            c_linet3.setBackgroundResource(R.drawable.linetyellow);
//        }
//        else if(md3.getMon_status().equals("Finish Repair")) {
//            c_linet3.setBackgroundResource(R.drawable.linetblue);
//        }
//        else if(md3.getMon_status().equals("Breakdown")) {
//            c_linet3.setBackgroundResource(R.drawable.linetred);
//        }

        if(md3.getMon_status().equals("Working")) {
            rline3.setBackgroundColor(Color.GREEN);
        }
        else if(md3.getMon_status().equals("Repairing")) {
            rline3.setBackgroundColor(Color.YELLOW);
        }
        else if(md3.getMon_status().equals("Finish Repair")) {
            rline3.setBackgroundColor(Color.BLUE);
        }
        else if(md3.getMon_status().equals("Breakdown")) {
            rline3.setBackgroundColor(Color.RED);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        MonData md = (MonData) getItem(position);
        viewHolder.bind(md);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idMon3 = id.getText().toString();
                viewData();
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                idMon3 = id.getText().toString();
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.onlongmenu, popup.getMenu());
                popup.show();popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.delete:
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Are you sure you want to delete this Station "+md3.getStation()+" on "+md3.getLine()+" data?")
                                        .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                                        .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                                                Call<MonData> deleteData = aiData.mondeleteData(idMon3);

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
                                                        ((Monitoring)context).gridview3();
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
//                                Toast.makeText(context,idMon3, Toast.LENGTH_SHORT).show();
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
        private TextView station3, tv_mon3_id;

        ViewHolder (View v){
            station3 = v.findViewById(R.id.station3);
            tv_mon3_id = v.findViewById(R.id.tv_mon3_id);
        }

        void bind(MonData md){
            station3.setText(md.getStation());
            tv_mon3_id.setText(md.getMon_id());
        }
    }

    private void deleteData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonData> deleteData = aiData.mondeleteData(idMon3);

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
    }

    private void viewData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<MonResponseData> getData = aiData.monviewData(idMon3);

        getData.enqueue(new Callback<MonResponseData>() {
            @Override
            public void onResponse(Call<MonResponseData> call, Response<MonResponseData> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listMon3 = response.body().getData();

                    String Vmon_id = listMon3.get(0).getMon_id();
                    String Vmon_no = listMon3.get(0).getMon_no();
                    String Vmon_date = listMon3.get(0).getMon_date();
                    String Vplant = listMon3.get(0).getPlant();
                    String Vline = listMon3.get(0).getLine();
                    String Vstation = listMon3.get(0).getStation();
                    String Vmon_status = listMon3.get(0).getMon_status();
                    String Vmon_pic = listMon3.get(0).getMon_pic();
                    String Vmon_image = listMon3.get(0).getMon_image();
                    String Vmon_desc = listMon3.get(0).getMon_desc();
                    String Vparameter1 = listMon3.get(0).getParameter1();
                    String Vparameter2 = listMon3.get(0).getParameter2();
                    String Vparameter3 = listMon3.get(0).getParameter3();
                    String Vparameter4 = listMon3.get(0).getParameter4();

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

