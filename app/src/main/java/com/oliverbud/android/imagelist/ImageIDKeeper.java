package com.oliverbud.android.imagelist;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */
public class ImageIDKeeper {

    ArrayList<String> imageIdList;

    @Inject
    public ImageIDKeeper (){
        imageIdList = new ArrayList<>();
    }

    public boolean containedInList(String query){
        return imageIdList.contains(query);
    }

    public void addToList(String newItem){
        if (!containedInList(newItem)){
            imageIdList.add(newItem);
        }
    }
}
