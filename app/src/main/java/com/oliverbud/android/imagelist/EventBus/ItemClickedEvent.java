package com.oliverbud.android.imagelist.EventBus;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */
public class ItemClickedEvent {

    ImageDataItem item;
    ImageView statusView;

    public ItemClickedEvent(ImageView viewHolder, ImageDataItem item){
        this.statusView = viewHolder;
        this.item = item;
    }

    public String getTitle(){
        return item.imageId;
    }

    public ImageView getStatusView(){
        return statusView;
    }

    public int getStatus(){
        return this.item.status;
    }

    public void setStatus(int status){
        item.status = status;
    }
}
