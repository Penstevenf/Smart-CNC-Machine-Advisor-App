package com.example.winteq.adapter.asset;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.winteq.AssetManagementView;
import com.example.winteq.R;
import com.example.winteq.model.asset.AssetData;

import java.util.ArrayList;
import java.util.List;

public class AdapterAssetMachine extends BaseAdapter implements Filterable {

    private Context context;
    private List<AssetData> listAsset;
    private List<AssetData> listAssetFiltered;
    private String idAsset, machine;
    AssetData assetData;

    public AdapterAssetMachine(Context context, List<AssetData> listAsset) {
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
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_asset_machine, parent, false);
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
                Toast.makeText(context, machine+" Machine Selected", Toast.LENGTH_SHORT).show();
                Intent nameSendm = new Intent(context, AssetManagementView.class);
                nameSendm.putExtra("xMachine", machine);
                context.startActivity(nameSendm);
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

        void bind(AssetData ad){
//            tv_asset_id.setText(ad.getAsset_id());
            tv_machname.setText(ad.getMachine_name());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listAssetFiltered = listAsset;
                }else{
                    List<AssetData> listFiltered = new ArrayList<>();
                    for (AssetData row: listAsset){
                        if(row.getMachine_name().toLowerCase().contains(Key.toLowerCase())){
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

}
