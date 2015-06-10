package com.oliverbud.android.imagelist.Networking;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by oliverbud on 5/26/15.
 */
public interface ImageApi {

    @GET ("/ajax/services/search/images")
    Observable<NetworkResponseData>  search(
            @Query("v") float version,
            @Query("q") String searchString,
            @Query("rsz") int rSize,
            @Query("start") int start,
            @Query("userip") String ip,
            @Query("imgsz") String size

    );
}
