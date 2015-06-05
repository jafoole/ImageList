package com.oliverbud.android.imagelist.Networking;

import android.os.Parcel;
import android.os.Parcelable;

import com.oliverbud.android.imagelist.NetworkManager;
import com.oliverbud.android.imagelist.Networking.ResponseData;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 6/5/15.
 */
@org.parceler.Parcel
public class NetworkResponseData {

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