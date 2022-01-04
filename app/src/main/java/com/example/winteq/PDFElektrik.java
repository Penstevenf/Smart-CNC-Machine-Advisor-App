package com.example.winteq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class PDFElektrik extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pdf_elektrik);

        final PDFView pdfView = findViewById(R.id.pdfView_elc);
        ProgressDialog pd = new ProgressDialog(PDFElektrik.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        //UNPACK DATA FROM INTENT
        Intent i = this.getIntent();
        final String path = i.getExtras().getString("PATH");

        FileLoader.with(this)
                .load(path, false)
                .fromDirectory("help_elektrik_pdf", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        File pdfFile = response.getBody();
                        pd.dismiss();
                        try {
                            pdfView.fromFile(pdfFile)
                                    .defaultPage(1)
                                    .enableAnnotationRendering(true)
                                    .onLoad(PDFElektrik.this)
                                    .scrollHandle(new DefaultScrollHandle(PDFElektrik.this))
                                    .spacing(10) //in dp
                                    .onPageError(PDFElektrik.this)
                                    .load();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(PDFElektrik.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(PDFElektrik.this, String.valueOf(nbPages)+" Pages", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(PDFElektrik.this, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}