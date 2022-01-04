package com.example.winteq;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.help.mekanik.HelpDataMec;
import com.example.winteq.model.help.mekanik.HelpResponseDataMec;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpMecView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    EditText et_id;
    TextView tv_itemdate, tv_item,tv_desc;
    FloatingActionButton editdatamec, deletedatamec;
    Api_Interface apiInterface;
    ImageView itempic;
    Button btn_downloadpdf, btn_viewpdf;
    HelpDataMec helpDataMec;
    private List<HelpDataMec> listHelpMec;

    private String xId, xDate, xItem, xDescription, xImage, xPdf;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_mec_view);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);
        PRDownloader.initialize(HelpMecView.this.getApplicationContext());

        Intent sendMec = getIntent();
        xId = sendMec.getStringExtra("xIdMec");
        xDate = sendMec.getStringExtra("xDateMec");
        xItem = sendMec.getStringExtra("xItemMec");
        xImage = sendMec.getStringExtra("xImageMec");
        xPdf = sendMec.getStringExtra("xPdfMec");
        xDescription = sendMec.getStringExtra("xDescMec");

        drawerLayout = findViewById(R.id.helpmecview);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        editdatamec = findViewById(R.id.editdatamec);
        deletedatamec = findViewById(R.id.deletedatamec);

        et_id = findViewById(R.id.et_mecid);
        tv_item = findViewById(R.id.itemnamewzzm);
        tv_desc = findViewById(R.id.itemdesczzm);
        tv_itemdate = findViewById(R.id.itemdatezzm);
        itempic = findViewById(R.id.itempiczzm);
        btn_viewpdf = findViewById(R.id.btn_viewpdfmec);
        btn_viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpMecView.this, PDFElektrik.class);
                i.putExtra("PATH", xPdf);
                HelpMecView.this.startActivity(i);
            }
        });

        btn_downloadpdf = findViewById(R.id.btn_downloadpdfmec);
        btn_downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData();
            }
        });

        et_id.setText(xId);
        tv_item.setText(xItem);
        tv_itemdate.setText(xDate);
        tv_desc.setText(xDescription);

        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        nama.setText(sp.getString(FULLNAME, null));

        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

        if(!(xImage.isEmpty())) {
            String imageUri1 = xImage;
            ImageView Image1 = itempic;
            Picasso.get().load(imageUri1).into(Image1);
        }

        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image2);
        }

        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            pfph.setImageBitmap(bitmap);
        }

        editdatamec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<HelpResponseDataMec> getData = aiData.aiMecGetData(et_id.getText().toString());

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
                            String varDescMec = listHelpMec.get(0).getDesc_mec();
                            String varImageMec = listHelpMec.get(0).getPdf_mec_image();
                            String varPdfMec = listHelpMec.get(0).getPdf_mec();

//                  Toast.makeText(HelpMecView.this, "Status: "+status+" | Message: "+message+ " | Data : "+varIdMec+varItemMec, Toast.LENGTH_SHORT).show();

                            Intent sendMec = new Intent(HelpMecView.this, HelpMecUpdate.class);
                            sendMec.putExtra("xIdMec", varIdMec);
                            sendMec.putExtra("xItemMec", varItemMec);
                            sendMec.putExtra("xDateMec", varDateMec);
                            sendMec.putExtra("xDescMec", varDescMec);
                            sendMec.putExtra("xPdfMec", varPdfMec);
                            sendMec.putExtra("xImageMec", varImageMec);
                            HelpMecView.this.startActivity(sendMec);
                        }
                    }
                    @Override
                    public void onFailure(Call<HelpResponseDataMec> call, Throwable t) {
                        Toast.makeText(HelpMecView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deletedatamec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpMecView.this);
                alertDialogBuilder.setTitle("Are you sure you want to delete this "+xItem+" data?")
                        .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                        .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                                Call<HelpDataMec> deleteData = aiData.helpmecdeleteData(et_id.getText().toString());

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
                                            Toast.makeText(HelpMecView.this, sr, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(HelpMecView.this, HelpMech.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(HelpMecView.this, sr, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<HelpDataMec> call, Throwable t) {
                                        Toast.makeText(HelpMecView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(HelpMecView.this, HelpMech.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(HelpMecView.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(HelpMecView.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HelpMecView.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpMecView.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(HelpMecView.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(HelpMecView.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(HelpMecView.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadData(){
        runtimePermission();
    }

    private void runtimePermission() {
        Dexter.withContext(HelpMecView.this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    downloadPDF();
                } else if(report.isAnyPermissionPermanentlyDenied()){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpMecView.this);
                    Toast.makeText(HelpMecView.this, "Please Grant Permissions to Download PDF", Toast.LENGTH_SHORT).show();
                    alertDialogBuilder.setTitle("Permissions Required")
                            .setMessage("You have permanently denied some of the required permissions " +
                                    "For this action, please choose Settings, then go to permissions and allow Storage permission.")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", HelpMecView.this.getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    HelpMecView.this.startActivity(intent);
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
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HelpMecView.this);
                    Toast.makeText(HelpMecView.this, "Please Grant Permissions to Download PDF", Toast.LENGTH_SHORT).show();
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

        ProgressDialog pd = new ProgressDialog(HelpMecView.this);
        pd.setMessage("Downloading...");
        pd.setCancelable(false);
        pd.show();

//      File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File myDir = new File(root + "/PDF Mechanical Documents");

        if(!myDir.exists()){
            myDir.mkdir();
        }

        File fileLocation = new File(myDir,xItem);

        PRDownloader.download(xPdf, fileLocation.getPath(), URLUtil.guessFileName(xPdf, null, null))
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
                        Toast.makeText(HelpMecView.this, "PDF Downloaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        pd.dismiss();
                        Toast.makeText(HelpMecView.this, "Error: Can't Download PDF, There Might Be No PDF", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}