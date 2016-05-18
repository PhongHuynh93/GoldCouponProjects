package com.degiorgi.valerio.goldcoupon.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.Settings;
import com.degiorgi.valerio.goldcoupon.Sync.SyncAdapter;

public class ScrollingActivity extends AppCompatActivity implements ScrollingActivityFragment.ScrollingFragmentListener,
        DetailActivityFragment.DetailFragmentListener {


    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);



        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
        }
        if (id == R.id.menu_refresh) {
            SyncAdapter.syncImmediately(getApplicationContext());
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_ARG, uri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(uri);
            Log.w("TEST1", String.valueOf(uri));
            startActivity(intent);
        }

    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

        if(uri!= null) {

            Intent intent = new Intent(this, DetailDialogActivity.class);
            String stringUri = uri.toString();
            intent.putExtra("Uri", stringUri);
            startActivity(intent);
        }
    }
}
