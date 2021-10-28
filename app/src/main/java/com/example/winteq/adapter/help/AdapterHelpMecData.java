package com.example.winteq.adapter.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.winteq.R;
import com.example.winteq.WMS;
import com.example.winteq.model.help.mekanik.HelpDataMec;

import java.util.ArrayList;
import java.util.List;

public class AdapterHelpMecData extends BaseAdapter implements Filterable {
    private Context context;
    private List<HelpDataMec> listHelpMec;
    private List<HelpDataMec> listHelpMecFiltered;
    private List<HelpDataMec> listGetHelpMec;
    HelpDataMec helpDataMec;

    public AdapterHelpMecData(Context context, List<HelpDataMec> listHelpMec) {
        this.context = context;
        this.listHelpMec = listHelpMec;
        this.listHelpMecFiltered = listHelpMec;
    }

    public AdapterHelpMecData(WMS context) {
    }

    @Override
    public int getCount() {
        return listHelpMecFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return listHelpMecFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_help_mec, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(itemView);

        HelpDataMec hde = (HelpDataMec) getItem(position);
        viewHolder.bind(hde);

        return itemView;
    }

    private class ViewHolder {
        private TextView tv_mec, tv_mec_id;

        ViewHolder (View v){
            tv_mec = v.findViewById(R.id.tv_mec);
            tv_mec_id = v.findViewById(R.id.tv_mec_id);
        }

        void bind(HelpDataMec hde){
            tv_mec.setText(hde.getItem_mec());
            tv_mec_id.setText(hde.getHelp_mec_id());

        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listHelpMecFiltered = listHelpMec;
                }else{
                    List<HelpDataMec> listFiltered = new ArrayList<>();
                    for (HelpDataMec row: listHelpMecFiltered){
                        if(row.getItem_mec().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listHelpMecFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listHelpMecFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listHelpMecFiltered = (List<HelpDataMec>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
