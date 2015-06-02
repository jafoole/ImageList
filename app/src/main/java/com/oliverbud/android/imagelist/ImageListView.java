package com.oliverbud.android.imagelist;

import java.util.ArrayList;

/**
 * Created by oliverbud on 5/26/15.
 */
public interface ImageListView {

    public void displayLoading();

    public void setItems(ArrayList<ImageDataItem> listData);

    public void addItems(ArrayList<ImageDataItem> listData);


    public void displayError();

}
