package com.oliverbud.android.imagelist;


import android.graphics.Color;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.oliverbud.android.imagelist.Networking.ImageApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by oliverbudiardjo on 6/4/15.
 */
public class NetworkManager {

    ImageApi service;

    final float version = 1.0f;

    public NetworkManager(){
        Log.d("itemListApp", "Create NetworkManager");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .build();
        service  = restAdapter.create(ImageApi.class);
    }

    public void search(String searchString, int rSize, int startPageLocation, String userIp, String size, final Callback callback){

        Callback networkCallback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                ArrayList<ImageDataItem> list = new ArrayList<ImageDataItem>();
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

                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                            newDataItem.setColor(color);

                            list.add(newDataItem);
                        }

                    }
                    catch (Exception e){
                        Log.d("itemListApp", "esception: " + e);
                        callback.failure(null);
                    }

                    callback.success(list, null);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };

        service.search(version, searchString, rSize, startPageLocation, userIp, size, networkCallback);
    }
}
