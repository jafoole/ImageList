package com.oliverbud.android.imagelist.EventBus;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */
public class UpdateListAtPosition {

    public int position;
    public boolean success;

    public UpdateListAtPosition(int position, boolean success){
        this.position = position;
        this.success = success;
    }
}
