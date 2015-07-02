package com.oliverbud.android.imagelist.UI;

import com.oliverbud.android.imagelist.Networking.ImageApi;
import com.oliverbud.android.imagelist.Networking.NetworkManager;
import com.oliverbud.android.imagelist.Networking.PingApi;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

import java.util.ArrayList;

import retrofit.Callback;

/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class MockNetworkManager extends NetworkManager{

    public MockNetworkManager(ImageApi imageService, PingApi pingService){

    }

    public void search(String searchString, int rSize, int startPageLocation, String userIp, String size, final Callback callback){
        callback.success((Object)new ArrayList<ImageDataItem>(), null);
    }
}
