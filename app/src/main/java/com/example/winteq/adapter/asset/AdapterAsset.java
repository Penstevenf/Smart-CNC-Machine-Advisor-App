package com.example.winteq.adapter.asset;

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

import com.example.winteq.AssetManagement;
import com.example.winteq.AssetManagementStation;
import com.example.winteq.R;
import com.example.winteq.model.asset.AssetData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterAsset extends BaseAdapter implements Filterable {

    private Context context;
    private List<AssetData> listAsset;
    private List<AssetData> listAssetFiltered;
    private String idAsset, nameAsset, noAsset, line;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;
    AssetData assetData;

    public AdapterAsset(Context context, List<AssetData> listAsset) {
        this.context = context;
        this.listAsset = listAsset;
        this.listAssetFiltered = listAsset;
    }

    @Override
    public int getCount() {
        return listAssetFiltered.size();
    }


    @Override
    public Object getItem(int position) {
        return listAssetFiltered.get(position);
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
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_asset, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        AssetData ad = (AssetData) getItem(position);
        viewHolder.bind(ad);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line = AD.getAsset_line();
                Toast.makeText(context, line+" Selected", Toast.LENGTH_SHORT).show();
                Intent nameSend = new Intent(context, AssetManagementStation.class);
                nameSend.putExtra("xLine", line);
                context.startActivity(nameSend);
            }
        });

        return itemView;
    }

    private class ViewHolder {
        private TextView tv_asset_id, tv_asset_line;

        ViewHolder (View v){
//            tv_asset_id = v.findViewById(R.id.tv_asset_id);
            tv_asset_line = v.findViewById(R.id.tv_asset_line);
        }

        void bind(AssetData ad){
//            tv_asset_id.setText(ad.getAsset_id());
            tv_asset_line.setText(ad.getAsset_line());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Spinner sp_category;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listAssetFiltered = listAsset;
                    sp_category = ((AssetManagement)context).findViewById(R.id.statsp);
                    sp_category.setSelection(0);
                }else{
                    List<AssetData> listFiltered = new ArrayList<>();
                    for (AssetData row: listAsset){
                        if(row.getAsset_name().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listAssetFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listAssetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listAssetFiltered = (List<AssetData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilterLine() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if(Key.isEmpty() || Key.contains("All Line")){
                    listAssetFiltered = listAsset;
                }else{
                    List<AssetData> listFilteredss = new ArrayList<>();
                    for (AssetData row: listAsset){
                        if(row.getAsset_line().toLowerCase().contains(Key.toLowerCase())){
                            listFilteredss.add(row);
                        }
                    }

                    listAssetFiltered = listFilteredss;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listAssetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listAssetFiltered = (List<AssetData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
