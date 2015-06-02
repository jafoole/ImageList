package com.oliverbud.android.imagelist;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliverbud on 5/26/15.
 */
public class ImageDataItem implements Parcelable{

    String url;
    Integer width;
    Integer height;
    String name;

    public void setUrl(String url){
        this.url = url;
    }

    public void setWidth(Integer width){
        this.width = width;
    }

    public void setHeight(Integer height){
        this.height = height;
    }

    public void setName(String name){
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("width", width);
        bundle.putInt("height", height);
        bundle.putString("name", name);

        out.writeBundle(bundle);

    }

    public static final Parcelable.Creator<ImageDataItem> CREATOR
            = new Parcelable.Creator<ImageDataItem>() {
        public ImageDataItem createFromParcel(Parcel in) {
            return new ImageDataItem(in);
        }

        public ImageDataItem[] newArray(int size) {
            return new ImageDataItem[size];
        }
    };

    public ImageDataItem(){

    }

    private ImageDataItem(Parcel in) {
        Bundle bundle = in.readBundle();
        url = bundle.getString("url");
        width = bundle.getInt("width");
        height = bundle.getInt("height");
    }
}
