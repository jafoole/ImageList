package com.oliverbud.android.imagelist.Networking;


import android.util.Log;

import com.oliverbud.android.imagelist.Networking.ImageApi;
import com.oliverbud.android.imagelist.Networking.NetworkResponseData;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by oliverbudiardjo on 6/4/15.
 */
public class NetworkManager {

    ImageApi imageService;
    PingApi pingService;

    final float version = 1.0f;

    @Inject
    public NetworkManager(ImageApi imageService, PingApi pingService){
        Log.d("itemListApp", "Create NetworkManager");

       this.imageService = imageService;
        this.pingService = pingService;
    }

    public void search(String searchString, int rSize, int startPageLocation, String userIp, String size, final Callback callback){
        Log.d("itemListApp", "NetworkManager search");

        imageService.search(version, searchString, rSize, startPageLocation, userIp, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetworkResponseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.failure(null);
                    }

                    @Override
                    public void onNext(NetworkResponseData networkResponseData) {
                        if(networkResponseData.getResponseData() != null){
                            callback.success(networkResponseData.getResponseData().getResults(), null);
                        }
                    }
                });
    }

    public Observable<Object> ping(){

        return  pingService.ping()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
