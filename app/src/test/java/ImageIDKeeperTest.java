package com.oliverbud.android.imagelist;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class ImageIDKeeperTest extends TestCase {

    @Test
    public void testIDKeeper(){
        ImageIDKeeper keeper = new ImageIDKeeper();

        keeper.addToList("Hello");
        ArrayList<String> list = keeper.getImageIdList();

        assertEquals("add to list failed", "beans", list.get(0));
    }

}