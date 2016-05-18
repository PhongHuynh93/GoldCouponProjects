package com.degiorgi.valerio.goldcoupon;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Valerio on 01/05/2016.
 */
public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
