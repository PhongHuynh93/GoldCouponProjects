package com.degiorgi.valerio.goldcoupon.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Valerio on 09/04/2016.
 */
public class DatabaseColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String PostId = "postId";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String Title = "title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String ImageUrl = "imageUrl";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String PostUrl = "postUrl";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String Excerpt = "excerpt";

}
