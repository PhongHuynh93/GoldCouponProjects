package com.degiorgi.valerio.goldcoupon.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.degiorgi.valerio.goldcoupon.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Valerio on 06/05/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<String> IMAGES = new ArrayList<>();

    public ViewPagerAdapter(Context context, ArrayList<String> IMAGES) {
        this.IMAGES = IMAGES;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return IMAGES.size();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.imagepager_layout, collection, false);
        final ImageView img = (ImageView) itemView.findViewById(R.id.img);

        (collection).addView(itemView);

        Log.w("TEST1", "PICASSO" + IMAGES.get(position));

        final int width = context.getResources().getDisplayMetrics().widthPixels;
        final int height = (int) (width / 1.5);

        if (IMAGES.size() > 1) {
            Picasso.with(context)
                    .load(IMAGES.get(position))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(width, height)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                            Picasso.with(context)
                                    .load(IMAGES.get(position))
                                    .resize(width, height)
                                    .into(img, new Callback() {
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


        } else {
            Picasso.with(context)
                    .load(R.drawable.coupon)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(width, (int) (width / 1.5))
                    .into(img);
        }


        return itemView;
    }
}