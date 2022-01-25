package com.example.winteq;

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
import com.example.winteq.model.history.HistoryData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RepairItem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;

    ImageView ipic;
    Button up;
    EditText line,station,machine,pic,part,desc;
    FloatingActionButton riad;
    Api_Interface apiInterface;
    HistoryData historyData;
    Bitmap bitmap;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_repair_item);

        //Get SharedPReference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        //Find Variable
        drawerLayout = findViewById(R.id.repairitem); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Call ApiInterface
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        line = findViewById(R.id.pline);
        station = findViewById(R.id.pstation);
        machine = findViewById(R.id.pmachine);
        pic = findViewById(R.id.ppic);
        desc = findViewById(R.id.itemdesc);
        part = findViewById(R.id.partreq);
        ipic = findViewById(R.id.itempic);
        up = findViewById(R.id.upreq);
        riad = findViewById(R.id.req);

        //Setup Header
        View header = navigationView.getHeaderView(0);

        TextView nama = (TextView) header.findViewById(R.id.fname);

        //Setup Toolbar
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

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });

        riad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                repairItem();
            }
        });
    }

//            String ln = line.getText().toString();
//            String st = station.getText().toString();
//            String mc = machine.getText().toString();;
//            String pc = pic.getText().toString();
//            String sa = "Waiting";
//            String im = sp.getString("papicc","");
//            String pb = desc.getText().toString();
//            String pa = part.getText().toString();
//            String ty = "Part Repair";


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(RepairItem.this, TechnicalSupport.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(RepairItem.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(RepairItem.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(RepairItem.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RepairItem.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(RepairItem.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(RepairItem.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(RepairItem.this, Help.class);
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
                this.ipic.setImageBitmap(bitmap);
                View header = navigationView.getHeaderView(0);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) ipic.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

//    private void repairItem() {
//        String ln = line.getText().toString();
//        String st = station.getText().toString();
//        String mc = machine.getText().toString();;
//        String pc = pic.getText().toString();
//        String pb = desc.getText().toString();
//        String pa = part.getText().toString();
//        String image = getStringImage(bitmap);
//        ProgressDialog pd = new ProgressDialog(RepairItem.this);
//        pd.setMessage("Loading...");
//        pd.setCancelable(false);
//        pd.show();
//
//        Call<HistoryData> hisaddcall = apiInterface.aiRequestData(ln,st, mc, pc, pa, pb, image);
//        hisaddcall.enqueue(new Callback<HistoryData>() {
//            @Override
//            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
//                //sr untuk menampung array message dalam bentuk string
//                //loop isi data dari array message lalu di append ke dalam string sr
//                //if else untuk mencegah mengambil value awal dari string sr ("")
//                String sr = "";
//                for(int i=0 ; i<response.body().getMessage().length ; i++){
//                    if(sr.length() == 0){
//                        sr = response.body().getMessage()[i];
//                    }else{
//                        sr = sr + "\n" + response.body().getMessage()[i];
//                    }
//                }
//                if(response.body() != null && response.body().isStatus()){
//                    historyData = response.body();
//                    pd.dismiss();
//
//                    Toast.makeText(RepairItem.this, sr, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RepairItem.this, TechnicalSupport.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    pd.dismiss();
//                    Toast.makeText(RepairItem.this, sr, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HistoryData> call, Throwable t) {
//                pd.dismiss();
//                Toast.makeText(RepairItem.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}