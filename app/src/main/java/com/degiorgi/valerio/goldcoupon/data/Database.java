package com.degiorgi.valerio.goldcoupon.data;

import net.simonvt.schematic.annotation.Table;

/**
 * Created by Valerio on 09/04/2016.
 */

@net.simonvt.schematic.annotation.Database(version = Database.VERSION )
public class Database {

    public static final int VERSION = 4;
    @Table(DatabaseColumns.class)
    public static final String Coupons = "Coupons";

    private Database(){}
}
