package com.oliverbud.android.imagelist.UI;


import android.util.Log;

import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.Networking.NetworkManager;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oliverbud on 5/26/15.
 */
public class ImageListPresenter {

    ImageListView imageListView;

    NetworkManager networkManager;

    public int page = 0;

    public ArrayList<ImageDataItem> list;

    ImageIDKeeper idKeeper;


    @javax.inject.Inject
    public ImageListPresenter(ImageListView imageListView, NetworkManager networkManager, ImageIDKeeper idKeeper) {
        Log.d("itemListApp", "instantiate Presenter");
        this.imageListView = imageListView;
        this.networkManager = networkManager;
        this.idKeeper = idKeeper;

    }

    public void loadMore(String moreString){

        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                ArrayList<ImageDataItem> moreList = (ArrayList<ImageDataItem>)o;
                for (int i = 0; i < moreList.size(); i ++){
                    if (idKeeper.containedInList(moreList.get(i).imageId)){
                        moreList.get(i).status = 3;
                    }
                }
                if (list != null && imageListView != null) {
                    for (int i = 0; i < moreList.size(); i ++) {
                        if (!list.contains(moreList.get(i))) {
                            list.add(moreList.get(i));
                        }
                    }
                    imageListView.addItems(moreList);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("itemListApp", "displayError 2");
            }
        };

        Log.d("itemListApp", "page: " + page);
        page += 1;
        networkManager.search(moreString, 8, page * 8, null, "medium", callback);
    }

    public void searchFor(String searchString) {
        Log.d("itemListApp", "searchFor: " + searchString);


        page = 0;
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                Log.d("itemListApp", "presenter callback, success");

                list = (ArrayList<ImageDataItem>) o;

                for (int i = 0; i < list.size(); i ++){
                    if (idKeeper.containedInList(list.get(i).imageId)){
                        list.get(i).status = 3;
                    }
                }

                imageListView.setItems(list);
                page += 1;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("itemListApp", "displayError 3");

                imageListView.displayError();

            }
        };
        imageListView.displayLoading();
        networkManager.search(searchString, 8, 0, null, "medium", callback);


    }
}
