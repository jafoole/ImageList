package com.oliverbud.android.imagelist.UI;

import com.oliverbud.android.imagelist.EventBus.SearchEvent;
import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.Networking.NetworkManager;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by oliverbudiardjo on 7/2/15.
 */
public class ImageListPresenterTest{

    ImageListView view = new ImageListView() {
        @Override
        public void displayLoading() {

        }

        @Override
        public void setItems(ArrayList<ImageDataItem> listData) {

        }

        @Override
        public void updateItems(int position) {

        }

        @Override
        public void displayError() {

        }
    };

    ImageIDKeeper keeper = new ImageIDKeeper();

    NetworkManager manager = new MockNetworkManager(null, null);

    ImageListPresenter presenter;

    @Before
    public void setup(){
        presenter = new ImageListPresenter(view, manager, keeper);
    }

    @Test
    public void testPresenter(){
        presenter.searchFor("feet");
        presenter.loadMore("feet");
        presenter.loadMore("feet");

        assertEquals(3, presenter.page);

    }

    @Test
    public void testBusSearch(){

        SearchEvent event = new SearchEvent("tacos");
        presenter.onEvent(event);

        assertEquals("tacos", presenter.currentSearchDisplay);
    }

}