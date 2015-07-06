package com.oliverbud.android.imagelist.UI;

import com.oliverbud.android.imagelist.EventBus.AddItemsEvent;
import com.oliverbud.android.imagelist.EventBus.removeSavedItem;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class NavigationPresenterTest{

    NavView view = new NavView() {
        @Override
        public void updateNavigationWithItems(ArrayList<String> items) {

        }

        @Override
        public void updateSavedWithItems(ArrayList<String> items) {

        }
    };

    NavigationPresenter presenter;

    @Before
    public void setup(){
        presenter = new NavigationPresenter(view);
        ArrayList<String> savedList = new ArrayList<>();
        savedList.add("beetroot");
        savedList.add("salmanilla");
        savedList.add("rubarb");

        presenter.updateSavedItems(savedList);

        ArrayList<String> navList = new ArrayList<>();

        navList.add("snails");
        navList.add("lizards");
        navList.add("buffalo");

        AddItemsEvent event = new AddItemsEvent(navList);

        presenter.onEvent(event);
    }

    @Test
    public void testUpdateSaved(){
        assertEquals(3, presenter.savedList.size());

    }

    @Test
    public void testContainsSaved(){
        assertFalse(presenter.savedList.size() == 0);
        boolean contains = presenter.containsSavedItem("beetroot");
        assertTrue(contains);
    }


    @Test
    public void testRemoveSaved(){
        presenter.onEvent(new removeSavedItem("beetroot"));
        boolean removed = presenter.containsSavedItem("beetroot");
        assertFalse(removed);

    }

    @Test
    public void testUpdateNav(){

        assertEquals(3, presenter.navList.size());
    }

    @Test
    public void tesContainsNav(){
        boolean contains = presenter.containsNavItem("snails");
        assertTrue(contains);

    }


}