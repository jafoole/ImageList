package com.oliverbud.android.imagelist.Networking;

import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 8/3/15.
 */
public class NetworkResponseDataSub extends NetworkResponseData {

    @Override
    public ResponseData getResponseData() {
        return new ResponseData(new ArrayList<ImageDataItem>());
    }
}
