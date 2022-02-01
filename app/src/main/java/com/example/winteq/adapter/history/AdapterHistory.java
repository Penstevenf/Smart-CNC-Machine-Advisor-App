package com.example.winteq.adapter.history;

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

import com.example.winteq.History;
import com.example.winteq.HistoryView;
import com.example.winteq.R;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.history.HistoryData;
import com.example.winteq.model.history.HistoryResponseData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHistory extends BaseAdapter implements Filterable {

    private Context context;
    private List<HistoryData> listHistory;
    private List<HistoryData> listHistoryFiltered;
    private String idAsset, nameAsset, noAsset, line;

    public AdapterHistory(Context context, List<HistoryData> listHistory) {
        this.context = context;
        this.listHistory = listHistory;
        this.listHistoryFiltered = listHistory;
    }

    public AdapterHistory(History context) {
    }

    @Override
    public int getCount() {
        return listHistoryFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listHistoryFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        final HistoryData HD = (HistoryData) this.getItem(position);

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_layout_hs, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        HistoryData hd = (HistoryData) getItem(position);
        viewHolder.bind(hd);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<HistoryResponseData> showData = aiData.aiHistoryData();

                showData.enqueue(new Callback<HistoryResponseData>() {
                    @Override
                    public void onResponse(Call<HistoryResponseData> call, Response<HistoryResponseData> response) {
                        boolean status = response.body().isStatus();
                        String message = response.body().getMessage();

                        listHistory = response.body().getData();

                        if(response.body() != null && response.body().isStatus()) {

                            String varId = listHistory.get(0).getId_hist();
                            String varLine = listHistory.get(0).getLine_hist();
                            String varStation = listHistory.get(0).getStation_hist();
                            String varMachine = listHistory.get(0).getMachine_hist();
                            String varDatep = listHistory.get(0).getDate_problem();
                            String varPic = listHistory.get(0).getPic_hist();
                            String varDateSol = listHistory.get(0).getDate_solution();
                            String varPro = listHistory.get(0).getProblem();
                            String varImgp = listHistory.get(0).getImage_problem();
                            String varImgs = listHistory.get(0).getImage_solution();
                            String varPartp = listHistory.get(0).getPart_problem();
                            String varParts = listHistory.get(0).getPart_solution();
                            String varSol = listHistory.get(0).getSolution();
                            String varQtyp = listHistory.get(0).getQty_problem();
                            String varQtys = listHistory.get(0).getQty_solution();
                            String varStatus = listHistory.get(0).getStatus_hist();
                            String varType = listHistory.get(0).getType_hist();
                            String varTitle = listHistory.get(0).getTitle();

//                          Toast.makeText(context, "Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendHV = new Intent(context, HistoryView.class);
                            sendHV.putExtra("xId", varId);
                            sendHV.putExtra("xLine", varLine);
                            sendHV.putExtra("xStation", varStation);
                            sendHV.putExtra("xMachine", varMachine);
                            sendHV.putExtra("xDatep", varDatep);
                            sendHV.putExtra("xPic", varPic);
                            sendHV.putExtra("xDates", varDateSol);
                            sendHV.putExtra("xPro", varPro);
                            sendHV.putExtra("xImgp", varImgp);
                            sendHV.putExtra("xImgs", varImgs);
                            sendHV.putExtra("xPartp", varPartp);
                            sendHV.putExtra("xParts", varParts);
                            sendHV.putExtra("xSol", varSol);
                            sendHV.putExtra("xQtyp", varQtyp);
                            sendHV.putExtra("xQtys", varQtys);
                            sendHV.putExtra("xStatus", varStatus);
                            sendHV.putExtra("xType", varType);
                            sendHV.putExtra("xTitle", varTitle);
                            context.startActivity(sendHV);
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryResponseData> call, Throwable t) {
                        Toast.makeText(context, "Failed To Display Data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return itemView;
    }

    private class ViewHolder {
        private TextView tv1id, tv_asset_line, sptitle, spstation, spmachine, sppic, spdate, spdate2, spproblem;

        ViewHolder (View v){
            tv1id = v.findViewById(R.id.tv1id);
            tv_asset_line = v.findViewById(R.id.tv1);
            sptitle = v.findViewById(R.id.sptitle);
            spstation = v.findViewById(R.id.spstation);
            spmachine = v.findViewById(R.id.spmachine);
            sppic = v.findViewById(R.id.sppic);
            spdate = v.findViewById(R.id.spdate);
            spproblem = v.findViewById(R.id.sptype);
            spdate2 = v.findViewById(R.id.spdate2);
        }

        void bind(HistoryData hd) {
            tv1id.setText(hd.getId_hist());
            tv_asset_line.setText(hd.getLine_hist());
            sptitle.setText(hd.getTitle());
            spstation.setText(hd.getStation_hist());
            spmachine.setText(hd.getMachine_hist());
            sppic.setText(hd.getPic_hist());
            spdate.setText(hd.getDate_problem());
            spproblem.setText(hd.getProblem());
            spdate2.setText(hd.getDate_solution());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Spinner sv;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listHistoryFiltered = listHistory;
                    sv = ((History)context).findViewById(R.id.searchsp);
                    sv.setSelection(0);
                }else{
                    List<HistoryData> listFiltered = new ArrayList<>();
                    for (HistoryData row: listHistory){
                        if(row.getPart_problem().toLowerCase().contains(Key.toLowerCase())||
                                row.getLine_hist().toLowerCase().contains(Key.toLowerCase())||
                                row.getStation_hist().toLowerCase().contains(Key.toLowerCase())||
                                row.getMachine_hist().toLowerCase().contains(Key.toLowerCase())||
                                row.getProblem().toLowerCase().contains(Key.toLowerCase())||
                                row.getPic_hist().toLowerCase().contains(Key.toLowerCase())||
                                row.getStatus_hist().toLowerCase().contains(Key.toLowerCase())||
                                row.getTitle().toLowerCase().contains(Key.toLowerCase())||
                                row.getType_hist().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listHistoryFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listHistoryFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listHistoryFiltered = (List<HistoryData>) filterResults.values;
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
                    listHistoryFiltered = listHistory;
                }else{
                    List<HistoryData> listFilteredss = new ArrayList<>();
                    for (HistoryData row: listHistory){
                        if(row.getLine_hist().toLowerCase().contains(Key.toLowerCase())){
                            listFilteredss.add(row);
                        }
                    }

                    listHistoryFiltered = listFilteredss;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listHistoryFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listHistoryFiltered = (List<HistoryData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
