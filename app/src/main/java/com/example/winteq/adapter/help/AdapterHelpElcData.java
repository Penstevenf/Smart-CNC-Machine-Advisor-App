package com.example.winteq.adapter.help;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.winteq.HelpElcUpdate;
import com.example.winteq.HelpElcView;
import com.example.winteq.HelpElec;
import com.example.winteq.R;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.help.elektrik.HelpDataElc;
import com.example.winteq.model.help.elektrik.HelpResponseDataElc;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterHelpElcData extends BaseAdapter implements Filterable {
    private Context context;
    private List<HelpDataElc> listHelpElc;
    private List<HelpDataElc> listHelpElcFiltered;
    private String idElc, url, itemname;
    HelpDataElc helpDataElc;

    public AdapterHelpElcData(Context context, List<HelpDataElc> listHelpElc) {
        this.context = context;
        this.listHelpElc = listHelpElc;
        this.listHelpElcFiltered = listHelpElc;
    }

    @Override
    public int getCount() {
        return listHelpElcFiltered.size();
    }


    @Override
    public Object getItem(int position) {
        return listHelpElcFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_help_elc, parent, false);
        }

        final HelpDataElc hdElc = (HelpDataElc) this.getItem(position);

        TextView id;
        ImageView iv_elc;
        id = itemView.findViewById(R.id.tv_elc_id);
        iv_elc = itemView.findViewById(R.id.iv_elc);
        PRDownloader.initialize(context.getApplicationContext());

        if(hdElc.getPdf_elc_image() != null && hdElc.getPdf_elc_image().length()>0){
            Picasso.get().load(hdElc.getPdf_elc_image()).into(iv_elc);
        }else{
            Picasso.get().load(R.drawable.profile).into(iv_elc);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<HelpResponseDataElc> getData = aiData.aiElcGetData(hdElc.getHelp_elc_id());

                getData.enqueue(new Callback<HelpResponseDataElc>() {
                    @Override
                    public void onResponse(Call<HelpResponseDataElc> call, Response<HelpResponseDataElc> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listHelpElc = response.body().getData();

                            String varIdElc = listHelpElc.get(0).getHelp_elc_id();
                            String varItemElc = listHelpElc.get(0).getItem_elc();
                            String varDateElc = listHelpElc.get(0).getElc_date();
                            String varImageElc = listHelpElc.get(0).getPdf_elc_image();
                            String varPdfElc = listHelpElc.get(0).getPdf_elc();
                            String varDescElc = listHelpElc.get(0).getDesc_elc();

//                          Toast.makeText(context, "Data : "+varIdElc+varItemElc, Toast.LENGTH_SHORT).show();

                            Intent sendElc = new Intent(context, HelpElcView.class);
                            sendElc.putExtra("xIdElc", varIdElc);
                            sendElc.putExtra("xItemElc", varItemElc);
                            sendElc.putExtra("xDateElc", varDateElc);
                            sendElc.putExtra("xPdfElc", varPdfElc);
                            sendElc.putExtra("xImageElc", varImageElc);
                            sendElc.putExtra("xDescElc", varDescElc);
                            context.startActivity(sendElc);
                        }
                    }
                    @Override
                    public void onFailure(Call<HelpResponseDataElc> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //untuk menampilkan dialog
                AlertDialog.Builder messageDialog = new AlertDialog.Builder(context);
                messageDialog.setTitle("Please Choose Operation for "+hdElc.getItem_elc()+" data")
                        .setMessage("Do you want to Download PDF, Edit Data or Permanently Delete Data, Delete Data cannot be undone.");
                messageDialog.setIcon(R.mipmap.ic_launcher_round);
                messageDialog.setCancelable(true);

                idElc = id.getText().toString();

                messageDialog.setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData();
                        dialogInterface.dismiss();
                        Handler handler = new android.os.Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((HelpElec)context).retrieveDataElc();
                            }
                        }, 500);
                    }
                });

                messageDialog.setNegativeButton("Edit Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editData();
                        dialogInterface.dismiss();
                    }
                });

                messageDialog.setNeutralButton("Download PDF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        url = hdElc.getPdf_elc();
                        itemname = hdElc.getItem_elc();
                        downloadData();
                        dialogInterface.dismiss();
                    }
                });


                messageDialog.show();

                return false;
            }
        });

        ViewHolder viewHolder = new ViewHolder(itemView);

        HelpDataElc hde = (HelpDataElc) getItem(position);
        viewHolder.bind(hde);

        return itemView;
    }

    private void deleteData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpDataElc> deleteData = aiData.helpelcdeleteData(idElc);

        deleteData.enqueue(new Callback<HelpDataElc>() {
            public void onResponse(Call<HelpDataElc> call, Response<HelpDataElc> response) {
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

                    helpDataElc = response.body();
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpDataElc> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpResponseDataElc> getData = aiData.aiElcGetData(idElc);

        getData.enqueue(new Callback<HelpResponseDataElc>() {
            @Override
            public void onResponse(Call<HelpResponseDataElc> call, Response<HelpResponseDataElc> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listHelpElc = response.body().getData();

                    String varIdElc = listHelpElc.get(0).getHelp_elc_id();
                    String varItemElc = listHelpElc.get(0).getItem_elc();
                    String varDateElc = listHelpElc.get(0).getElc_date();
                    String varImageElc = listHelpElc.get(0).getPdf_elc_image();
                    String varDescElc = listHelpElc.get(0).getDesc_elc();
                    String varPdfElc = listHelpElc.get(0).getPdf_elc();

//                  Toast.makeText(context, "Status: "+status+" | Message: "+message+ " | Data : "+varIdElc+varItemElc, Toast.LENGTH_SHORT).show();

                    Intent sendElc = new Intent(context, HelpElcUpdate.class);
                    sendElc.putExtra("xIdElc", varIdElc);
                    sendElc.putExtra("xItemElc", varItemElc);
                    sendElc.putExtra("xDateElc", varDateElc);
                    sendElc.putExtra("xPdfElc", varPdfElc);
                    sendElc.putExtra("xDescElc", varDescElc);
                    sendElc.putExtra("xImageElc", varImageElc);
                    context.startActivity(sendElc);
                }
            }
            @Override
            public void onFailure(Call<HelpResponseDataElc> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadData(){
        runtimePermission();
    }

    private void runtimePermission() {
            Dexter.withContext(context).withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        downloadPDF();
                    } else if(report.isAnyPermissionPermanentlyDenied()){
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        Toast.makeText(context, "Please Grant Permissions to Download PDF", Toast.LENGTH_SHORT).show();
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("You have permanently denied some of the required permissions " +
                                        "For this action, please choose Settings, then go to permissions and allow Storage permission.")
                                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", context.getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
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
                    } else{
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        Toast.makeText(context, "Please Grant Permissions to Download PDF", Toast.LENGTH_SHORT).show();
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("You have denied some of the required permissions. " +
                                        "For this action, please choose Allow to re-choose the permission.")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        runtimePermission();
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
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).onSameThread()
                    .check();
        }

    private void downloadPDF(){

        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Downloading...");
        pd.setCancelable(false);
        pd.show();

//      File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File myDir = new File(root + "/PDF Electrical Documents");

        if(!myDir.exists()){
            myDir.mkdir();
        }

        File fileLocation = new File(myDir,itemname);

        PRDownloader.download(url, fileLocation.getPath(), URLUtil.guessFileName(url, null, null))
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        Long per = progress.currentBytes*100 / progress.totalBytes;

                        pd.setMessage("Downloading : "+per+"%");
                        //set legacy external storage true in android manifest
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        pd.dismiss();
                        Toast.makeText(context, "PDF Downloaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        pd.dismiss();
                        Toast.makeText(context, "Error: Can't Download PDF, There Might Be No PDF", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class ViewHolder {
        private TextView tv_elc, tv_elc_id;

        ViewHolder (View v){
            tv_elc = v.findViewById(R.id.tv_elc);
            tv_elc_id = v.findViewById(R.id.tv_elc_id);
        }

        void bind(HelpDataElc hde){
            tv_elc.setText(hde.getItem_elc());
            tv_elc_id.setText(hde.getHelp_elc_id());

        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if(Key.isEmpty()){
                    listHelpElcFiltered = listHelpElc;
                }else{
                    List<HelpDataElc> listFiltered = new ArrayList<>();
                    for (HelpDataElc row: listHelpElcFiltered){
                        if(row.getItem_elc().toLowerCase().contains(Key.toLowerCase())){
                            listFiltered.add(row);
                        }
                    }

                    listHelpElcFiltered = listFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listHelpElcFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listHelpElcFiltered = (List<HelpDataElc>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
