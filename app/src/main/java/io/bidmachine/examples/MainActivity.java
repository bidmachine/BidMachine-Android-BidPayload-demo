package io.bidmachine.examples;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.bidmachine.BidMachine;
import io.bidmachine.banner.BannerListener;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerSize;
import io.bidmachine.banner.BannerView;
import io.bidmachine.interstitial.InterstitialAd;
import io.bidmachine.interstitial.InterstitialListener;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.rewarded.RewardedAd;
import io.bidmachine.rewarded.RewardedListener;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.utils.BMError;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BID_MACHINE_SELLER_ID = "5";

    private Button bInitialize;
    private Button bLoadBanner;
    private Button bShowBanner;
    private Button bLoadMrec;
    private Button bShowMrec;
    private Button bLoadInterstitial;
    private Button bShowInterstitial;
    private Button bLoadRewarded;
    private Button bShowRewarded;
    private FrameLayout adContainer;

    private BannerView bannerView;
    private BannerView mrecView;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bInitialize = findViewById(R.id.bInitialize);
        bInitialize.setOnClickListener(v -> initialize());
        bLoadBanner = findViewById(R.id.bLoadBanner);
        bLoadBanner.setOnClickListener(v -> loadBanner());
        bShowBanner = findViewById(R.id.bShowBanner);
        bShowBanner.setOnClickListener(v -> showBanner());
        bLoadMrec = findViewById(R.id.bLoadMrec);
        bLoadMrec.setOnClickListener(v -> loadMrec());
        bShowMrec = findViewById(R.id.bShowMrec);
        bShowMrec.setOnClickListener(v -> showMrec());
        bLoadInterstitial = findViewById(R.id.bLoadInterstitial);
        bLoadInterstitial.setOnClickListener(v -> loadInterstitial());
        bShowInterstitial = findViewById(R.id.bShowInterstitial);
        bShowInterstitial.setOnClickListener(v -> showInterstitial());
        bLoadRewarded = findViewById(R.id.bLoadRewarded);
        bLoadRewarded.setOnClickListener(v -> loadRewarded());
        bShowRewarded = findViewById(R.id.bShowRewarded);
        bShowRewarded.setOnClickListener(v -> showRewarded());

        adContainer = findViewById(R.id.adContainer);
    }

    private void initialize() {
        Log.d(TAG, "initialize");

        // Initialize BidMachine SDK first
        BidMachine.setLoggingEnabled(true);
        BidMachine.initialize(this, BID_MACHINE_SELLER_ID);

        bInitialize.setEnabled(false);
        enableLoadButton();
    }

    private void enableLoadButton() {
        bLoadBanner.setEnabled(true);
        bLoadMrec.setEnabled(true);
        bLoadInterstitial.setEnabled(true);
        bLoadRewarded.setEnabled(true);
    }

    private void addAdView(View view) {
        adContainer.removeAllViews();
        adContainer.addView(view);
    }

    private boolean isBidPayloadInvalid(String bidPayload) {
        if (TextUtils.isEmpty(bidPayload)) {
            Log.d(TAG, "idPayload is null or empty");
            Toast.makeText(this, "bidPayload is null or empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void loadBanner() {
        bShowBanner.setEnabled(false);

        // Destroy previous ad
        destroyBanner();

        String bidPayload = Utils.readFile(this, "banner_bid_payload.txt");
        if (isBidPayloadInvalid(bidPayload)) {
            return;
        }

        BannerRequest bannerRequest = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .setBidPayload(bidPayload)
                .build();
        bannerView = new BannerView(this);
        bannerView.setListener(new BannerViewListener());
        bannerView.load(bannerRequest);

        Log.d(TAG, "loadBanner");
    }

    private void showBanner() {
        Log.d(TAG, "showBanner");

        bShowBanner.setEnabled(false);

        // Check if an ad can be shown before actual impression
        if (bannerView != null && bannerView.canShow() && bannerView.getParent() == null) {
            addAdView(bannerView);
        } else {
            Log.d(TAG, "show error - banner object is null");
        }
    }

    private void destroyBanner() {
        Log.d(TAG, "destroyBanner");

        adContainer.removeAllViews();
        if (bannerView != null) {
            bannerView.setListener(null);
            bannerView.destroy();
            bannerView = null;
        }
    }

    private void loadMrec() {
        bShowMrec.setEnabled(false);

        // Destroy previous ad
        destroyMrec();

        String bidPayload = Utils.readFile(this, "mrec_bid_payload.txt");
        if (isBidPayloadInvalid(bidPayload)) {
            return;
        }

        BannerRequest bannerRequest = new BannerRequest.Builder()
                .setSize(BannerSize.Size_300x250)
                .setBidPayload(bidPayload)
                .build();
        mrecView = new BannerView(this);
        mrecView.setListener(new MrecViewListener());
        mrecView.load(bannerRequest);

        Log.d(TAG, "loadMrec");
    }

    private void showMrec() {
        Log.d(TAG, "showMrec");

        bShowMrec.setEnabled(false);

        // Check if an ad can be shown before actual impression
        if (mrecView != null && mrecView.canShow() && mrecView.getParent() == null) {
            addAdView(mrecView);
        } else {
            Log.d(TAG, "show error - mrec object is null");
        }
    }

    private void destroyMrec() {
        Log.d(TAG, "destroyMrec");

        adContainer.removeAllViews();
        if (mrecView != null) {
            mrecView.setListener(null);
            mrecView.destroy();
            mrecView = null;
        }
    }

    private void loadInterstitial() {
        bShowInterstitial.setEnabled(false);

        // Destroy previous ad
        destroyInterstitial();

        String bidPayload = Utils.readFile(this, "interstitial_bid_payload.txt");
        if (isBidPayloadInvalid(bidPayload)) {
            return;
        }

        InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
                .setBidPayload(bidPayload)
                .build();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setListener(new InterstitialAdListener());
        interstitialAd.load(interstitialRequest);

        Log.d(TAG, "loadInterstitial");
    }

    private void showInterstitial() {
        Log.d(TAG, "showInterstitial");

        bShowInterstitial.setEnabled(false);

        // Check if an ad can be shown before actual impression
        if (interstitialAd != null && interstitialAd.canShow()) {
            interstitialAd.show();
        } else {
            Log.d(TAG, "show error - interstitial object not loaded");
        }
    }

    private void destroyInterstitial() {
        Log.d(TAG, "destroyInterstitial");

        if (interstitialAd != null) {
            interstitialAd.setListener(null);
            interstitialAd.destroy();
            interstitialAd = null;
        }
    }

    private void loadRewarded() {
        bShowRewarded.setEnabled(false);

        // Destroy previous ad
        destroyRewarded();

        String bidPayload = Utils.readFile(this, "rewarded_bid_payload.txt");
        if (isBidPayloadInvalid(bidPayload)) {
            return;
        }

        RewardedRequest rewardedRequest = new RewardedRequest.Builder()
                .setBidPayload(bidPayload)
                .build();

        rewardedAd = new RewardedAd(this);
        rewardedAd.setListener(new RewardedAdListener());
        rewardedAd.load(rewardedRequest);

        Log.d(TAG, "loadRewarded");
    }

    private void showRewarded() {
        Log.d(TAG, "showRewarded");

        bShowRewarded.setEnabled(false);

        // Check if an ad can be shown before actual impression
        if (rewardedAd != null && rewardedAd.canShow()) {
            rewardedAd.show();
        } else {
            Log.d(TAG, "show error - rewarded object not loaded");
        }
    }

    private void destroyRewarded() {
        Log.d(TAG, "destroyRewarded");

        if (rewardedAd != null) {
            rewardedAd.setListener(null);
            rewardedAd.destroy();
            rewardedAd = null;
        }
    }


    private class BannerViewListener implements BannerListener {

        @Override
        public void onAdLoaded(@NonNull BannerView bannerView) {
            bShowBanner.setEnabled(true);

            Log.d(TAG, "BannerViewListener - onAdLoaded");
            Toast.makeText(MainActivity.this, "Banner Loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoadFailed(@NonNull BannerView bannerView, @NonNull BMError bmError) {
            Log.d(TAG, "BannerViewListener - onAdLoadFailed: " + bmError.getMessage());
            Toast.makeText(MainActivity.this, "Banner LoadFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdShown(@NonNull BannerView bannerView) {
            Log.d(TAG, "BannerViewListener - onAdShown");
        }

        @Override
        public void onAdImpression(@NonNull BannerView bannerView) {
            Log.d(TAG, "BannerViewListener - onAdImpression");
        }

        @Override
        public void onAdClicked(@NonNull BannerView bannerView) {
            Log.d(TAG, "BannerViewListener - onAdClicked");
        }

        @Override
        public void onAdExpired(@NonNull BannerView bannerView) {
            Log.d(TAG, "BannerViewListener - onAdExpired");
        }

    }

    private class MrecViewListener implements BannerListener {

        @Override
        public void onAdLoaded(@NonNull BannerView bannerView) {
            bShowMrec.setEnabled(true);

            Log.d(TAG, "MrecViewListener - onAdLoaded");
            Toast.makeText(MainActivity.this, "Mrec Loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoadFailed(@NonNull BannerView bannerView, @NonNull BMError bmError) {
            Log.d(TAG, "MrecViewListener - onAdLoadFailed: " + bmError.getMessage());
            Toast.makeText(MainActivity.this, "Mrec LoadFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdShown(@NonNull BannerView bannerView) {
            Log.d(TAG, "MrecViewListener - onAdShown");
        }

        @Override
        public void onAdImpression(@NonNull BannerView bannerView) {
            Log.d(TAG, "MrecViewListener - onAdImpression");
        }

        @Override
        public void onAdClicked(@NonNull BannerView bannerView) {
            Log.d(TAG, "MrecViewListener - onAdClicked");
        }

        @Override
        public void onAdExpired(@NonNull BannerView bannerView) {
            Log.d(TAG, "MrecViewListener - onAdExpired");
        }

    }

    private class InterstitialAdListener implements InterstitialListener {

        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            bShowInterstitial.setEnabled(true);

            Log.d(TAG, "InterstitialAdListener - onAdLoaded");
            Toast.makeText(MainActivity.this, "Interstitial Loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoadFailed(@NonNull InterstitialAd interstitialAd,
                                   @NonNull BMError bmError) {
            Log.d(TAG, "InterstitialAdListener - onAdLoadFailed: " + bmError.getMessage());
            Toast.makeText(MainActivity.this, "Interstitial LoadFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdShown(@NonNull InterstitialAd interstitialAd) {
            Log.d(TAG, "InterstitialAdListener - onAdShown");
        }

        @Override
        public void onAdShowFailed(@NonNull InterstitialAd interstitialAd,
                                   @NonNull BMError bmError) {
            Log.d(TAG, "InterstitialAdListener - onAdShowFailed");
        }

        @Override
        public void onAdImpression(@NonNull InterstitialAd interstitialAd) {
            Log.d(TAG, "InterstitialAdListener - onAdImpression");
        }

        @Override
        public void onAdClicked(@NonNull InterstitialAd interstitialAd) {
            Log.d(TAG, "InterstitialAdListener - onAdClicked");
        }

        @Override
        public void onAdClosed(@NonNull InterstitialAd interstitialAd, boolean b) {
            Log.d(TAG, "InterstitialAdListener - onAdClosed");
        }

        @Override
        public void onAdExpired(@NonNull InterstitialAd interstitialAd) {
            Log.d(TAG, "InterstitialAdListener - onAdExpired");
        }

    }

    private class RewardedAdListener implements RewardedListener {

        @Override
        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
            bShowRewarded.setEnabled(true);

            Log.d(TAG, "RewardedAdListener - onAdLoaded");
            Toast.makeText(MainActivity.this, "Rewarded Loaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoadFailed(@NonNull RewardedAd rewardedAd, @NonNull BMError bmError) {
            Log.d(TAG, "RewardedAdListener - onAdLoadFailed: " + bmError.getMessage());
            Toast.makeText(MainActivity.this, "Rewarded LoadFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdShown(@NonNull RewardedAd rewardedAd) {
            Log.d(TAG, "RewardedAdListener - onAdShown");
        }

        @Override
        public void onAdShowFailed(@NonNull RewardedAd rewardedAd, @NonNull BMError bmError) {
            Log.d(TAG, "RewardedAdListener - onAdShowFailed");
        }

        @Override
        public void onAdImpression(@NonNull RewardedAd rewardedAd) {
            Log.d(TAG, "RewardedAdListener - onAdImpression");
        }

        @Override
        public void onAdClicked(@NonNull RewardedAd rewardedAd) {
            Log.d(TAG, "RewardedAdListener - onAdClicked");
        }

        @Override
        public void onAdClosed(@NonNull RewardedAd rewardedAd, boolean b) {
            Log.d(TAG, "RewardedAdListener - onAdClosed");
        }

        @Override
        public void onAdRewarded(@NonNull RewardedAd rewardedAd) {
            Log.d(TAG, "RewardedAdListener - onAdRewarded");
        }

        @Override
        public void onAdExpired(@NonNull RewardedAd rewardedAd) {
            Log.d(TAG, "RewardedAdListener - onAdExpired");
        }

    }

}