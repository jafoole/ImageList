package com.oliverbud.android.imagelist.Networking;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */
public interface PingApi {

    @GET("/posts/1")
    Observable<Object> ping();

}
