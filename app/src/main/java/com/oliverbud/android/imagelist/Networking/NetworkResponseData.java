package com.oliverbud.android.imagelist.Networking;

import com.oliverbud.android.imagelist.Database.ImageDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by oliverbudiardjo on 6/5/15.
 */

@Table(databaseName = ImageDatabase.NAME)


@org.parceler.Parcel
public class NetworkResponseData extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    boolean feet = true;

    public ResponseData responseData;


    public NetworkResponseData(){

    }

    public NetworkResponseData(ResponseData responseData){
        this.responseData = responseData;
    }


    public ResponseData getResponseData(){
        return this.responseData;
    }



}