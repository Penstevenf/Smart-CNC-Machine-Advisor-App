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
import android.widget.Spinner;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestItem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;

    ImageView ipic;
    Button up;
    Spinner line, station;
    EditText machine, pic, part, qty, desc, unit_request;
    FloatingActionButton riad, requestpart;
    Api_Interface apiInterface;
    HistoryData historyData;
    Bitmap bitmap;

    private String xPart, xLine, xStation, xQty, xMachine, xDesc, xPic, xImage;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_request_item);

        //Get SharedPReference
        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);

        //Find Variable
        drawerLayout = findViewById(R.id.requestitem); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Call ApiInterface
        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        //Unpack Intent
        Intent sendAP = getIntent();
        xPart = sendAP.getStringExtra("xPart");
        xLine = sendAP.getStringExtra("xLine");
        xStation = sendAP.getStringExtra("xStation");
        xQty = sendAP.getStringExtra("xQty");
        xMachine = sendAP.getStringExtra("xMachine");
        xDesc = sendAP.getStringExtra("xDesc");
        xImage = sendAP.getStringExtra("xImage");
        xPic = sendAP.getStringExtra("xPic");

        //id
        line = findViewById(R.id.tv1);
        station = findViewById(R.id.pstation);
        machine = findViewById(R.id.pmachine);
        pic = findViewById(R.id.ppic);
        desc = findViewById(R.id.itemdesc);
        part = findViewById(R.id.partreq);
        qty = findViewById(R.id.pqty);
        ipic = findViewById(R.id.itempic);
        up = findViewById(R.id.upreq);
        riad = findViewById(R.id.req);
//        unit_request = findViewById(R.id.unit_request);


        //set data
        if(!(xPic.isEmpty())) {
            pic.setText(xPic);
        }
        if(!(xDesc.isEmpty())) {
            desc.setText("Unit = "+xDesc);
        }
        if(!(xQty.isEmpty())) {
            qty.setText(xQty);
        }
        if(!(xPart.isEmpty())) {
            part.setText(xPart);
        }
        if(!(xMachine.isEmpty())) {
            machine.setText(xMachine);
        }


        if(xLine.equals("Line 1")){
            line.setSelection(0);
        }
        if(xLine.equals("Line 2")){
            line.setSelection(1);
        }
        if(xLine.equals("Line 3")){
            line.setSelection(2);
        }
        if(xLine.equals("Line 4")){
            line.setSelection(3);
        }

        if(xStation.equals("1")){
            station.setSelection(0);
        }
        if(xStation.equals("2")){
            station.setSelection(1);
        }
        if(xStation.equals("3")){
            station.setSelection(2);
        }
        if(xStation.equals("4")){
            station.setSelection(3);
        }
        if(xStation.equals("5")){
            station.setSelection(4);
        }
        if(xStation.equals("6")){
            station.setSelection(5);
        }
        if(xStation.equals("7")){
            station.setSelection(6);
        }
        if(xStation.equals("8")){
            station.setSelection(7);
        }
        if(xStation.equals("9")){
            station.setSelection(8);
        }
        if(xStation.equals("10")){
            station.setSelection(9);
        }


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

        if(!(xImage.isEmpty())) {
            String imageUri = xImage;
            ImageView Image2 = ipic;
            Picasso.get().load(imageUri).into(Image2);
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

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });

        riad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestItem();
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(RequestItem.this, TechnicalSupport.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(RequestItem.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(RequestItem.this, Profile.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(RequestItem.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RequestItem.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(RequestItem.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(RequestItem.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(RequestItem.this, Help.class);
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

    private void requestItem() {
        String ln = line.getSelectedItem().toString();
        String st = station.getSelectedItem().toString();
        String mc = machine.getText().toString();;
        String pc = pic.getText().toString();
        String pb = desc.getText().toString();
        String pa = part.getText().toString();
        String qt = qty.getText().toString();
//        String unit = unit_request.getText().toString();
        String image = getStringImage(bitmap);
        ProgressDialog pd = new ProgressDialog(RequestItem.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        Call<HistoryData> hisaddcall = apiInterface.aiRequestData(ln, st, mc, pc, pa, qt, pb, image);
        hisaddcall.enqueue(new Callback<HistoryData>() {
            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
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
                    historyData = response.body();
                    pd.dismiss();

                    Toast.makeText(RequestItem.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RequestItem.this, TechnicalSupport.class);
                    startActivity(intent);
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(RequestItem.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(RequestItem.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}