package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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
import com.example.winteq.model.user.UserData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;
    Button btn_image;
    FloatingActionButton Update;
    EditText et_fullname, et_email, et_npk, et_username, et_telp, et_id, et_id2, et_id3, et_stat;
    TextView nama, name;
    Api_Interface apiInterface;
    UserData userData;
    private Bitmap bitmap;
    ImageView profile_image;
    Switch status;

    private static final String SHARE_PREF_NAME = "mypref";
    private static final String ID = "id";
    private static final String NPK = "npk";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String TELP = "no_telp";
    private static final String STAT = "stat";
    private static final String IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        apiInterface = ApiClient.getClient().create(Api_Interface.class);

        drawerLayout = findViewById(R.id.prof);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        Update = findViewById(R.id.Update);

        et_email = findViewById(R.id.pfl_email);
        et_npk = findViewById(R.id.pfl_npk);
        et_fullname = findViewById(R.id.pfl_full);
        et_telp = findViewById(R.id.pfl_telp);
        et_username = findViewById(R.id.pfl_username);
        et_id = findViewById(R.id.pfl_id);
        et_id2 = findViewById(R.id.pfl_id2);
        et_id3 = findViewById(R.id.pfl_id3);
        et_stat = findViewById(R.id.pfl_stat);
        btn_image = findViewById(R.id.btn_image);
        profile_image = findViewById(R.id.p);

        sp = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE);


        navigationView.bringToFront();

        setSupportActionBar(toolbar);
        navigationView.setCheckedItem(R.id.nav_profile);

        //open drawer di strings.xml
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        String profileS = sp.getString(IMAGE, null);

        //untuk menampilkan image setelah login
        if(!(profileS.isEmpty())) {
            String imageUri = profileS;
            ImageView Image1 = (ImageView) findViewById(R.id.p);
            ImageView Image2 = pfph;
            Picasso.get().load(imageUri).into(Image1);
            Picasso.get().load(imageUri).into(Image2);
        }

        //untuk menampilkan image setelah diupload
        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            profile_image.setImageBitmap(bitmap);
            pfph.setImageBitmap(bitmap);
        }

        nama = header.findViewById(R.id.fname);
        name = findViewById(R.id.name);

        nama.setText(sp.getString(FULLNAME, null));
        name.setText(sp.getString(FULLNAME, null));

        et_email.setText(sp.getString(EMAIL, null));
        et_npk.setText(sp.getString(NPK, null));
        et_fullname.setText(sp.getString(FULLNAME, null));
        et_telp.setText(sp.getString(TELP, null));
        et_username.setText(sp.getString(USERNAME, null));
        et_id.setText(sp.getString(ID, null));
        et_id2.setText(sp.getString(ID, null));
        et_id3.setText(sp.getString(ID, null));

        //untuk status active atau offline
        status = (Switch) findViewById(R.id.status);

        String sta = sp.getString(STAT, null);

        if (sta.equals("1")){
            status.setChecked(sp.getBoolean("switch",true));
            et_stat.setText("0");

            et_stat.setText(sp.getString(STAT, null));
        }
        else{
            status.setChecked(sp.getBoolean("switch",false));
            et_stat.setText("1");

            et_stat.setText(sp.getString(STAT, null));
        }

        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp.edit().putBoolean("switch", true).apply();
                    sp.edit().putString(STAT, "1").apply();

                    et_stat.setText(sp.getString(STAT, null));

                    String id = et_id3.getText().toString();
                    String stat = et_stat.getText().toString();

                    statupdate1(id, stat);

                } else {
                    sp.edit().putBoolean("switch", false).apply();
                    sp.edit().putString(STAT, "0").apply();

                    et_stat.setText(sp.getString(STAT, null));

                    String id = et_id3.getText().toString();
                    String stat = et_stat.getText().toString();

                    statupdate0(id, stat);

                }
            }
        });

        //untuk update profile
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = et_fullname.getText().toString();
                String email = et_email.getText().toString();
                String npk = et_npk.getText().toString();
                String telp = et_telp.getText().toString();
                String username = et_username.getText().toString();
                String Id = et_id.getText().toString();

                update(fullname, email, npk, username, telp, Id);
            }
        });

        //untuk update gambar profile
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosefile();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Profile.this, Dashboard.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent1 = new Intent(Profile.this, Dashboard.class);
                startActivity(intent1);
                break;

            case R.id.nav_profile:
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean(SHARE_PREF_NAME, false).apply();
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(Profile.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                break;

            case R.id.nav_grafik:
                Intent intent2 = new Intent(Profile.this, Graph.class);
                startActivity(intent2);
                break;

            case R.id.nav_contact:
                Intent intent3 = new Intent(Profile.this, Contact.class);
                startActivity(intent3);
                break;

            case R.id.nav_help:
                Intent intent4 = new Intent(Profile.this, Help.class);
                startActivity(intent4);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //untuk update profile
    private void update(String username, String fullname, String email, String npk, String no_telp, String id) {

        Call<UserData> UpdateDataCall = apiInterface.profileResponse(username, fullname, email, npk, no_telp, id);
        UpdateDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
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

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(NPK,response.body().getNpk());
                    editor.putString(USERNAME,response.body().getUsername());
                    editor.putString(FULLNAME,response.body().getFullname());
                    editor.putString(EMAIL,response.body().getEmail());
                    editor.putString(TELP,response.body().getNo_telp());
                    editor.apply();

                    //Toast.makeText(getApplicationContext(), sp.getString(ID, null),Toast.LENGTH_SHORT).show();

                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                    userData = response.body();

                } else {
                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Profile.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                this.profile_image.setImageBitmap(bitmap);
                View header = navigationView.getHeaderView(0);
                ImageView pfph = (ImageView) header.findViewById(R.id.pfph);
                pfph.setImageBitmap(bitmap);

            } catch (IOException e){
                e.printStackTrace();
            }
            String iD = et_id2.getText().toString();
            UploadPicture(iD,  getStringImage(bitmap));

        }
    }

    private void UploadPicture(String id, String image) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

            Call<UserData> imageDataCall = apiInterface.imageResponse(id, image);
            imageDataCall.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    progressDialog.dismiss();
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

                        sp.edit().putString(IMAGE, response.body().getImage()).apply();
//                      Toast.makeText(Profile.this, id, Toast.LENGTH_SHORT).show();

                        Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                        userData = response.body();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Profile.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

//      Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        return encodedImage;
    }

    //untuk update status
    private void statupdate1(String id, String stat) {

        Call<UserData> statDataCall = apiInterface.userstatResponse(id, stat);
        statDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
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

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(STAT,response.body().getStat());
                    editor.apply();

                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                    userData = response.body();

                } else {
                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Profile.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void statupdate0(String id, String stat) {

        Call<UserData> statDataCall = apiInterface.userstatResponse(id, stat);
        statDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
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

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(STAT,response.body().getStat());
                    editor.apply();

                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                    userData = response.body();

                } else {
                    Toast.makeText(Profile.this, sr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Profile.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
