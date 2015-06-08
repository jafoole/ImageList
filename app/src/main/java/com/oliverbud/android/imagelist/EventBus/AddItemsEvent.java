package com.oliverbud.android.imagelist.EventBus;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class AddItemsEvent {

    public ArrayList<String> addItems;

    public AddItemsEvent(ArrayList<String> addItems){
        this.addItems = addItems;
    }
}
