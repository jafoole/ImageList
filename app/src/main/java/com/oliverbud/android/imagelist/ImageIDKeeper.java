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

    public void removeFromList(String newItem){
        if (containedInList(newItem)){
            imageIdList.remove(newItem);
        }
    }

    public ArrayList<String> getImageIdList(){
        return imageIdList;
    }
}

/* This is my change */

<!DOCTYPE html>
<html>
<head>
<title>Hello World HTML</title>
</head>
<body>
<h1>Hello World</h1>
</body>
</html>

