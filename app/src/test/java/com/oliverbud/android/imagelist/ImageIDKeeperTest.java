package com.oliverbud.android.imagelist;

import android.util.Log;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class ImageIDKeeperTest{

    private ImageIDKeeper keeper;

    @Before
    public void setup(){
        this.keeper = new ImageIDKeeper();
        this.keeper.addToList("Hello");
    }

    @Test
    public void testIDKeeperAddition(){
        assertEquals("add to list failed", "Hello", this.keeper.getImageIdList().get(0));
    }

    @Test
    public void testIDKeeperContains(){
        boolean contains = this.keeper.containedInList("Hello");
        assertEquals("contains list failed", true, contains);
    }

    @Test
    public void testIDKeeperDeletion(){

        this.keeper.removeFromList("Hello");
        assertEquals("delete from list failed", 0, this.keeper.getImageIdList().size());
    }

}