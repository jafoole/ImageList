package com.oliverbud.android.imagelist.EventBus;

import android.view.MenuItem;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavItemSelectedEvent {

    public MenuItem item;

    public NavItemSelectedEvent(MenuItem item) {
        this.item = item;
    }

}
