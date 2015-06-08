package com.oliverbud.android.imagelist;


import android.util.Log;

import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

import java.util.ArrayList;

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


    @javax.inject.Inject
    public ImageListPresenter(ImageListView imageListView, NetworkManager networkManager) {
        Log.d("itemListApp", "instantiate Presenter");
        this.imageListView = imageListView;
        this.networkManager = networkManager;

    }

    public void loadMore(String moreString){

        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                ArrayList<ImageDataItem> moreList = (ArrayList<ImageDataItem>)o;
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

                imageListView.displayError();
            }
        };

        Log.d("itemListApp", "page: " + page);
        page += 1;
        networkManager.search(moreString, 8, page * 8, null, "medium", callback);
    }

    public void searchFor(String searchString) {


        page = 0;
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                list = (ArrayList<ImageDataItem>) o;

                imageListView.setItems(list);
                page += 1;
            }

            @Override
            public void failure(RetrofitError error) {
                imageListView.displayError();
                Log.d("itemListApp", "displayError 3");

            }
        };
        imageListView.displayLoading();
        networkManager.search(searchString, 8, 0, null, "medium", callback);


    }
}
