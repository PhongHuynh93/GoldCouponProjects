package com.degiorgi.valerio.goldcoupon.Sync;

/**
 * Created by Valerio on 29/04/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */
public class SyncAuthService extends Service {

    // Instance field that stores the authenticator object
    private SyncAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new SyncAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}