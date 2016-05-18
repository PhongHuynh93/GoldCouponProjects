package com.degiorgi.valerio.goldcoupon.UI;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.degiorgi.valerio.goldcoupon.Global;
import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.data.DatabaseColumns;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Valerio on 03/05/2016.
 */
public class DetailDialogActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    Uri mUri;
    TextView telephone;
    TextView cell;
    Button callTell;
    Button callCell;
    Button goToWebsite;
    Button shareButton;
    Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

// Obtain the shared Tracker instance.
        Global application = (Global) getApplication();
        mTracker = application.getDefaultTracker();

        Intent intent = getIntent();
        String uri = intent.getStringExtra("Uri");

        if (uri != null) {
            mUri = Uri.parse(uri);
        }
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        telephone = (TextView) findViewById(R.id.textview_telephone);
        cell = (TextView) findViewById(R.id.textview_cell);
        callTell = (Button) findViewById(R.id.call_telephone);
        callCell = (Button) findViewById(R.id.call_cell);
        goToWebsite = (Button) findViewById(R.id.goToWebsite);
        shareButton = (Button) findViewById(R.id.share_button);

    }


    private boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.w("TEST1", "detailoader");

        if(mUri!=null) {

            return new CursorLoader(getApplicationContext(), mUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        final String telephonePartial;
        final String cellPartial;
        final String postUrl;

        if (data != null && data.moveToFirst()) {

            String content = data.getString(data.getColumnIndex(DatabaseColumns.Excerpt));

            if (isTelephonyEnabled()) {

                if (content.contains("Tel.") || content.contains("Cel.")) {
                    int index = content.indexOf("Tel.");
                    if (index == -1) {
                        index = content.indexOf("Cel.");
                    }

                    telephonePartial = " " + content.substring(index + 4, index + 15);
                    callTell.setClickable(true);


                    telephone.setText(telephonePartial);
                    callTell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + telephonePartial));
                            startActivity(intent);

                        }
                    });

                } else {
                    telephone.setText(R.string.telephone_not_available);
                    callTell.setClickable(false);
                }

                if (content.contains("Cell.") || content.contains("Tele.")) {
                    int index = content.indexOf("Cell.");
                    if (index == -1) {
                        index = content.indexOf("Tele.");
                    }
                    cellPartial = " " + content.substring(index + 5, index + 16);
                    callCell.setClickable(true);


                    cell.setText(cellPartial);
                    callCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + cellPartial));
                            startActivity(intent);

                        }
                    });
                } else {
                    callCell.setClickable(false);
                    cell.setText(R.string.telephone_not_available);
                }

            }

            postUrl = data.getString(data.getColumnIndex(DatabaseColumns.PostUrl));

            goToWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(postUrl));
                    startActivity(browserIntent);

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Website")
                            .build());

                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, "GoldCoupon");
                    share.putExtra(Intent.EXTRA_TEXT, postUrl);
                    startActivity(Intent.createChooser(share, "Condividi!"));

                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("Share")
                            .build());

                }
            });


        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.i("TEST1", "Setting screen name: " + "DialogActivity");
        mTracker.setScreenName("Image~" + "DialogActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
