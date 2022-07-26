package com.example.winteq.adapter.sensor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.winteq.DetailError;
import com.example.winteq.R;
import com.example.winteq.Sensor;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.sensor.SensorData;
import com.example.winteq.model.sensor.SensorResponseData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSensor extends BaseAdapter implements Filterable {
    private Context context;
    private List<SensorData> listSensor;
    private List<SensorData> listSensorFiltered;
    private String idAsset, machine;
    SensorData sensorData;

    public AdapterSensor(Context context, List<SensorData> listSensor) {
        this.context = context;
        this.listSensor = listSensor;
        this.listSensorFiltered = listSensor;
    }

    public AdapterSensor(Sensor context) {
    }

    @Override
    public int getCount() {
        return listSensorFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listSensorFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_sensor, parent, false);
        }

        final SensorData sd1 = (SensorData) this.getItem(position);

        TextView id, error_percentage;
        id = itemView.findViewById(R.id.sen_id);
        error_percentage = itemView.findViewById(R.id.error_percentage);

        //convert error to percentage
        String error = sd1.getError_percentage();
        Float value = Float.parseFloat(error);
        float etotal = Float.parseFloat(error);
        float total = etotal * 100;
        String errortotal = Float.toString(total) + "%";
        error_percentage.setText(errortotal);

//        Toast.makeText(context, "Data : "+value, Toast.LENGTH_SHORT).show();

        if(value >= 0 && value <= 0.101) {
            error_percentage.setBackground(ContextCompat.getDrawable(context, R.drawable.linetgreenforsensor));
        }
        else if(value > 0.10 && value <= 0.201) {
            error_percentage.setBackground(ContextCompat.getDrawable(context, R.drawable.linetyellowforsensor));
        }
        else if(value > 0.20) {
            error_percentage.setBackground(ContextCompat.getDrawable(context, R.drawable.linetredforsensor));
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        SensorData sd = (SensorData) getItem(position);
        viewHolder.bind(sd);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<SensorResponseData> getData = aiData.aiSensorViewData(sd1.getSen_id());

                getData.enqueue(new Callback<SensorResponseData>() {
                    @Override
                    public void onResponse(Call<SensorResponseData> call, Response<SensorResponseData> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listSensor = response.body().getData();

                            String varId = listSensor.get(0).getSen_id();
                            String varStatus = listSensor.get(0).getSen_status();
                            String varCutSpeed = listSensor.get(0).getCut_speed();
                            String varRpm = listSensor.get(0).getRpm();
                            String varFeed = listSensor.get(0).getFeed();
                            String varCutTime = listSensor.get(0).getCut_time();
                            String varError = listSensor.get(0).getError_percentage();
                            String varErrorSpeed = listSensor.get(0).getError_cut_speed();
                            String varErrorFeed = listSensor.get(0).getError_feed();
                            String varErrorRpm = listSensor.get(0).getError_rpm();
                            String varErrorTime = listSensor.get(0).getError_cut_time();
                            String varDate = listSensor.get(0).getSen_date();
                            String varLine = listSensor.get(0).getSen_line();
                            String varStation = listSensor.get(0).getSen_station();
                            String varMachine = listSensor.get(0).getSen_machine();

//                          Toast.makeText(context, "Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendSP = new Intent(context, DetailError.class);
                            sendSP.putExtra("xId", varId);
                            sendSP.putExtra("xStatus", varStatus);
                            sendSP.putExtra("xCutSpeed", varCutSpeed);
                            sendSP.putExtra("xRpm", varRpm);
                            sendSP.putExtra("xFeed", varFeed);
                            sendSP.putExtra("xCutTime", varCutTime);
                            sendSP.putExtra("xError", varError);
                            sendSP.putExtra("xErrorSpeed", varErrorSpeed);
                            sendSP.putExtra("xErrorFeed", varErrorFeed);
                            sendSP.putExtra("xErrorRpm", varErrorRpm);
                            sendSP.putExtra("xErrorTime", varErrorTime);
                            sendSP.putExtra("xDate", varDate);
                            sendSP.putExtra("xLine", varLine);
                            sendSP.putExtra("xStation", varStation);
                            sendSP.putExtra("xMachine", varMachine);
                            context.startActivity(sendSP);
                        }
                    }
                    @Override
                    public void onFailure(Call<SensorResponseData> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return itemView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Spinner sp_categoryz;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listSensorFiltered = listSensor;
                }else{
                    List<SensorData> listFiltereds = new ArrayList<>();
                    for (SensorData row: listSensorFiltered){
                        if(row.getSen_status().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getError_percentage().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getSen_date().toLowerCase().contains(Key.toLowerCase())){
                            listFiltereds.add(row);
                        }
                    }

                    listSensorFiltered = listFiltereds;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listSensorFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listSensorFiltered = (List<SensorData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        private TextView sen_id, sen_status, sen_cut_speed, sen_rpm, sen_feed, sen_cut_time, error_percentage, sen_date;

        ViewHolder (View v){
            sen_id = v.findViewById(R.id.sen_id);
            sen_status = v.findViewById(R.id.sen_status);
            sen_cut_speed = v.findViewById(R.id.sen_cut_speed);
            sen_rpm = v.findViewById(R.id.sen_rpm);
            sen_feed = v.findViewById(R.id.sen_feed);
            sen_cut_time = v.findViewById(R.id.sen_cut_time);
            error_percentage = v.findViewById(R.id.error_percentage);
            sen_date = v.findViewById(R.id.sen_date);
        }

        void bind(SensorData sd){
            sen_id.setText(sd.getSen_id());
            sen_status.setText(sd.getSen_status());
            sen_cut_speed.setText(sd.getCut_speed());
            sen_rpm.setText(sd.getRpm());
            sen_feed.setText(sd.getFeed());
            sen_cut_time.setText(sd.getCut_time());
            sen_date.setText(sd.getSen_date());
        }
    }
}
