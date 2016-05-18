package com.degiorgi.valerio.goldcoupon.Models;

/**
 * Created by Valerio on 09/04/2016.
 */
public class Coupons {

    private String mTitle;
    private String mImageUrl;
    private String mPostUrl;



    public Coupons (String title, String imageUrl, String postUrl){

        mTitle = title;
        mImageUrl = imageUrl;
        mPostUrl = postUrl;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmPostUrl() {
        return mPostUrl;
    }
}
