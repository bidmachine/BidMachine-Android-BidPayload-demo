# BidMachine SDK Android BidPayload Examples

* [Banner](#banner)
* [Mrec](#mrec)
* [Interstitial](#interstitial)
* [Rewarded](#rewarded)

## Banner
```java
// BidPayload from server
String bidPayload = "<bid_payload>";

// Create new BannerRequest instance with BidPayload
BannerRequest bannerRequest = new BannerRequest.Builder()
        .setSize(BannerSize.Size_320x50)
        .setBidPayload(bidPayload)
        .build();

// Create new BannerView instance and load it with BannerRequest
BannerView bannerView = new BannerView(context);
bannerView.setListener(bannerListener);
bannerView.load(bannerRequest);
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L108)

## Mrec
```java
// BidPayload from server
String bidPayload = "<bid_payload>";

// Create new BannerRequest instance with BidPayload
BannerRequest bannerRequest = new BannerRequest.Builder()
        .setSize(BannerSize.Size_300x250)
        .setBidPayload(bidPayload)
        .build();
 
// Create new BannerView instance and load it with BannerRequest
BannerView bannerView = new BannerView(context);
bannerView.setListener(bannerListener);
bannerView.load(bannerRequest);
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L154)

## Interstitial
```java
// BidPayload from server
String bidPayload = "<bid_payload>";

// Create new InterstitialRequest instance with BidPayload
InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
        .setBidPayload(bidPayload)
        .build();

// Create new InterstitialAd instance and load it with InterstitialRequest
InterstitialAd interstitialAd = new InterstitialAd(context);
interstitialAd.setListener(interstitialListener);
interstitialAd.load(interstitialRequest);
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L200)

## Rewarded
```java
// BidPayload from server
String bidPayload = "<bid_payload>";

// Create new RewardedRequest instance with BidPayload
RewardedRequest rewardedRequest = new RewardedRequest.Builder()
        .setBidPayload(bidPayload)
        .build();

// Create new RewardedAd instance and load it with RewardedRequest
RewardedAd rewardedAd = new RewardedAd(context);
rewardedAd.setListener(rewardedListener);
rewardedAd.load(rewardedRequest);
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L245)
