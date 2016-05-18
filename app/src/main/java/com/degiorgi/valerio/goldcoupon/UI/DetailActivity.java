package com.degiorgi.valerio.goldcoupon.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.degiorgi.valerio.goldcoupon.R;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.DetailFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            Uri uri = getIntent().getData();
            if (uri != null) {
                arguments.putParcelable(DetailActivityFragment.DETAIL_ARG, getIntent().getData());

                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }


        }
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {


        if(uri!=null) {

            Intent intent = new Intent(this, DetailDialogActivity.class);
            String stringUri = uri.toString();
            intent.putExtra("Uri", stringUri);
            startActivity(intent);
        }

    }


}
