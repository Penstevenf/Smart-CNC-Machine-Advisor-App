package com.example.winteq;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.wms.WmsData;
import com.example.winteq.model.wms.WmsResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WMSView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    EditText et_id;
    TextView tv_itemdate, itemtag, tv_qty, tv_item, tv_copro, tv_area, tv_cabinet, tv_shelf, tv_desc, tv_category, tv_type, tv_lifetime;
    FloatingActionButton editdatawms, deletedatawms;
    WmsData wmsData;
    Api_Interface apiInterface;
    ImageView itempic;
    private List<WmsData> listGetWms;

    private String xId, xTag, xDate, xItem, xType, xLifetime, xQty, xCategory, xCopro, xArea, xCabinet, xShelf, xImage, xDescription;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wms_view);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        Intent send = getIntent();
        xId = send.getStringExtra("xId");
        xTag = send.getStringExtra("xTag");
        xDate = send.getStringExtra("xDate");
        xItem = send.getStringExtra("xItem");
        xType = send.getStringExtra("xType");
        xLifetime = send.getStringExtra("xLifetime");
        xQty = send.getStringExtra("xQty");
        xCategory = send.getStringExtra("xCategory");
        xCopro = send.getStringExtra("xCopro");
        xArea = send.getStringExtra("xArea");
        xCabinet = send.getStringExtra("xCabinet");
        xShelf = send.getStringExtra("xShelf");
        xImage = send.getStringExtra("xImage");
        xDescription = send.getStringExtra("xDescription");

        drawerLayout = findViewById(R.id.wmsview);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        editdatawms = findViewById(R.id.editdatawms);
        deletedatawms = findViewById(R.id.deletedatawms);

        et_id = findViewById(R.id.et_wmsid);
        tv_qty = findViewById(R.id.itemqtyz);
        tv_item = findViewById(R.id.itemnamewz);
        tv_copro = findViewById(R.id.itemcoproz);
        tv_area = findViewById(R.id.itemareaz);
        tv_cabinet = findViewById(R.id.itemcabinz);
        tv_shelf = findViewById(R.id.itemshelfz);
        tv_desc = findViewById(R.id.itemdescz);
        tv_category = findViewById(R.id.itemcatz);
        tv_itemdate = findViewById(R.id.itemdatez);
        tv_type = findViewById(R.id.itemtype);
        tv_lifetime = findViewById(R.id.itemlifetime);
        itemtag = findViewById(R.id.itemtagz);
        itempic = findViewById(R.id.itempicz);

        itemtag.setText(xTag);
        et_id.setText(xId);
        tv_qty.setText(xQty);
        tv_item.setText(xItem);
        tv_itemdate.setText(xDate);
        tv_copro.setText(xCopro);
        tv_area.setText(xArea);
        tv_cabinet.setText(xCabinet);
        tv_shelf.setText(xShelf);
        tv_desc.setText(xDescription);
        tv_category.setText(xCategory);
        tv_type.setText(xType);
        tv_lifetime.setText(xLifetime);

        //Setup Header
        View header = navigationView.getHeaderView(0);
        TextView nama = (TextView) header.findViewById(R.id.fname);

        //Setup Shared Preference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Input Name and Profile to Drawer
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

        editdatawms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                Call<WmsResponseData> getData = aiData.aiGetData(et_id.getText().toString());

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

//                    Toast.makeText(WMSView.this, "Status: "+status+" | Message: "+message+ " | Data : "+varImage, Toast.LENGTH_SHORT).show();

                            Intent send = new Intent(WMSView.this, WMSUpdate.class);
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

                            WMSView.this.startActivity(send);
                        }
                    }
                    @Override
                    public void onFailure(Call<WmsResponseData> call, Throwable t) {
                        Toast.makeText(WMSView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deletedatawms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WMSView.this);
                        alertDialogBuilder.setTitle("Are you sure you want to delete this "+xItem+" data?")
                                .setMessage("Do you really want to permanently delete these data? This process cannot be undone.")
                                .setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Api_Interface aiData = ApiClient.getClient().create(Api_Interface.class);
                    Call<WmsData> deleteData = aiData.wmsdeleteData(et_id.getText().toString());

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
                                Toast.makeText(WMSView.this, sr, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WMSView.this, WMS.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(WMSView.this, sr, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<WmsData> call, Throwable t) {
                            Toast.makeText(WMSView.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(WMSView.this, WMS.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(WMSView.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(WMSView.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(WMSView.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WMSView.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(WMSView.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(WMSView.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(WMSView.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}