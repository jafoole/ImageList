package com.oliverbud.android.imagelist;


import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by oliverbudiardjo on 6/4/15.
 */
public class NetworkManager {

    ImageApi service;

    final float version = 1.0f;

    public NetworkManager(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .build();
        service  = restAdapter.create(ImageApi.class);
    }

    public void search(String searchString, int rSize, int startPageLocation, String userIp, String size, Callback callback){
        service.search(version, searchString, rSize, startPageLocation, userIp, size, callback);
    }
}
