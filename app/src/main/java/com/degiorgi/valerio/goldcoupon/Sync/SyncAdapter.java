package com.degiorgi.valerio.goldcoupon.Sync;

/**
 * Created by Valerio on 29/04/2016.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.UI.ScrollingActivity;
import com.degiorgi.valerio.goldcoupon.data.DatabaseColumns;
import com.degiorgi.valerio.goldcoupon.data.DatabaseProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final int SYNC_INTERVAL = 60 * 360;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    OkHttpClient client = new OkHttpClient();

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    // This helper method has been taken by the udacity project Sunshine-Version 2
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    // This helper method has been taken by the udacity project Sunshine-Version 2
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    // This helper method has been taken by the udacity project Sunshine-Version 2
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }


            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    // This helper method has been taken by the udacity project Sunshine-Version 2

    private static void onAccountCreated(Account newAccount, Context context) {

        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();


        return response.body().string();
    }


    private void updateNotifications(int postCount) {

        SharedPreferences settings = getContext().getSharedPreferences("myPrefsFile", 0);
        String notificationOption = settings.getString(getContext().getString(R.string.notification_key), getContext().getString(R.string.notification_default));

        if (notificationOption.equals("true")) {

            if (postCount > settings.getInt("postCount", 0)) {

                settings.edit().putInt("postCount", postCount).apply();

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setSmallIcon(R.drawable.copertinaapp)
                                .setContentTitle("GoldCoupon")
                                .setContentText("Ci sono nuove offerte da visualizzare!");

                Intent resultIntent = new Intent(getContext(), ScrollingActivity.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                int mNotificationId = 1;
                NotificationManager mNotifyMgr =
                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }
        }
    }

    private ContentValues[] JsonToObject(String json) throws JSONException {

        JSONObject object = new JSONObject(json);

        int postCount = object.getInt("count");

        updateNotifications(postCount);

        JSONArray jArray = object.getJSONArray("posts");

        ArrayList<ContentValues> cAArray = new ArrayList<>(jArray.length());

        for (int i = 0; i < jArray.length(); i++) {

            ContentValues values = new ContentValues();

            String title;
            String imageUrl;
            String postUrl;
            String excerpt;
            int postId;

            JSONObject intermediateObj =  jArray.getJSONObject(i);

            postId = intermediateObj.getInt("id");
            title = intermediateObj.getString("title");
            imageUrl = intermediateObj.getJSONObject("thumbnail_images").getJSONObject("full").getString("url");
            postUrl = intermediateObj.getString("url");
            excerpt = intermediateObj.getString("excerpt");

            title = specialCharactersFilter(title);
            excerpt = specialCharactersFilter(excerpt);

            values.put(DatabaseColumns.PostId, postId);
            values.put(DatabaseColumns.Title, title);
            values.put(DatabaseColumns.ImageUrl, imageUrl);
            values.put(DatabaseColumns.PostUrl, postUrl);
            values.put(DatabaseColumns.Excerpt, excerpt);

            cAArray.add(values);
        }

        ContentValues[] cArray = new ContentValues[cAArray.size()];

        cAArray.toArray(cArray);

        return cArray;

    }

    private String specialCharactersFilter(String text) {

        if (text.contains("&#8220")) {
            text = text.replace("&#8220;", "'");
        }
        if (text.contains("&#8221")) {
            text = text.replace("&#8221;", "'");
        }
        if (text.contains("&#8217")) {
            text = text.replace("&#8217;", "");
        }
        if (text.contains("&#038")) {
            text = text.replace("&#038;", "E");
        }
        if (text.contains("&#8230")) {
            text = text.replace("&#8230;", "...");
        }
        if (text.contains("<p>")) {
            text = text.replace("<p>", "");
        }
        if (text.contains("</p>")) {
            text = text.replace("</p>", "");
        }
        if (text.contains("&#8211")) {
            text = text.replace("&#8211;", "-");
        }
        if (text.contains("<br>")) {
            text = text.replace("<br>", "");
        }
        if (text.contains("<br />")) {
            text = text.replace("<br />", "");
        }
        return text;
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.w("TEST1", "Starting Sync");
        String url = "http://goldcoupon.net/?json=get_recent_posts";

        try {
            String json = run(url);
            ContentValues[] cArray = JsonToObject(json);

            getContext().getContentResolver().delete(DatabaseProvider.Coupons.CONTENT_URI, null, null);
            getContext().getContentResolver().bulkInsert(DatabaseProvider.Coupons.CONTENT_URI, cArray);


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}