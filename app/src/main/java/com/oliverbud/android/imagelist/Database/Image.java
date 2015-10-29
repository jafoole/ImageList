package com.oliverbud.android.imagelist.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by oliverbudiardjo on 10/29/15.
 */
@Table(databaseName = ImageDatabase.NAME)
public class Image extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

    @Column
    boolean feet = true;

    public void setName(String name){
        this.name = name;
    }
}
