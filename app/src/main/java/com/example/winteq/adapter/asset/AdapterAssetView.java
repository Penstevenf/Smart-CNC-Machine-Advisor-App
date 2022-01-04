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

import com.example.winteq.AssetManagementReplace;
import com.example.winteq.AssetManagementView;
import com.example.winteq.R;
import com.example.winteq.model.asset.AssetData;

import java.util.ArrayList;
import java.util.List;

public class AdapterAssetView extends BaseAdapter implements Filterable {
    private Context context;
    private List<AssetData> listAsset;
    private List<AssetData> listAssetFiltered;
    private String idAsset, machine;
    AssetData assetData;

    public AdapterAssetView(Context context, List<AssetData> listAsset) {
        this.context = context;
        this.listAsset = listAsset;
        this.listAssetFiltered = listAsset;
    }

    public AdapterAssetView(AssetManagementView context) {
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

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_asset_view, parent, false);
        }

        final AssetData ad1 = (AssetData) this.getItem(position);

        TextView id;
        id = itemView.findViewById(R.id.tv_asset_id);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                idAsset = id.getText().toString();
                Toast.makeText(context, ad1.getAsset_part()+" Part Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AssetManagementReplace.class);
                context.startActivity(intent);
            }
        });

        ViewHolder viewHolder = new ViewHolder(itemView);

        AssetData ad = (AssetData) getItem(position);
        viewHolder.bind(ad);
        return itemView;
    }

    public Filter getFilterCategory() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if(Key.isEmpty() || Key.contains("All Category")){
                    listAssetFiltered = listAsset;
                }else{
                    List<AssetData> listFiltered = new ArrayList<>();
                    for (AssetData row: listAsset){
                        if(row.getAsset_category().toLowerCase().contains(Key.toLowerCase())){
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Spinner sp_category;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listAssetFiltered = listAsset;
                    sp_category = ((AssetManagementView)context).findViewById(R.id.asset_category);
                    sp_category.setSelection(0);
                }else{
                    List<AssetData> listFiltereds = new ArrayList<>();
                    for (AssetData row: listAssetFiltered){
                        if(row.getAsset_part().toLowerCase().contains(Key.toLowerCase())){
                            listFiltereds.add(row);
                        }
                    }

                    listAssetFiltered = listFiltereds;
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

    private class ViewHolder {
        private TextView tv_assetv_id, asset_category, asset_part, asset_qty, lifetime, date_regis, asset_replace;

        ViewHolder (View v){
            tv_assetv_id = v.findViewById(R.id.tv_assetv_id);
            asset_category = v.findViewById(R.id.asset_category);
            asset_part = v.findViewById(R.id.asset_part);
            asset_qty = v.findViewById(R.id.asset_qty);
            lifetime = v.findViewById(R.id.lifetime);
            date_regis = v.findViewById(R.id.date_regis);
            asset_replace = v.findViewById(R.id.asset_replace);
        }

        void bind(AssetData ad){
            tv_assetv_id.setText(ad.getAsset_id());
            asset_category.setText(ad.getAsset_category());
            asset_part.setText(ad.getAsset_part());
            asset_qty.setText(ad.getAsset_qty());
            lifetime.setText(ad.getLifetime());
            date_regis.setText(ad.getDate_register());
            asset_replace.setText(ad.getDate_replace());
        }
    }
}
