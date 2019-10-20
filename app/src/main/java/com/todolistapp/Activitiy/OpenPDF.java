package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.github.barteksc.pdfviewer.PDFView;
import com.todolistapp.R;

public class OpenPDF extends AppCompatActivity {

    public PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        pdfView = findViewById(R.id.pdfView);

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        String targetPdf = directory_path+"todo.pdf";

        pdfView.fromAsset(targetPdf).load();

    }
}
