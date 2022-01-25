package com.example.winteq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

public class ReplaceItem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Call Variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sp;

    Connection con;
    Statement stmt;
    ImageView ipic;
    Button up;
    EditText line,station,machine,pic,part,qty,desc,part2,qty2;
    private static final int RESULT_LOAD_IMAGE = 1;
    byte[] byteArray;
    String encodedImage;
    FloatingActionButton riad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove top
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_replace_item);

        //Find Variable
        drawerLayout = findViewById(R.id.replaceitem); //change xml id
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        line = findViewById(R.id.pline);
        station = findViewById(R.id.pstation);
        machine = findViewById(R.id.pmachine);
        pic = findViewById(R.id.ppic);
        desc = findViewById(R.id.itemdesc);
        part = findViewById(R.id.partreq);
        qty = findViewById(R.id.pqty);
        ipic = findViewById(R.id.itempic);
        up = findViewById(R.id.upreq);
        riad = findViewById(R.id.req);
        part2 = findViewById(R.id.partreq2);
        qty2 = findViewById(R.id.pqty2);

        //Setup Shared Preference
        sp = getSharedPreferences("login", MODE_PRIVATE);

        sp.edit().putString("papicc",null).apply();

        //Setup Header
        View header = navigationView.getHeaderView(0);
        TextView nama = (TextView) header.findViewById(R.id.fname);
        ImageView pfph = (ImageView) header.findViewById(R.id.pfph);

        //Setup Toolbar
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Input Name and Profile to Drawer
        nama.setText(sp.getString("full", ""));
        String profileS = sp.getString("pfp", "");
        byte[] bytes = Base64.decode(profileS,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            pfph.setImageBitmap(bitmap);
        }

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Opening the Gallery and selecting media
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&& !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE );
                    // this will jump to onActivity Function after selecting image
                }
                else
                {
                    Toast.makeText(ReplaceItem.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        riad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new preq().execute("");
//            }
//        });
    }

//    public class preq extends AsyncTask<String, String , String> {
//
//        String z = "";
//        Boolean isSuccess = false;
//
//        @Override
//        protected void onPreExecute() {
//
//            Toast.makeText(ReplaceItem.this , "Connecting" , Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Toast.makeText(ReplaceItem.this , "Add Succesful" , Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(ReplaceItem.this, Dashboard.class);
//            startActivity(intent);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            connectSQL c = new connectSQL();
//
//            String ln = line.getText().toString();
//            String st = station.getText().toString();
//            String mc = machine.getText().toString();;
//            String pc = pic.getText().toString();
//            String sa = "Waiting";
//            String im = sp.getString("papicc","");
//            String pb = desc.getText().toString();
//            String pa = part.getText().toString();
//            String qt = qty.getText().toString();
//            String ty = "Part Replace";
//            String p2 = part2.getText().toString();
//            String q2 = qty2.getText().toString();
//
//            try{
//                con = c.connectionclass();         // Connect to database
//                if(con == null){
//                    z = "Check Your Internet Connection";
//                }
//                else{
//                    String sql = "Insert Into Historydb (Line, Station, Machine, PIC1, Status, Image1, Problem, Part1, QTY1, Type, Part2, QTY2) values ('"+ln+"','"+st+"','"+mc+"','"+pc+"','"+sa+"','"+im+"','"+pb+"','"+pa+"','"+qt+"','"+ty+"','"+p2+"','"+q2+"');";
//                    stmt = con.createStatement();
//                    stmt.executeUpdate(sql);
//                }
//            }
//            catch (Exception e){
//                isSuccess = false;
//                z = e.getMessage();
//            }
//            return z;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK  && null != data)
        {
            // getting the selected image, setting in imageview and converting it to byte and base 64
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            Toast.makeText(ReplaceItem.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try
            {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e.getMessage().toString());
            }
            if (originBitmap != null)
            {
                this.ipic.setImageBitmap(originBitmap);
                Log.w("Image Setted in", "Done Loading Image");
                try
                {
                    Bitmap image = ((BitmapDrawable) ipic.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    sp.edit().putString("papicc",encodedImage).apply();
                }
                catch (Exception e)
                {
                    Log.w("OOooooooooo","exception");
                }
                Toast.makeText(ReplaceItem.this, "Conversion Done",Toast.LENGTH_SHORT).show();
            }
            // End getting the selected image, setting in imageview and converting it to byte and base 64
        }
        else
        {
            System.out.println("Error Occured");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(ReplaceItem.this, TechnicalSupport.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(ReplaceItem.this, Dashboard.class);
                startActivity(intent);
                break;

            case R.id.nav_profile:
                Intent intent1 = new Intent(ReplaceItem.this, Profile.class);
                startActivity(intent1);
                break;

            case R.id.nav_logout:
                sp.edit().putBoolean("logged", false).apply();
                Intent intent2 = new Intent(ReplaceItem.this, Login.class);
                startActivity(intent2);
                break;

            case R.id.nav_grafik:
                Intent intent3 = new Intent(ReplaceItem.this, Graph.class);
                startActivity(intent3);
                break;

            case R.id.nav_contact:
                Intent intent4 = new Intent(ReplaceItem.this, Contact.class);
                startActivity(intent4);
                break;

            case R.id.nav_help:
                Intent intent5 = new Intent(ReplaceItem.this, Help.class);
                startActivity(intent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}