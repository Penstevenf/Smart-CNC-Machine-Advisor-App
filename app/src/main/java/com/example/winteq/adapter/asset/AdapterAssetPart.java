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

import com.example.winteq.AssetManagementNotifPart;
import com.example.winteq.AssetManagementReplace;
import com.example.winteq.R;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.asset.AssetData;
import com.example.winteq.model.asset.AssetResponseData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAssetPart extends BaseAdapter implements Filterable {
    private Context context;
    private List<AssetData> listAsset;
    private List<AssetData> listAssetFiltered;
    private String idAsset, machine;
    AssetData assetData;

    public AdapterAssetPart(Context context, List<AssetData> listAsset) {
        this.context = context;
        this.listAsset = listAsset;
        this.listAssetFiltered = listAsset;
    }

    public AdapterAssetPart(AssetManagementNotifPart context) {
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
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_asset_notif_part, parent, false);
        }

        final AssetData ad1 = (AssetData) this.getItem(position);

        TextView id;
        id = itemView.findViewById(R.id.tv_asset_id);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<AssetResponseData> getData = aiData.aiAssetReplaceData(ad1.getAsset_id());

                getData.enqueue(new Callback<AssetResponseData>() {
                    @Override
                    public void onResponse(Call<AssetResponseData> call, Response<AssetResponseData> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listAsset = response.body().getData();

                            String varId = listAsset.get(0).getAsset_id();
                            String varCategory = listAsset.get(0).getAsset_category();
                            String varPart = listAsset.get(0).getAsset_part();
                            String varLine = listAsset.get(0).getAsset_line();
                            String varStation = listAsset.get(0).getAsset_station();
                            String varQty = listAsset.get(0).getAsset_qty();
                            String varMachine = listAsset.get(0).getMachine_name();
                            String varLifetime = listAsset.get(0).getLifetime();
                            String varRegister = listAsset.get(0).getDate_register();
                            String varReplace = listAsset.get(0).getDate_replace();
                            String varUpdate = listAsset.get(0).getDate_update();
                            String varUnit = listAsset.get(0).getAsset_unit();

//                          Toast.makeText(context, "Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendAP = new Intent(context, AssetManagementReplace.class);
                            sendAP.putExtra("xId", varId);
                            sendAP.putExtra("xCategory", varCategory);
                            sendAP.putExtra("xPart", varPart);
                            sendAP.putExtra("xLine", varLine);
                            sendAP.putExtra("xStation", varStation);
                            sendAP.putExtra("xQty", varQty);
                            sendAP.putExtra("xMachine", varMachine);
                            sendAP.putExtra("xLifetime", varLifetime);
                            sendAP.putExtra("xRegister", varRegister);
                            sendAP.putExtra("xReplace", varReplace);
                            sendAP.putExtra("xUpdate", varUpdate);
                            sendAP.putExtra("xUnit", varUnit);
                            context.startActivity(sendAP);
                        }
                    }
                    @Override
                    public void onFailure(Call<AssetResponseData> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
                Spinner sp_categoryz;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listAssetFiltered = listAsset;
                    sp_categoryz = ((AssetManagementNotifPart)context).findViewById(R.id.asset_categoryz);
                    sp_categoryz.setSelection(0);
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
            tv_assetv_id = v.findViewById(R.id.tv_assetv_idz);
            asset_category = v.findViewById(R.id.asset_categoryz);
            asset_part = v.findViewById(R.id.asset_partz);
            asset_qty = v.findViewById(R.id.asset_qtyz);
            lifetime = v.findViewById(R.id.lifetimez);
            date_regis = v.findViewById(R.id.date_regisz);
            asset_replace = v.findViewById(R.id.asset_replacez);
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
