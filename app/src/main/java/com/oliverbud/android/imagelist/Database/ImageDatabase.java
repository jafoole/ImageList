package com.oliverbud.android.imagelist.Database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by oliverbudiardjo on 10/29/15.
 */
@Database(name = ImageDatabase.NAME, version = ImageDatabase.VERSION)
public class ImageDatabase {

    public static final String NAME = "Images";

    public static final int VERSION = 1;

}
