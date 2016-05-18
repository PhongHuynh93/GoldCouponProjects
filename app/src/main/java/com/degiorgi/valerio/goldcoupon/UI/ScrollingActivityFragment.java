package com.degiorgi.valerio.goldcoupon.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.adapter.CouponsCursorAdapter;
import com.degiorgi.valerio.goldcoupon.adapter.RecyclerViewItemClickListener;
import com.degiorgi.valerio.goldcoupon.data.DatabaseColumns;
import com.degiorgi.valerio.goldcoupon.data.DatabaseProvider;


public class ScrollingActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    private ScrollingFragmentListener mListener;
    private CouponsCursorAdapter mCursorAdapter;


    public ScrollingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_blank, container, false);

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mCursorAdapter = new CouponsCursorAdapter(getContext(), null);
        recyclerView.setAdapter(mCursorAdapter);


        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(),
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        Cursor cur = mCursorAdapter.getCursor();
                        if (cur != null) {
                            cur.moveToPosition(position);

                            onButtonPressed(DatabaseProvider.Coupons.withId(cur.getInt(0)));

                            Log.w("TEST1", String.valueOf(cur.getInt(0)));

                        }
                    }
                }));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@goldcoupon.net"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Richiesta Informazioni");
                startActivity(intent);

            }
        });


        return rootview;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScrollingFragmentListener) {
            mListener = (ScrollingFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScrollingFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.w("TEST1", "onCreateLoader");

        return new android.support.v4.content.CursorLoader(getContext(), DatabaseProvider.Coupons.CONTENT_URI,
                null,
                null,
                null,
                DatabaseColumns._ID + " ASC");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(CURSOR_LOADER_ID);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (getLoaderManager().getLoader(CURSOR_LOADER_ID) == null) {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ScrollingFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
