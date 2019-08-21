package com.trustvision.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trustingsocial.tvsdk.FaceSDKConfiguration;
import com.trustingsocial.tvsdk.TVDetectionResult;
import com.trustingsocial.tvsdk.internal.TrustVisionSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TrustVisionSDK.init(this, new TrustVisionSDK.TVInitializeListener() {
            @Override
            public void onInitSuccess() {

            }

            @Override
            public void onInitError() {

            }
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceSDKConfiguration.Builder builder = new FaceSDKConfiguration.Builder();
                builder.setEnableSound(true)
                        .setActionMode(FaceSDKConfiguration.TVActionMode.FULL)
                         .setCardType(TrustVisionSDK.getCardTypes().get(0));
                TrustVisionSDK.startTrustVisionSDK(MainActivity.this, builder.build());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            TVDetectionResult result = (TVDetectionResult) data.getSerializableExtra(TrustVisionSDK.TV_RESULT);
            Toast.makeText(this, "Face matching: " + result.getFaceCompareResult().getMatchResult().name(), Toast.LENGTH_LONG).show();

        }
    }

}
