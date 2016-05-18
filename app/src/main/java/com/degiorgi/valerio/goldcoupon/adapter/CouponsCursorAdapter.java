package com.degiorgi.valerio.goldcoupon.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.degiorgi.valerio.goldcoupon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * Created by Valerio on 10/04/2016.
 */
public class CouponsCursorAdapter extends CursorRecyclerViewAdapter<CouponsCursorAdapter.ViewHolder>
         {


    private static Context mContext;
             public CouponsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {

        if(cursor!=null) {

            String titleText = cursor.getString(cursor.getColumnIndex("title"));

            final int width = mContext.getResources().getDisplayMetrics().widthPixels / 2;
            final int height = (int) (width * 0.7);

            viewHolder.title.setText(titleText);
            final String imageUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));

            Picasso.with(mContext)
                    .load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(width, height)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(viewHolder.image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(mContext)
                                    .load(imageUrl)
                                    .resize(width, height)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(viewHolder.image, new Callback() {
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
    public int getItemCount() {
        return super.getItemCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView title;
        public final ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.article_title);
            image = (ImageView) itemView.findViewById(R.id.thumbnail);

        }


    }
}