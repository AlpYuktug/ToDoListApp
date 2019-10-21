package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.todolistapp.R;

import java.io.File;

public class OpenPDF extends AppCompatActivity {

    private static final int BACKUP_FILE_REQUEST_CODE = 1000;

    public PDFView pdfView;
    public ImageView imageViewShare;

    public File docsFolder,pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);

        pdfView = findViewById(R.id.pdfView);
        imageViewShare = findViewById(R.id.imageViewShare);

        docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        pdfFile = new File(docsFolder.getAbsolutePath(), "ToDoList.pdf");


        pdfView.fromFile(pdfFile).load();

        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(OpenPDF.this, OpenPDF.this.getPackageName() + ".provider", pdfFile);Intent share = new Intent(android.content.Intent.ACTION_SEND);
                Intent shares = new Intent(Intent.ACTION_SEND);
                shares.putExtra(Intent.EXTRA_STREAM, uri);
                shares.setType("message/rfc822");
                startActivity(shares);
            }
        });

    }
}
