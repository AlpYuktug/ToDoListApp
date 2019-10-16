package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.todolistapp.R;

public class Login extends AppCompatActivity {

    public ImageView imageViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageViewLogin = findViewById(R.id.imageViewLogin);
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intentHome = new Intent(Login.this,Home.class);
                startActivity(intentHome);
            }
        });
    }
}
