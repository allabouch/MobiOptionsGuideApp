package com.mobioptions.guideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

import java.util.concurrent.TimeUnit;

public class GridViewButton extends AppCompatActivity implements MaxAdListener {

    // Allabouch Mohamed: https://www.facebook.com/AllbSiMo : https://www.instagram.com/allabou.ch/ : If you have any problem contact me at instagram

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private int currentImageResource;
    private String currentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_grid_view_button);

        String interstitialAdUnitId = getString(R.string.interstitial_ad_unit_id);
        interstitialAd = new MaxInterstitialAd(interstitialAdUnitId, this);

        interstitialAd.setListener(this);
        interstitialAd.loadAd();

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> createNativeAd());

        // Define buttons and set onClick listeners
        // Example for button1
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip1);
            navigateToActivity(R.drawable.btn1, buttonText);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip2);
            navigateToActivity(R.drawable.btn2, buttonText);
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip3);
            navigateToActivity(R.drawable.btn3, buttonText);
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip4);
            navigateToActivity(R.drawable.btn4, buttonText);
        });

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip5);
            navigateToActivity(R.drawable.btn5, buttonText);
        });

        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip6);
            navigateToActivity(R.drawable.btn6, buttonText);
        });

        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip7);
            navigateToActivity(R.drawable.btn7, buttonText);
        });

        Button button8 = findViewById(R.id.button8);
        button8.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip8);
            navigateToActivity(R.drawable.btn8, buttonText);
        });

        Button button9 = findViewById(R.id.button9);
        button9.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip9);
            navigateToActivity(R.drawable.btn9, buttonText);
        });

        Button button10 = findViewById(R.id.button10);
        button10.setOnClickListener(v -> {
            String buttonText = getString(R.string.Tip10);
            navigateToActivity(R.drawable.btn10, buttonText);
        });

    }


    private void navigateToActivity(int imageResource, String text) {
        currentImageResource = imageResource;
        currentText = text;

        if (interstitialAd.isReady()) {
            interstitialAd.showAd();
        } else {
            startDetailActivity(imageResource, text);
        }
    }

    private void startDetailActivity(int imageResource, String text) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("imageResource", imageResource);
        intent.putExtra("text", text);
        startActivity(intent);
    }

    void createNativeAd() {
        FrameLayout nativeAdContainer = findViewById(R.id.native_ad_layout);
        String nativeAdUnitId = getString(R.string.native_ad_unit_id);
        nativeAdLoader = new MaxNativeAdLoader(nativeAdUnitId, this);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }
                nativeAd = ad;
                nativeAdContainer.removeAllViews();
                nativeAdContainer.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
            }
        });
        nativeAdLoader.loadAd();
    }

    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
    }

    @Override
    public void onAdHidden(final MaxAd maxAd) {
        startDetailActivity(currentImageResource, currentText);
        interstitialAd.loadAd();
    }
}
