package com.oliverbud.android.imagelist.EventBus;

import android.view.MenuItem;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavItemSelectedEvent {

    public String item;

    public NavItemSelectedEvent(String item) {
        this.item = item;
    }

}
