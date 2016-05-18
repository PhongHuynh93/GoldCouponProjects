package com.degiorgi.valerio.goldcoupon.UI;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.adapter.ViewPagerAdapter;
import com.degiorgi.valerio.goldcoupon.data.DatabaseColumns;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String DETAIL_ARG = "detail_id";
    private static final int CURSOR_LOADER_ID = 1;
    Uri mUri;
    TextView mTitle;
    TextView mContent;
    ImageView mPostImage;
    ViewPager viewPager;
    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
    ViewPagerAdapter viewPagerAdapter;
    private DetailFragmentListener mListener;
    private ArrayList<String> IMAGES = new ArrayList<>();


    public DetailActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_detail_activity, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_ARG);
        }
        mTitle = (TextView) rootview.findViewById(R.id.post_title);
        mContent = (TextView) rootview.findViewById(R.id.detail_content);
        mPostImage = (ImageView) rootview.findViewById(R.id.detail_image);

        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(mUri);
            }
        });

        viewPager = (ViewPager) rootview.findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getContext(), IMAGES);
        viewPager.setAdapter(viewPagerAdapter);

        registerForContextMenu(mContent);
        registerForContextMenu(mTitle);


        return rootview;

    }


    private void Run(String url) throws IOException {

        Log.w("TEST1", "onRUn");

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("TEST1", "onRUnFailed");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.w("TEST1", "onRUnSuccess");


                String json = response.body().string();

                IMAGES.clear();


                try {

                    Log.w("TEST1", "onRUnTry");

                    JSONObject object = new JSONObject(json);
                    JSONObject jObject = object.getJSONObject("post");
                    JSONArray galleryArray = jObject.getJSONArray("attachments");

                    Log.w("TEST1", galleryArray.toString());

                    if (galleryArray.length() > 0) {
                        for (int i = 0; i < galleryArray.length(); i++) {

                            String url = galleryArray.getJSONObject(i).getJSONObject("images").getJSONObject("full").getString("url");
                            IMAGES.add(url);
                        }

                        if (viewPagerAdapter != null && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewPagerAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        Log.w("TEST1", IMAGES.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDetailFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailFragmentListener) {
            mListener = (DetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DetailFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(getContext(), mUri, null, null, null, null);
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        final int width = getContext().getResources().getDisplayMetrics().widthPixels;
        final int height = (int) (width / 1.5);
        final Context mContext = getContext();
        final String check = "";


        if (data.moveToFirst()) {
            final String url = data.getString(data.getColumnIndex(DatabaseColumns.ImageUrl));
            Log.w("TEST2", String.valueOf(data.getString(1)));

            String title = data.getString(data.getColumnIndex(DatabaseColumns.Title));
            mTitle.setText(title);

            String contentText = data.getString(data.getColumnIndex(DatabaseColumns.Excerpt));

            if (!contentText.equals(check)) {
                mContent.setText(contentText);
            } else {
                mContent.setText(R.string.noContentDescription);
            }

            String id = String.valueOf(data.getInt(data.getColumnIndex(DatabaseColumns.PostId)));

            try {
                Run("http://goldcoupon.net/?json=get_post&post_id=" + id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.with(mContext)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(width, height)
                    .into(mPostImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                            Picasso.with(mContext)
                                    .load(url)
                                    .resize(width, height)
                                    .into(mPostImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public interface DetailFragmentListener {
        void onDetailFragmentInteraction(Uri uri);
    }
}
