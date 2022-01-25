package com.example.winteq.adapter.solution;

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

import com.example.winteq.R;
import com.example.winteq.Solution;
import com.example.winteq.SolutionData;
import com.example.winteq.TechnicalSupport;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.history.HistoryData;
import com.example.winteq.model.history.HistoryResponseData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSolution extends BaseAdapter implements Filterable {

    private Context context;
    private List<HistoryData> listHistory;
    private List<HistoryData> listHistoryFiltered;
    private String idHist, nameAsset, noAsset, line;

    public AdapterSolution(Context context, List<HistoryData> listHistory) {
        this.context = context;
        this.listHistory = listHistory;
        this.listHistoryFiltered = listHistory;
    }

    public AdapterSolution(Solution context) {
    }

    public AdapterSolution(TechnicalSupport context) {
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
            itemView = LayoutInflater.from(context).inflate(R.layout.listview_layout_sp, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        HistoryData hd = (HistoryData) getItem(position);
        viewHolder.bind(hd);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<HistoryResponseData> getData = aiData.aiGetProblemData(hd.getId_hist());

                getData.enqueue(new Callback<HistoryResponseData>() {
                    @Override
                    public void onResponse(Call<HistoryResponseData> call, Response<HistoryResponseData> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listHistory = response.body().getData();

                            String varId = listHistory.get(0).getId_hist();
                            String varLine = listHistory.get(0).getLine_hist();
                            String varStation = listHistory.get(0).getStation_hist();
                            String varMachine = listHistory.get(0).getMachine_hist();
                            String varDate = listHistory.get(0).getDate_problem();
                            String varPic = listHistory.get(0).getPic_hist();
                            String varTitle = listHistory.get(0).getTitle();
                            String varProblem = listHistory.get(0).getProblem();
                            String varImage = listHistory.get(0).getImage_problem();

//                          Toast.makeText(context, "Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendPr = new Intent(context, SolutionData.class);
                            sendPr.putExtra("xId", varId);
                            sendPr.putExtra("xLine", varLine);
                            sendPr.putExtra("xStation", varStation);
                            sendPr.putExtra("xMachine", varMachine);
                            sendPr.putExtra("xDate", varDate);
                            sendPr.putExtra("xPic", varPic);
                            sendPr.putExtra("xTitle", varTitle);
                            sendPr.putExtra("xProblem", varProblem);
                            sendPr.putExtra("xImage", varImage);
                            context.startActivity(sendPr);
                        }
                    }
                    @Override
                    public void onFailure(Call<HistoryResponseData> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return itemView;
    }

    private class ViewHolder {
        private TextView tv1id, tv_asset_line, sptitle, spstation, spmachine, sppic, spdate, sptype;

        ViewHolder (View v){
            tv1id = v.findViewById(R.id.tv1id);
            tv_asset_line = v.findViewById(R.id.tv1);
            sptitle = v.findViewById(R.id.sptitle);
            spstation = v.findViewById(R.id.spstation);
            spmachine = v.findViewById(R.id.spmachine);
            sppic = v.findViewById(R.id.sppic);
            spdate = v.findViewById(R.id.spdate);
            sptype = v.findViewById(R.id.sptype);
        }

        void bind(HistoryData hd) {
            tv1id.setText(hd.getId_hist());
            tv_asset_line.setText(hd.getLine_hist());
            sptitle.setText(hd.getTitle());
            spstation.setText(hd.getStation_hist());
            spmachine.setText(hd.getMachine_hist());
            sppic.setText(hd.getPic_hist());
            spdate.setText(hd.getDate_problem());
            sptype.setText(hd.getType_hist());
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Spinner sv, sv2;
                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listHistoryFiltered = listHistory;
                    sv = ((Solution)context).findViewById(R.id.searchsp);
                    sv.setSelection(0);
                    sv2 = ((TechnicalSupport)context).findViewById(R.id.searchsp);
                    sv2.setSelection(0);
                }else{
                    List<HistoryData> listFiltered = new ArrayList<>();
                    for (HistoryData row: listHistory){
                        if(row.getPart_problem().toLowerCase().contains(Key.toLowerCase())){
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
