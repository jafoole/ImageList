package com.oliverbud.android.imagelist;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class ImageIDKeeperTest extends TestCase {

    private static ImageIDKeeper keeper = new ImageIDKeeper();

    @Test
    public void testIDKeeperAddition(){
        keeper.addToList("Hello");
        assertEquals("add to list failed", "Hello", keeper.getImageIdList().get(0));
    }

    @Test
    public void testIDKeeperContains(){
        boolean contains = keeper.containedInList("beans");
        assertEquals("contains list failed", true, contains);
    }

    @Test
    public void testIDKeeperDeletion(){
        keeper.removeFromList("Hello");
        assertEquals("delete from list failed", 0, keeper.getImageIdList().size());
    }

}