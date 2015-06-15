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

    public boolean getStatus(){
        return this.item.status;
    }

    public void setStatus(boolean status){
        item.status = status;
    }
}
