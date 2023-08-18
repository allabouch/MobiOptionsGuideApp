package com.mobioptions.guideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    // Allabouch Mohamed: https://www.facebook.com/AllbSiMo : https://www.instagram.com/allabou.ch/ : If you have any problem contact me at instagram

    private boolean areAdsReady = false; // Declare a boolean variable to track ads status
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView splashImage = findViewById(R.id.splash_image);
        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        splashImage.startAnimation(splashAnimation);

        // Delay for a few seconds, then start the main activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}