package com.oliverbud.android.imagelist.Networking;

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