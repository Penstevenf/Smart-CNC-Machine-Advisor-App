package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.winteq.api.ApiClient;
import com.example.winteq.api.Api_Interface;
import com.example.winteq.model.wms.WmsData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WMSAdd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    EditText et_qty, et_item, et_copro, et_area, et_cabinet, et_shelf, et_desc, et_lifetime, et_type;
    TextView tv_default, tv_tag;
    FloatingActionButton wmsin;
    WmsData wmsData;
    Api_Interface apiInterface;
    RadioGroup radioGroup;
    RadioButton radioButton, rb_elc, rb_mec;
    Bitmap bitmap;
    Button btn_wmsimage;
    ImageView add_image;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";
    private static final String ELC_TAG = "Elc_tag";
    private static final String MEC_TAG = "Mec_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wmsadd);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);
        drawerLayout = findViewById(R.id.wmsadd);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        wmsin = findViewById(R.id.wmsup);

        tv_tag = findViewById(R.id.tv_tag);
        et_qty = findViewById(R.id.e3);
        et_item = findViewById(R.id.e6);
        et_copro = findViewById(R.id.e5);
        et_area = findViewById(R.id.e7);
        et_cabinet = findViewById(R.id.e8);
        et_shelf = findViewById(R.id.e9);
        et_desc = findViewById(R.id.e10);
        et_type = findViewById(R.id.et_typewms);
        et_lifetime = findViewById(R.id.et_lifewms);
        tv_default = findViewById(R.id.tv_default);
        radioGroup = findViewById(R.id.rg_add);
        rb_elc = findViewById(R.id.rb_add_elektrik);
        rb_mec = findViewById(R.id.rb_add_mekanik);

        rb_elc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString(ELC_TAG, null) == null) {
                    tv_tag.setText("ELC-001");
                }
                else if (sp.getString(ELC_TAG, null) != null) {
                    tv_tag.setText(sp.getString(ELC_TAG, null));
                }
            }
        });

        rb_elc.performClick();

        rb_mec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString(MEC_TAG, null) == null) {
                    tv_tag.setText("MEC-001");
                }
                else if (sp.getString(MEC_TAG, null) != null) {
                    tv_tag.setText(sp.getString(MEC_TAG, null));
                }
            }
        });


        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        btn_wmsimage = findViewById(R.id.btn_wmsimage);
        add_image = findViewById(R.id.add_img);
        btn_wmsimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });

        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        nama.setText(sp.getString(FULLNAME, null));
        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

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

        wmsin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wmsadd();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(WMSAdd.this, WMS.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(WMSAdd.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(WMSAdd.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(WMSAdd.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WMSAdd.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(WMSAdd.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(WMSAdd.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(WMSAdd.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //untuk intent ke pemilihan gambar
    private void choosefile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri filepath = data.getData();
            InputStream imageStream;
            tv_default.setText("Image Preview");

            try {
                imageStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(imageStream);
//              bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                this.add_image.setImageBitmap(bitmap);
                View header = navigationView.getHeaderView(0);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) add_image.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

    private void wmsadd() {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String qty = et_qty.getText().toString();
        String item_name = et_item.getText().toString();
        String type = et_type.getText().toString();
        String lifetime_wms = et_lifetime.getText().toString();
        String copro = et_copro.getText().toString();
        String category = radioButton.getText().toString();
        String area = et_area.getText().toString();
        String cabinet = et_cabinet.getText().toString();
        String shelf = et_shelf.getText().toString();
        String description = et_desc.getText().toString();
        String image = getStringImage(bitmap);
        ProgressDialog pd = new ProgressDialog(WMSAdd.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Call<WmsData> wmsaddcall = apiInterface.wmsaddResponse(qty, item_name, type, lifetime_wms, category, copro, area, cabinet, shelf, description, image);
        wmsaddcall.enqueue(new Callback<WmsData>() {
            @Override
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
                    pd.dismiss();

                    Toast.makeText(WMSAdd.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WMSAdd.this, WMS.class);
                    startActivity(intent);
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(WMSAdd.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WmsData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(WMSAdd.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}