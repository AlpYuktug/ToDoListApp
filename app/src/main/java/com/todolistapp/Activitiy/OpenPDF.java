package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.github.barteksc.pdfviewer.PDFView;
import com.todolistapp.R;

import java.io.File;

public class OpenPDF extends AppCompatActivity {

    public PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        pdfView = findViewById(R.id.pdfView);

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        File pdfFile = new File(docsFolder.getAbsolutePath(), "ToDoList.pdf");


        pdfView.fromFile(pdfFile).load();

    }
}
