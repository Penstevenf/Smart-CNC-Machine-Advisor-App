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
import com.example.winteq.model.help.elektrik.HelpDataElc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpElcUpdate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText et_itemelcupdate, et_idelcupdate;
    Button btn_elcimageupdate, btn_elcpdfupdate;
    TextView tv_elcpdfupdate;
    FloatingActionButton elc_up;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Api_Interface apiInterface;
    Bitmap bitmap;
    ImageView iv_elcimageupdate;
    HelpDataElc helpDataElc;

    private  String encodedPDF;

    private String xIdElc, xItemElc, xDateElc, xPdfElc, xImageElc;
    private static final String SHARE_PREF_NAME = "mypref";
    private static final String FULLNAME = "fullname";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_elc_update);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        Intent sendElc = getIntent();
        xIdElc = sendElc.getStringExtra("xIdElc");
        xItemElc = sendElc.getStringExtra("xItemElc");
        xPdfElc = sendElc.getStringExtra("xPdfElc");
        xImageElc = sendElc.getStringExtra("xImageElc");

        drawerLayout = findViewById(R.id.help_elc_update);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        elc_up = findViewById(R.id.elc_up);

        iv_elcimageupdate = findViewById(R.id.iv_elcimageupdate);

        tv_elcpdfupdate = findViewById(R.id.tv_elcpdfupdate);
        et_idelcupdate = findViewById(R.id.et_idelcupdate);
        et_idelcupdate.setText(xIdElc);

        et_itemelcupdate = findViewById(R.id.et_itemelcupdate);
        et_itemelcupdate.setText(xItemElc);

        btn_elcimageupdate = findViewById(R.id.btn_elcimageupdate);
        btn_elcimageupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

            }
        });

        btn_elcpdfupdate = findViewById(R.id.btn_elcpdfupdate);
        btn_elcpdfupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile.createChooser(chooseFile, "Select PDF"), 1);

            }
        });

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

        if(!(xImageElc.isEmpty())) {
            String imageUri1 = xImageElc;
            ImageView Image1 = iv_elcimageupdate;
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

        elc_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elcup();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(HelpElcUpdate.this, HelpElec.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent1 = new Intent(HelpElcUpdate.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HelpElcUpdate.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HelpElcUpdate.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(HelpElcUpdate.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(HelpElcUpdate.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(HelpElcUpdate.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case 0:
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 0 && resultCode == RESULT_OK && data != null){

                    Uri filepath = data.getData();
                    InputStream imageStream;

                    try {
                        imageStream = getContentResolver().openInputStream(filepath);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        this.iv_elcimageupdate.setImageBitmap(bitmap);
                        View header = navigationView.getHeaderView(0);

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;

            case 1:
                super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == 1 && resultCode == RESULT_OK && data != null) {

                    Uri path = data.getData();
                    InputStream inputStream;

                    try {
                        inputStream = getContentResolver().openInputStream(path);

                        byte[] pdfInBytes = new byte[inputStream.available()];
                        inputStream.read(pdfInBytes);
                        encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                        tv_elcpdfupdate.setText("Document Selected");
                        btn_elcpdfupdate.setText("Change Document");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String getStringImage(Bitmap bitmap){
        if(bitmap == null){
            bitmap = ((BitmapDrawable) iv_elcimageupdate.getDrawable()).getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

    private void elcup() {
        String help_elc_id =  et_idelcupdate.getText().toString();
        String item_elc = et_itemelcupdate.getText().toString();
        String pdf_elc_image = getStringImage(bitmap);
        String pdf_elc = encodedPDF;

        Call<HelpDataElc> HelpElcUpdatecall = apiInterface.helpelcupdateData(help_elc_id, item_elc, pdf_elc_image, pdf_elc);
        HelpElcUpdatecall.enqueue(new Callback<HelpDataElc>() {
            @Override
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

                    Toast.makeText(HelpElcUpdate.this, sr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HelpElcUpdate.this, HelpElec.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(HelpElcUpdate.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpDataElc> call, Throwable t) {
                Toast.makeText(HelpElcUpdate.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}