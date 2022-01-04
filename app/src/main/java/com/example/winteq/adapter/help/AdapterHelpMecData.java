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
import com.example.winteq.HelpMecUpdate;
import com.example.winteq.HelpMecView;
import com.example.winteq.HelpMech;
import com.example.winteq.R;
import com.example.winteq.WMS;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.help.mekanik.HelpDataMec;
import com.example.winteq.model.help.mekanik.HelpResponseDataMec;
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

public class AdapterHelpMecData extends BaseAdapter implements Filterable {
    private Context context;
    private List<HelpDataMec> listHelpMec;
    private List<HelpDataMec> listHelpMecFiltered;
    private String idMec, url, itemname;
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

        final HelpDataMec hdMec = (HelpDataMec) this.getItem(position);

        TextView id;
        ImageView iv_mec;
        id = itemView.findViewById(R.id.tv_mec_id);
        iv_mec = itemView.findViewById(R.id.iv_mec);
        PRDownloader.initialize(context.getApplicationContext());

        if(hdMec.getPdf_mec_image() != null && hdMec.getPdf_mec_image().length()>0){
            Picasso.get().load(hdMec.getPdf_mec_image()).into(iv_mec);
        }else{
            Picasso.get().load(R.drawable.profile).into(iv_mec);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<HelpResponseDataMec> getData = aiData.aiMecGetData(hdMec.getHelp_mec_id());

                getData.enqueue(new Callback<HelpResponseDataMec>() {
                    @Override
                    public void onResponse(Call<HelpResponseDataMec> call, Response<HelpResponseDataMec> response) {

                        if(response.body() != null && response.body().isStatus()) {
                            boolean status = response.body().isStatus();
                            String message = response.body().getMessage();
                            listHelpMec = response.body().getData();

                            String varIdMec = listHelpMec.get(0).getHelp_mec_id();
                            String varItemMec = listHelpMec.get(0).getItem_mec();
                            String varDateMec = listHelpMec.get(0).getMec_date();
                            String varImageMec = listHelpMec.get(0).getPdf_mec_image();
                            String varPdfMec = listHelpMec.get(0).getPdf_mec();
                            String varDescMec = listHelpMec.get(0).getDesc_mec();

//                          Toast.makeText(context, "Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendMec = new Intent(context, HelpMecView.class);
                            sendMec.putExtra("xIdMec", varIdMec);
                            sendMec.putExtra("xItemMec", varItemMec);
                            sendMec.putExtra("xDateMec", varDateMec);
                            sendMec.putExtra("xPdfMec", varPdfMec);
                            sendMec.putExtra("xImageMec", varImageMec);
                            sendMec.putExtra("xDescMec", varDescMec);
                            context.startActivity(sendMec);
                        }
                    }
                    @Override
                    public void onFailure(Call<HelpResponseDataMec> call, Throwable t) {
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
                messageDialog.setTitle("Please Choose Operation for "+hdMec.getItem_mec()+" data")
                        .setMessage("Do you want to Download PDF, Edit Data or Permanently Delete Data, Delete Data cannot be undone.");
                messageDialog.setIcon(R.mipmap.ic_launcher_round);
                messageDialog.setCancelable(true);

                idMec = id.getText().toString();

                messageDialog.setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteData();
                        dialogInterface.dismiss();
                        Handler handler = new android.os.Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((HelpMech)context).retrieveDataMec();
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
                        url = hdMec.getPdf_mec();
                        itemname = hdMec.getItem_mec();
                        downloadData();
                        dialogInterface.dismiss();
                    }
                });


                messageDialog.show();

                return false;
            }
        });

        ViewHolder viewHolder = new ViewHolder(itemView);

        HelpDataMec hde = (HelpDataMec) getItem(position);
        viewHolder.bind(hde);

        return itemView;
    }

    private void deleteData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpDataMec> deleteData = aiData.helpmecdeleteData(idMec);

        deleteData.enqueue(new Callback<HelpDataMec>() {
            public void onResponse(Call<HelpDataMec> call, Response<HelpDataMec> response) {
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

                    helpDataMec = response.body();
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpDataMec> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editData(){
        Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
        Call<HelpResponseDataMec> getData = aiData.aiMecGetData(idMec);

        getData.enqueue(new Callback<HelpResponseDataMec>() {
            @Override
            public void onResponse(Call<HelpResponseDataMec> call, Response<HelpResponseDataMec> response) {

                if(response.body() != null && response.body().isStatus()) {
                    boolean status = response.body().isStatus();
                    String message = response.body().getMessage();
                    listHelpMec = response.body().getData();

                    String varIdMec = listHelpMec.get(0).getHelp_mec_id();
                    String varItemMec = listHelpMec.get(0).getItem_mec();
                    String varDateMec = listHelpMec.get(0).getMec_date();
                    String varImageMec = listHelpMec.get(0).getPdf_mec_image();
                    String varDescMec = listHelpMec.get(0).getDesc_mec();
                    String varPdfMec = listHelpMec.get(0).getPdf_mec();

//                  Toast.makeText(context, "Status: "+status+" | Message: "+message+ " | Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                    Intent sendMec = new Intent(context, HelpMecUpdate.class);
                    sendMec.putExtra("xIdMec", varIdMec);
                    sendMec.putExtra("xItemMec", varItemMec);
                    sendMec.putExtra("xDateMec", varDateMec);
                    sendMec.putExtra("xPdfMec", varPdfMec);
                    sendMec.putExtra("xDescMec", varDescMec);
                    sendMec.putExtra("xImageMec", varImageMec);
                    context.startActivity(sendMec);
                }
            }
            @Override
            public void onFailure(Call<HelpResponseDataMec> call, Throwable t) {
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
        File myDir = new File(root + "/PDF Mechanical Documents");

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
