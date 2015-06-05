package com.oliverbud.android.imagelist.Networking;

import com.oliverbud.android.imagelist.ImageDataItem;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 6/5/15.
 */
@Parcel
public class ResponseData {

    public ArrayList<ImageDataItem> results;

    public ResponseData(){

    }

    public ResponseData(ArrayList<ImageDataItem> results){
        this.results = results;
    }

    public ArrayList<ImageDataItem> getResults(){
        return this.results;
    }



}