---
title: "Android SDK Guidelines"
priority: 3
group: "mobile_sdk"
---
## OVERVIEW
TrustVision SDK is an android SDK for TrustVision Engine. It provides these features:

- Capture ID and Selfie.
- ID and selfie matching.
- Liveness checking.
- OCR and also QR scanner.


## Specifications
* Gradle Version 3.3
* Tested with Gradle Plugin for Android Studio - version 3.4.1
* minSdkVersion 21
* targetSdkVersion 28
* <b>NOTICE:</b> use Android X support library, please check [Migrate to Android X](https://developer.android.com/jetpack/androidx/migrate)
</br>(will update document later for older support library)

## Table Of Contents
- [Overview](#overview)
	- [Specifications](#specifications)
- [Table Of Contents](#table-of-contents)
- [Example Project](#example-project)
- [Integration Steps](#integration-steps)
	- [1. Adding the SDK to your project](#1-adding-the-sdk-to-your-project)
	- [2. Initialize SDK](#2-initialize-sdk)
	- [3. Start the SDK](#3-start-the-sdk)
	- [4. Handle the Result](#4-handle-the-result)
- [API References](#api-references)		


## Example Project
- Please refer to the sample project provided in the **Example** to get an understanding of the implementation process.
- Set **YOUR\_APP\_KEY** and **YOUR\_APP\_SECRET** in the `MainActivity.java` class

```java
TrustVisionSDK.init(this, YOUR_APP_KEY, YOUR_APP_SECRET, this);
```
- Build and run the example app


## Installing SDK

###1. Adding the SDK to your project

* Get library file `tv_sdk.zip`, extract and put all files into a folder in android project.</br>
Example: folder `${project.rootDir}/repo` 

```
	app
	root
	+--com
		+--trustvision
			+--tv_api_sdk
				+--1.0.0
					+--tv_api_sdk-1.0.0.aar
					+--tv_api_sdk-1.0.0.pom
				maven-metadata.xml
			+--tv_sdk
	...					
```

* Add the following set of lines to the Project (top-level) `build.gradle`

```groovy
  repositories {
       maven { url "https://maven.google.com" }
       maven { url "path/to/tvsdk/folder" }
       ...
  }
```
Example: `maven { url "${project.rootDir}/repo" }`

* Add the following set of lines to your `app/build.gradle`

```groovy
  android {
      ...
      sourceCompatibility JavaVersion.VERSION_1_8
      targetCompatibility JavaVersion.VERSION_1_8
  } 

  dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.1.0-alpha02'
    implementation('com.trustvision:tv_sdk:1.0.0@aar') {
        transitive = true
    }
    testImplementation 'junit:junit:4.12'

  } 
```
**If your app does not use AndroidX, please migrate to androidX first. Tutorial about Migrate to AndroidX can be found from [here](https://developer.android.com/jetpack/androidx/migrate)**

* Add to `gradle.properties`

```groovy
android.useAndroidX=true
android.enableJetifier=true
```


		 
###2. Initialize SDK
To initialize the SDK, add the following line to your app before calling any of the SDK functionalities. For example,  you can add it to the `onCreate` method of the application class.
 
```java
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrustVisionSDK.init(this, appKey, appSecret, new TrustVisionSDK.TVInitializeListener() {
            @Override
            public void onInitSuccess() {

            }

            @Override
            public void onInitError() {

            }
        });
    }
```
Please note: The SDK requires **Camera** permissions to capture images.Camera permissions will be handled by the SDK if not already handled by the app.
###3. Start the SDK
The SDK provides some built in Activities, to start the SDK:

1. Set config parameters 

```java
   FaceSDKConfiguration.Builder builder = new FaceSDKConfiguration.Builder();
   builder.setEnableSound(true)
          .setActionMode(TVActionMode.FULL)
          .setCardType(selectedCard);
```

2. Start SDK from configuration

```java
   TrustVisionSDK.startTrustVisionSDK(activity, builder.build());
```

**Important**

1. Action Modes: We're currently support 4 modes: Check TVActionMode enums:
   - **FULL**: The SDK will require to take ID((front, back or both depend on ID type), then take sefie with liveness checking. SDK will do face comparison and extract information from the ID. 
   - **FACE_MATCHING**: The SDK will require to take ID(front only), then take sefie without liveness checking. SDK will do face comparison only.
   - **LIVENESS**: The SDK will require to take selfie then do liveness checking only.
   - **OCR**: The SDK will require to take ID((front, back or both depend on ID type) then do OCR only
2. Card Types: We supports limited ID card types for each country. Please go to dashboard and add ID card types which you want to be add to your app. Then use this method to get list card types
`TrustVisionSDK.getCardTypes()`
3. Camera options: When capturing selfie, we can choose front camera or back camera and it could be config by setting from dashboard. Use this method to get list card types


###4. Handle the Result
The result will be sent to `onActivityResult` method.

```java
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            TVDetectionResult result = (TVDetectionResult) data.getSerializableExtra(TrustVisionSDK.TV_RESULT);
            if (result.getError() != null) {
                Toast.makeText(this, result.getError().getDetailErrorDescription(), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, SuccessActivity.class);
                intent.putExtra(TrustVisionSDK.TV_RESULT, result);
                startActivity(intent);
            }
        }
    }
```

<b>TVDetectionResult</b> is result of TrustVisionSDK and can be serialized with key `TrustVisionSDK.TV_RESULT`

```java
TVDetectionResult result = (TVDetectionResult) data.getSerializableExtra(TrustVisionSDK.TV_RESULT);
```
**TVDetectionResult** is a general result whichs contain results from Liveness Checking, Face Comparison and ID Card Information Extraction.

* `getLivenessResult()` : return **TVLivenessResult**. It is the result from liveness checking with score. If the action mode is FACE_MATCHING or OCR, the result will be empty.
* `getFaceCompareResult()`: return **TVCompareFacesResul**. It is result from liveness checking and contains id image, selfie image, matched or not matching, score matching. If the action mode is LIVENESS or OCR, the result will be empty
* `getCardInfoResult()`: return **TVCardInfoResult**. It constaint card information.
* `getError()`: TVDetectionError is error object returned in case of failure in Request Permission. Please always call this method to check error first before getting the result.

## API References
The api references can be found from [here]()
