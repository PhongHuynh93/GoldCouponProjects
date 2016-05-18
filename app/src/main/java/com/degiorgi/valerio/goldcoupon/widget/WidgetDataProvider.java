package com.degiorgi.valerio.goldcoupon.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.degiorgi.valerio.goldcoupon.Models.Coupons;
import com.degiorgi.valerio.goldcoupon.R;
import com.degiorgi.valerio.goldcoupon.data.DatabaseColumns;
import com.degiorgi.valerio.goldcoupon.data.DatabaseProvider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valerio on 02/05/2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext = null;
    Intent intent;
    Cursor mCursor;
    List<Coupons> couponList = new ArrayList<>();
    Coupons object;


    public WidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.intent = intent;
    }

    private void initData() {

        mCursor = mContext.getContentResolver().query(DatabaseProvider.Coupons.CONTENT_URI,
                new String[]{
                        DatabaseColumns.Title,
                        DatabaseColumns.ImageUrl,
                        DatabaseColumns.PostUrl,
                },
                null,
                null,
                null);

        couponList.clear();

        if (mCursor.moveToFirst()) {
            while (mCursor.moveToNext()) {
                object = new Coupons(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2));
                couponList.add(object);
            }
        }

        mCursor.close();
    }

    @Override
    public void onCreate() {
        initData();

    }



    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

        mCursor.close();
        couponList.clear();
    }

    @Override
    public int getCount() {
        return couponList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_collection_item);

        Coupons obj = couponList.get(position);

        view.setTextViewText(R.id.widget_post_title, obj.getmTitle());

        try {
            Bitmap b = Picasso.with(mContext).load(obj.getmImageUrl()).resize(300, 150).get();
            view.setImageViewBitmap(R.id.widget_post_image, b);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
