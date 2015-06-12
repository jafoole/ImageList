package com.oliverbud.android.imagelist.UI;

import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavigationPresenter {

    NavView navigationView;
    public ArrayList<String> navList = new ArrayList<String>();

    @Inject
    public NavigationPresenter( NavView navigationView){
        Log.d("itemListApp", "NavigationPresenterInstantiate");
        this.navigationView = navigationView;
    }

    public void updateNavItems(ArrayList<String> navList){
        for (int i = 0; i < navList.size(); i ++) {
            if (!containsItem(navList.get(i))) {
                this.navList.add(navList.get(i));
            }
        }
        navigationView.updateNavigationWithItems(this.navList);

    }

    public boolean containsItem(String query) {

        for (int i = 0; i < this.navList.size(); i++) {
            if (this.navList.get(i).toString().toLowerCase().equals(query.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}
