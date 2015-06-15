package com.oliverbud.android.imagelist.EventBus;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;
import com.oliverbud.android.imagelist.UI.Util.ImageListAdapter;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */
public class ItemClickedEvent {

    ImageDataItem item;
    public int position;

    public ItemClickedEvent( ImageDataItem item, int position){
        this.item = item;
        this.position = position;
    }

    public String getTitle(){
        return item.imageId;
    }


    public int getStatus(){
        return this.item.status;
    }

    public void setStatus(int status){
        item.status = status;
    }
}
