package com.example.winteq.adapter.wms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.winteq.R;
import com.example.winteq.WMS;
import com.example.winteq.WMSUpdate;
import com.example.winteq.WMSView;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.wms.WmsData;
import com.example.winteq.model.wms.WmsResponseData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends BaseAdapter implements Filterable {
    private Context context;
    private List<WmsData> listWms;
    private List<WmsData> listWmsFiltered;
    private List<WmsData> listGetWms;
    private String idWms, itemwms, dialog_image, dialog_description, dialog_item;
    WmsData wmsData;

    public AdapterData(Context context, List<WmsData> listWms) {
        this.context = context;
        this.listWms = listWms;
        this.listWmsFiltered = listWms;
    }

    public AdapterData(WMS context) {
    }

    @Override
    public int getCount() {
        return listWmsFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listWmsFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if(itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_layout_wms, parent, false);
        }

        WmsData wd1 = (WmsData) this.getItem(position);

        TextView id, item;
        id = itemView.findViewById(R.id.tv_id);
        item = itemView.findViewById(R.id.itemname);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idWms = id.getText().toString();
                viewData();
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemwms = item.getText().toString();
                //untuk menampilkan dialog
                AlertDialog.Builder messageDialog = new AlertDialog.Builder(context);
                messageDialog.setTitle("Please Choose Operation for "+itemwms+" data")
                        .setMessage("Do you want to View Data, Edit Data or Permanently Delete Data, Delete Data cannot be undone.");
                messageDialog.setCancelable(true);

                idWms = id.getText().toString();

                messageDialog.setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData();
                        dialogInterface.dismiss();
                    }
                });

                messageDialog.setNegativeButton("Edit Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editData();
                        dialogInterface.dismiss();
                    }
                });
                
                messageDialog.setNeutralButton("View Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idWms = id.getText().toString();
                        viewData();
                    }
                });

                messageDialog.show();

                return false;
            }
        });

        ViewHolder viewHolder = new ViewHolder(itemView);

        WmsData wd = (WmsData) getItem(position);
        viewHolder.bind(wd);

        return itemView;

    }

    private void deleteData(){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Are you sure you want to delete this "+itemwms+" data?")
                    .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                    .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                            Call<WmsData> deleteData = aiData.wmsdeleteData(idWms);

                            deleteData.enqueue(new Callback<WmsData>() {
                                public void onResponse(Call<WmsData> call, Response<WmsData> response) {
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
                                        wmsData = response.body();
                                        Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<WmsData> call, Throwable t) {
                                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Handler handler = new android.os.Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((WMS)context).retrieveData();
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
        }

    private void viewData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<WmsResponseData> getData = aiData.aiGetData(idWms);

        getData.enqueue(new Callback<WmsResponseData>() {
            @Override
            public void onResponse(Call<WmsResponseData> call, Response<WmsResponseData> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listGetWms = response.body().getData();

                    String varIdWms = listGetWms.get(0).getWms_id();
                    String varNoTag = listGetWms.get(0).getNo_tag();
                    String varDate = listGetWms.get(0).getDate();
                    String varItemName = listGetWms.get(0).getItem_name();
                    String varType = listGetWms.get(0).getType();
                    String varLifetime = listGetWms.get(0).getLifetime_wms();
                    String varQty = listGetWms.get(0).getQty();
                    String varCategory = listGetWms.get(0).getCategory();
                    String varCopro = listGetWms.get(0).getCopro();
                    String varArea = listGetWms.get(0).getArea();
                    String varCabinet = listGetWms.get(0).getCabinet();
                    String varShelf = listGetWms.get(0).getShelf();
                    String varImage = listGetWms.get(0).getImage();
                    String varDescription = listGetWms.get(0).getDescription();

//                    Toast.makeText(context, "Status: "+status+" | Message: "+message+ " | Data : "+varImage, Toast.LENGTH_SHORT).show();

                    Intent send = new Intent(context, WMSView.class);
                    send.putExtra("xId", varIdWms);
                    send.putExtra("xItem", varItemName);
                    send.putExtra("xType", varType);
                    send.putExtra("xLifetime", varLifetime);
                    send.putExtra("xQty", varQty);
                    send.putExtra("xCategory", varCategory);
                    send.putExtra("xCopro", varCopro);
                    send.putExtra("xArea", varArea);
                    send.putExtra("xCabinet", varCabinet);
                    send.putExtra("xShelf", varShelf);
                    send.putExtra("xDescription", varDescription);
                    send.putExtra("xImage", varImage);
                    send.putExtra("xDate", varDate);
                    send.putExtra("xTag", varNoTag);

                    context.startActivity(send);
                }
            }
            @Override
            public void onFailure(Call<WmsResponseData> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<WmsData> popupData = aiData.wmspopupData(idWms);

        popupData.enqueue(new Callback<WmsData>() {
            public void onResponse(Call<WmsData> call, Response<WmsData> response) {
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
                    wmsData = response.body();
                    dialog_image = response.body().getImage();
                    dialog_item = response.body().getItem_name();
                    dialog_description = response.body().getDescription();
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WmsData> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog(context, R.style.DialogStyle);
                TextView title, description;
                dialog.setContentView(R.layout.layout_custom_dialog_itemlist);
                title = dialog.findViewById(R.id.dialog_item);
                description = dialog.findViewById(R.id.dialog_description);

                String imageUri = dialog_image;
                ImageView Image1 = dialog.findViewById(R.id.dialog_image);
                Picasso.get().load(imageUri).into(Image1);

                title.setText(dialog_item);
                description.setText(dialog_description);

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

                ImageView btnClose = dialog.findViewById(R.id.btn_close);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }, 500);
    }

    private void editData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<WmsResponseData> getData = aiData.aiGetData(idWms);

        getData.enqueue(new Callback<WmsResponseData>() {
            @Override
            public void onResponse(Call<WmsResponseData> call, Response<WmsResponseData> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listGetWms = response.body().getData();

                    String varIdWms = listGetWms.get(0).getWms_id();
                    String varNoTag = listGetWms.get(0).getNo_tag();
                    String varDate = listGetWms.get(0).getDate();
                    String varItemName = listGetWms.get(0).getItem_name();
                    String varType = listGetWms.get(0).getType();
                    String varLifetime = listGetWms.get(0).getLifetime_wms();
                    String varQty = listGetWms.get(0).getQty();
                    String varCategory = listGetWms.get(0).getCategory();
                    String varCopro = listGetWms.get(0).getCopro();
                    String varArea = listGetWms.get(0).getArea();
                    String varCabinet = listGetWms.get(0).getCabinet();
                    String varShelf = listGetWms.get(0).getShelf();
                    String varImage = listGetWms.get(0).getImage();
                    String varDescription = listGetWms.get(0).getDescription();

//                    Toast.makeText(context, "Status: "+status+" | Message: "+message+ " | Data : "+varImage, Toast.LENGTH_SHORT).show();

                    Intent send = new Intent(context, WMSUpdate.class);
                    send.putExtra("xId", varIdWms);
                    send.putExtra("xItem", varItemName);
                    send.putExtra("xType", varType);
                    send.putExtra("xLifetime", varLifetime);
                    send.putExtra("xQty", varQty);
                    send.putExtra("xCategory", varCategory);
                    send.putExtra("xCopro", varCopro);
                    send.putExtra("xArea", varArea);
                    send.putExtra("xCabinet", varCabinet);
                    send.putExtra("xShelf", varShelf);
                    send.putExtra("xDescription", varDescription);
                    send.putExtra("xImage", varImage);
                    send.putExtra("xDate", varDate);
                    send.putExtra("xTag", varNoTag);

                    context.startActivity(send);
                }
            }
            @Override
            public void onFailure(Call<WmsResponseData> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Filter getFilterCategory() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key = charSequence.toString();
                if(Key.isEmpty() || Key.contains("All Category")){
                    listWmsFiltered = listWms;
                }else{
                    List<WmsData> listFiltered = new ArrayList<>();
                    for (WmsData row: listWms){
                        if(row.getCategory().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listWmsFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listWmsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listWmsFiltered = (List<WmsData>) filterResults.values;
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
                    listWmsFiltered = listWms;
                    sp_category = ((WMS)context).findViewById(R.id.sp_category);
                    sp_category.setSelection(0);
                }else{
                    List<WmsData> listFiltereds = new ArrayList<>();
                    for (WmsData row: listWmsFiltered){
                        if(row.getItem_name().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getType().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getNo_tag().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getArea().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getCopro().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getCabinet().toLowerCase().contains(Key.toLowerCase()) ||
                                row.getShelf().toLowerCase().contains(Key.toLowerCase())){
                            listFiltereds.add(row);
                        }
                    }

                    listWmsFiltered = listFiltereds;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listWmsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listWmsFiltered = (List<WmsData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        private TextView id, tag, date, itemname, type, lifetime, qty, category,copro,area,cabinet,shelf;

        ViewHolder (View v){
            id = v.findViewById(R.id.tv_id);
            tag = v.findViewById(R.id.tag);
            date = v.findViewById(R.id.date);
            itemname = v.findViewById(R.id.itemname);
            type = v.findViewById(R.id.type);
            lifetime = v.findViewById(R.id.lifetime);
            qty = v.findViewById(R.id.qty);
            category = v.findViewById(R.id.category);
            copro = v.findViewById(R.id.copro);
            area = v.findViewById(R.id.area);
            cabinet = v.findViewById(R.id.cabinet);
            shelf = v.findViewById(R.id.shelf);
        }

        void bind(WmsData wd){
            id.setText(wd.getWms_id());
            tag.setText(wd.getNo_tag());
            date.setText(wd.getDate());
            itemname.setText(wd.getItem_name());
            type.setText(wd.getType());
            lifetime.setText(wd.getLifetime_wms());
            qty.setText(wd.getQty());
            category.setText(wd.getCategory());
            copro.setText(wd.getCopro());
            area.setText(wd.getArea());
            cabinet.setText(wd.getCabinet());
            shelf.setText(wd.getShelf());
        }

    }

}
