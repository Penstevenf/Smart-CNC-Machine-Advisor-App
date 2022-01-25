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

import com.example.winteq.History;
import com.example.winteq.HistoryView;
import com.example.winteq.R;
import com.example.winteq.model.history.HistoryData;

import java.util.ArrayList;
import java.util.List;

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
            public void onClick(View v) {
//                Toast.makeText(context, line+" Selected", Toast.LENGTH_SHORT).show();
                Intent nameSend = new Intent(context, HistoryView.class);
                context.startActivity(nameSend);
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
