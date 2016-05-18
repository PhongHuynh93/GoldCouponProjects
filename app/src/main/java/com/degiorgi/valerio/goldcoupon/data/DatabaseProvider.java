package com.degiorgi.valerio.goldcoupon.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Valerio on 09/04/2016.
 */
@ContentProvider(authority = DatabaseProvider.AUTHORITY, database = Database.class)
public class DatabaseProvider {
    public static final String AUTHORITY = "com.degiorgi.valerio.goldcoupon.data.DatabaseProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    interface Path {
        String COUPONS = "Coupons";
    }

    @TableEndpoint(table = Database.Coupons)
    public static class Coupons {
        @ContentUri(
                path = Path.COUPONS,
                type = "vnd.android.cursor.dir/Coupons"
        )
        public static final Uri CONTENT_URI = buildUri(Path.COUPONS);

        @InexactContentUri(
                name = "COUPON_ID",
                path = Path.COUPONS + "/#",
                type = "vnd.android.cursor.item/Coupon",
                whereColumn = DatabaseColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(int id) {

            return buildUri(Path.COUPONS, String.valueOf(id));
        }
    }

}