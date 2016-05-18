package com.degiorgi.valerio.goldcoupon.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.Sync.SyncAdapter;

/**
 * Created by Valerio on 08/05/2016.
 */
public class SplashActivity extends Activity {
    private static boolean splashLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!splashLoaded) {
            setContentView(R.layout.activity_splash);
            SyncAdapter.initializeSyncAdapter(this);
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, ScrollingActivity.class));
                    finish();
                }
            }, secondsDelayed * 3000);

            splashLoaded = true;
        } else {
            Intent goToMainActivity = new Intent(SplashActivity.this, ScrollingActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }
}