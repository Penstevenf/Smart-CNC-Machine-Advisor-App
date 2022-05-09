package com.example.winteq.adapter.sensor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.winteq.R;
import com.example.winteq.Sensor;
import com.example.winteq.model.asset.AssetData;

import java.util.ArrayList;
import java.util.List;

public class AdapterSensorMachine extends BaseAdapter implements Filterable {

    private Context context;
    private List<AssetData> listSensor;
    private List<AssetData> listSensorFiltered;
    private String line, station, machine;
    AssetData assetData;

    public AdapterSensorMachine(Context context, List<AssetData> listSensor) {
        this.context = context;
        this.listSensor = listSensor;
        this.listSensorFiltered = listSensor;
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

        final AssetData AD = (AssetData) this.getItem(position);

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_sensor_machine, parent, false);
        }

        TextView id;
        String name, no;
        id = itemView.findViewById(R.id.tv_asset_id);

        ViewHolder viewHolder = new ViewHolder(itemView);

        AssetData ad = (AssetData) getItem(position);
        viewHolder.bind(ad);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                machine = AD.getMachine_name();
                line = AD.getAsset_line();
                station = AD.getAsset_station();
//                Toast.makeText(context, machine+" Machine Selected", Toast.LENGTH_SHORT).show();
                Intent m = new Intent(context, Sensor.class);
                m.putExtra("xLine", line);
                m.putExtra("xStation", station);
                m.putExtra("xMachine", machine);
                context.startActivity(m);
            }
        });

        return itemView;
    }

    private class ViewHolder {
        private TextView tv_asset_id, tv_machname;

        ViewHolder (View v){
//            tv_asset_id = v.findViewById(R.id.tv_asset_id);
            tv_machname = v.findViewById(R.id.tv_machname);
        }

        void bind(AssetData sd){
//            tv_asset_id.setText(ad.getAsset_id());
            tv_machname.setText(sd.getMachine_name());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listSensorFiltered = listSensor;
                }else{
                    List<AssetData> listFiltered = new ArrayList<>();
                    for (AssetData row: listSensor){
                        if(row.getMachine_name().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listSensorFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listSensorFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listSensorFiltered = (List<AssetData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
