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

public class WMSUpdate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    EditText et_id, et_qty, et_item, et_copro, et_area, et_cabinet, et_shelf, et_desc, et_type, et_lifetime;
    TextView tv_itemdate, itemtag;
    FloatingActionButton fb_edit;
    WmsData wmsData;
    Api_Interface apiInterface;
    RadioGroup radioGroup;
    RadioButton radioButton, radioElektrik, radioMekanik;
    ImageView edit_image;
    Button btn_wmseditimage;
    Bitmap bitmap;

    private String xId, xTag, xDate, xItem, xType, xLifetime, xQty, xCategory, xCopro, xArea, xCabinet, xShelf, xImage, xDescription;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wms_update);

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

        drawerLayout = findViewById(R.id.wmsupdate);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        fb_edit = findViewById(R.id.fb_edit);
        et_id = findViewById(R.id.et_idwmsedit);
        et_qty = findViewById(R.id.itemqty);
        et_item = findViewById(R.id.itemnamew);
        et_copro = findViewById(R.id.itemcopro);
        et_area = findViewById(R.id.eitemarea);
        et_cabinet = findViewById(R.id.eitemcabin);
        et_shelf = findViewById(R.id.eitemshelf);
        et_desc = findViewById(R.id.itemdesc);
        et_type = findViewById(R.id.typew);
        et_lifetime = findViewById(R.id.et_lifetimeview);
        tv_itemdate = findViewById(R.id.tv_itemdate);
        tv_itemdate.setText(xDate);
        itemtag = findViewById(R.id.itemtag);
        itemtag.setText(xTag);
        radioGroup = findViewById(R.id.rg_update);
        radioElektrik = findViewById(R.id.rb_edit_elektrik);
        radioMekanik = findViewById(R.id.rb_edit_mekanik);
        btn_wmseditimage = findViewById(R.id.btn_wmseditimage);
        btn_wmseditimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });
        edit_image = findViewById(R.id.itempic);

        et_id.setText(xId);
        et_qty.setText(xQty);
        et_item.setText(xItem);
        et_type.setText(xType);
        et_lifetime.setText(xLifetime);
        String category = xCategory;

        if (category.equals("Electrical")){
            radioElektrik.setChecked(true);
            radioMekanik.setChecked(false);
        }
        else{
            radioMekanik.setChecked(true);
            radioElektrik.setChecked(false);
        }
        et_copro.setText(xCopro);
        et_area.setText(xArea);
        et_cabinet.setText(xCabinet);
        et_shelf.setText(xShelf);
        et_desc.setText(xDescription);


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
            ImageView Image1 = edit_image;
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

        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WMSUpdate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(WMSUpdate.this, WMS.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(WMSUpdate.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(WMSUpdate.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(WMSUpdate.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WMSUpdate.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(WMSUpdate.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(WMSUpdate.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(WMSUpdate.this, Help.class);
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

            try {
                imageStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(imageStream);
//              bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                this.edit_image.setImageBitmap(bitmap);
                View header = navigationView.getHeaderView(0);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) edit_image.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }


    private void WMSUpdate() {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String id = et_id.getText().toString();
        String qty = et_qty.getText().toString();
        String item_name = et_item.getText().toString();
        String type = et_type.getText().toString();
        String lifetime_wms = et_lifetime.getText().toString();
        String category = radioButton.getText().toString();
        String copro = et_copro.getText().toString();
        String area = et_area.getText().toString();
        String cabinet = et_cabinet.getText().toString();
        String shelf = et_shelf.getText().toString();
        String description = et_desc.getText().toString();
        String image = getStringImage(bitmap);
        ProgressDialog pd = new ProgressDialog(WMSUpdate.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Call<WmsData> WMSUpdatecall = apiInterface.wmsupdateData(id, qty, item_name, type, lifetime_wms, category, copro, area, cabinet, shelf, description, image);
        WMSUpdatecall.enqueue(new Callback<WmsData>() {
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

                    Toast.makeText(WMSUpdate.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WMSUpdate.this, WMS.class);
                    startActivity(intent);
                    finish();

                } else {
                    pd.dismiss();
                    Toast.makeText(WMSUpdate.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WmsData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(WMSUpdate.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}