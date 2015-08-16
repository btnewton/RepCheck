package com.brandtnewtonsoftware.repcheck.util;

import android.app.Activity;
import android.provider.Settings;

import com.brandtnewtonsoftware.repcheck.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Brandt on 7/23/2015.
 */
public class AdMobHelper {

    private static String[] testDeviceIDs = new String[] {
            AdRequest.DEVICE_ID_EMULATOR
    };


    public static AdView CreateAdRequest(Activity activity) {
        String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = MD5_Hash(android_id).toUpperCase();

        AdView mAdView = (AdView) activity.findViewById(R.id.adView);

        AdRequest.Builder adRequest = new AdRequest.Builder();
        for (String testDeviceID : testDeviceIDs) {
            adRequest.addTestDevice(testDeviceID);
        }
        adRequest.addTestDevice(deviceId);
        mAdView.loadAd(adRequest.build());

        return mAdView;
    }

    public static String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

}
