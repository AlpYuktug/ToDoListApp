package com.todolistapp.Activitiy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.todolistapp.R;

public class AnimationForOpenApp extends AppCompatActivity {

    LottieAnimationView animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_for_open_app);
        animation_view = findViewById(R.id.animation_view);

        animation_view.playAnimation();

        Thread myThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    sleep(5000);
                    Intent mainIntent = new Intent(AnimationForOpenApp.this,
                            Login.class);
                    startActivity(mainIntent);
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
