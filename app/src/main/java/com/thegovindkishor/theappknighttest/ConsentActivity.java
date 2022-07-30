package com.thegovindkishor.theappknighttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;

import java.net.MalformedURLException;
import java.net.URL;

public class ConsentActivity extends AppCompatActivity {
    ConsentForm form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        getconcentStatus();
    }

    private void getconcentStatus(){
      // ConsentInformation.getInstance(ConsentActivity.this).addTestDevice("6059850F3D104DA4B741B1035780DD9E");
        ConsentInformation.getInstance(ConsentActivity.this).addTestDevice("923EC97110C13B4773E626AFB30A168C");

        ConsentInformation.getInstance(ConsentActivity.this).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(ConsentActivity.this);
        String[] publisherIds = {"pub-2196304721699276"};
       // String[] publisherIds = {"pub-0123456789012345"};

        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.e("abcd", String.valueOf(consentStatus));
                // User's consent status successfully updated.
                boolean isEueropianuser=ConsentInformation.getInstance(ConsentActivity.this).isRequestLocationInEeaOrUnknown();
                if(isEueropianuser){
                    switch (consentStatus){
                        case UNKNOWN:
                            Toast.makeText(getApplicationContext(), "hii", Toast.LENGTH_SHORT).show();
                            displayconsentform();
                            break;
                        case PERSONALIZED:
                            loadbannerads(true);
                            break;
                        case NON_PERSONALIZED:
                            loadbannerads(false);
                            break;
                    }
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                startActivity(new Intent(ConsentActivity.this,SplashActivity.class));

                // User's consent status failed to update.
                Toast.makeText(getApplicationContext(),errorDescription,Toast.LENGTH_LONG).show();
                Log.e("erroe",errorDescription);
            }
        });
    }

    private void displayconsentform(){

        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL("https://www.your.com/privacyurl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(ConsentActivity.this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        form.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        switch (consentStatus){
                            case PERSONALIZED:
                               // loadbannerads(true);
                                break;

                            case NON_PERSONALIZED:
                               // loadbannerads(false);
                                break;
                        }
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();
//form.show();
        form.load();
    }
    private void loadbannerads(boolean isPersonlized){

        if(isPersonlized){
        startActivity(new Intent(ConsentActivity.this,SplashActivity.class));
        }
        else {

        }

//        AdView adView= findViewById(R.id.adView);
//        AdRequest adRequest;
//        if(isPersonlized){
//            adRequest= new AdRequest.Builder().build();
//        }
//        else {
//            Bundle bundle= new Bundle();
//            bundle.putString("npa","1");
//            adRequest= new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class,bundle).build();
//        }
//
//        adView.loadAd(adRequest);

    }
}