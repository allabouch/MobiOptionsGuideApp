package com.mobioptions.guideapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Activity_2 extends AppCompatActivity implements MaxAdListener {

    // Allabouch Mohamed: https://www.facebook.com/AllbSiMo : https://www.instagram.com/allabou.ch/ : If you have any problem contact me at instagram

    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;

    // OnCreate - Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_2);

        // Initialize interstitial ad
        String interstitialAdUnitId = getString(R.string.interstitial_ad_unit_id);
        interstitialAd = new MaxInterstitialAd(interstitialAdUnitId, this);
        interstitialAd.setListener(this);
        interstitialAd.loadAd();

        // Initialize AppLovin SDK
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> createNativeAd());
    }

    // Button Click Event to Navigate to Next Activity
    public void GoNext(View view) {
        if (interstitialAd.isReady()) {
            interstitialAd.showAd();
        } else {
            navigateToNextActivity();
        }
    }

    // Navigate to the next activity
    private void navigateToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), Activity_3.class);
        startActivity(intent);
    }

    // Create Native Ad
    void createNativeAd() {
        FrameLayout nativeAdContainer = findViewById(R.id.native_ad_layout);

        // Load native ad
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
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {}

            @Override
            public void onNativeAdClicked(final MaxAd ad) {}
        });

        nativeAdLoader.loadAd();
    }

    // Interstitial Ad Loaded Event
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        retryAttempt = 0;
    }

    // Interstitial Ad Load Failed Event
    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
    }

    // Interstitial Ad Display Failed Event
    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        interstitialAd.loadAd();
    }

    // Interstitial Ad Displayed Event
    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    // Interstitial Ad Clicked Event
    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    // Interstitial Ad Hidden Event - Transition to next activity
    @Override
    public void onAdHidden(final MaxAd maxAd) {
        navigateToNextActivity();
        interstitialAd.loadAd();
    }
}