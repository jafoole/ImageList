package com.oliverbud.android.imagelist;


import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import icepick.Icepick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oliverbud on 5/26/15.
 */
public class ImageListPresenter {

    ImageListView imageListView;
    private ImageApi service;

    public int page = 0;

    ArrayList<ImageDataItem> list;


    public ImageListPresenter(ImageListView imageListView) {
        Log.d("itemListApp", "instantiate Presenter");
        this.imageListView = imageListView;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .build();
        this.service = restAdapter.create(ImageApi.class);
    }

    public void decouple(){
        this.imageListView = null;
    }


    public void onResume(ImageListView ilv){
        Log.d("itemListApp", "onResume Presenter");
        if (this.imageListView == null) {
            this.imageListView = ilv;

            if (list != null && !list.isEmpty()) {
                Log.d("itemListApp", "list not empty");
                this.imageListView.setItems(list);
            }
        }
    }

    public void loadMore(String moreString){
        final ArrayList<ImageDataItem> moreList = new ArrayList<ImageDataItem>();

        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {

                //parse response
                String responseString = null;
                if (o.getClass() == LinkedTreeMap.class){
                    Gson gson = new Gson();

                    responseString = gson.toJson(o);
                    try {
                        final JSONObject jsonList = new JSONObject(responseString);
                        JSONObject dataArray = (JSONObject) jsonList.get("responseData");
                        JSONArray results = (JSONArray) dataArray.get("results");
                        Log.d("itemListApp", "resultsSize: " + results.length());

                        for (int i = 0; i < results.length(); i++) {
                            ImageDataItem newDataItem = new ImageDataItem();
                            JSONObject dataItem = results.getJSONObject(i);
                            if (dataItem.has("unescapedUrl")) {
                                String url = (String) dataItem.get("unescapedUrl");
                                newDataItem.setUrl(url);
                            }
                            if (dataItem.has("width")){
                                Integer width = Integer.parseInt((String)dataItem.get("width"));
                                newDataItem.setWidth(width);
                            }
                            if (dataItem.has("height")){
                                Integer height = Integer.parseInt((String)dataItem.get("height"));
                                newDataItem.setHeight(height);
                            }

                            if (dataItem.has("imageId")){
                                String name = (String)dataItem.get("imageId");
                                newDataItem.setName(name);
                            }

                            moreList.add(newDataItem);
                        }

                    }
                    catch (Exception e){
                        Log.d("itemListApp", "esception: " + e);
                    }
                }
                list.addAll(moreList);

                imageListView.addItems(moreList);
                page += 1;
            }

            @Override
            public void failure(RetrofitError error) {
                imageListView.displayError();
            }
        };
        service.search(1.0f, moreString, 8, page * 8, null, "small|medium", callback);
    }

    public void searchFor(String searchString) {

        page = 0;
        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                list = new ArrayList<ImageDataItem>();
                //parse response
                String responseString = null;
                if (o.getClass() == LinkedTreeMap.class){
                    Gson gson = new Gson();

                    responseString = gson.toJson(o);
                    try {
                        final JSONObject jsonList = new JSONObject(responseString);
                        JSONObject dataArray = (JSONObject) jsonList.get("responseData");
                        JSONArray results = (JSONArray) dataArray.get("results");
                        Log.d("itemListApp", "resultsSize: " + results.length());

                        for (int i = 0; i < results.length(); i++) {
                            ImageDataItem newDataItem = new ImageDataItem();
                            JSONObject dataItem = results.getJSONObject(i);
                            if (dataItem.has("unescapedUrl")) {
                                String url = (String) dataItem.get("unescapedUrl");
                                newDataItem.setUrl(url);
                            }
                            if (dataItem.has("width")){
                                Integer width = Integer.parseInt((String)dataItem.get("width"));
                                newDataItem.setWidth(width);
                            }
                            if (dataItem.has("height")){
                                Integer height = Integer.parseInt((String)dataItem.get("height"));
                                newDataItem.setHeight(height);
                            }
                            Log.d("itemListApp", "dataItem nad imageId:"  + dataItem.has("imageId"));

                            if (dataItem.has("imageId")){
                                String name = (String)dataItem.get("imageId");
                                newDataItem.setName(name);
                            }
                            list.add(newDataItem);
                        }

                    }
                    catch (Exception e){
                        Log.d("itemListApp", "esception: " + e);
                    }
                }

                imageListView.setItems(list);
                page += 1;
            }

            @Override
            public void failure(RetrofitError error) {
                imageListView.displayError();
            }
        };
        imageListView.displayLoading();
        service.search(1.0f, searchString, 8, 0, null, "medium", callback);


    }
}
