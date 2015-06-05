package com.oliverbud.android.imagelist;


import android.util.Log;

import com.oliverbud.android.imagelist.Networking.ImageApi;
import com.oliverbud.android.imagelist.Networking.NetworkResponseData;

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

        Callback<NetworkResponseData> networkCallback = new Callback<NetworkResponseData>() {
            @Override
            public void success(NetworkResponseData o, Response response) {
                Log.d("itemListApp", "search success");
                if (o.getResponseData() != null) {
                    callback.success(o.getResponseData().getResults(), null);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("itemListApp", "search error: " + error);

                callback.failure(error);
            }
        };

        service.search(version, searchString, rSize, startPageLocation, userIp, size, networkCallback);
    }
}
