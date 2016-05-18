package com.degiorgi.valerio.goldcoupon.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Valerio on 02/05/2016.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {


    @Override
    public android.widget.RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new WidgetDataProvider(this, intent);
    }
}