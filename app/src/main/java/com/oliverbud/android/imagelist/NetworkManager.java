package com.oliverbud.android.imagelist;


import android.util.Log;

import com.oliverbud.android.imagelist.Networking.ImageApi;
import com.oliverbud.android.imagelist.Networking.NetworkResponseData;

import retrofit.Callback;
import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        Log.d("itemListApp", "NetworkManager search");

        service.search(version, searchString, rSize, startPageLocation, userIp, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkResponseData -> {
                    if(networkResponseData.getResponseData() != null){
                        callback.success(networkResponseData.getResponseData().getResults(), null);
                    }
                });
    }

}
