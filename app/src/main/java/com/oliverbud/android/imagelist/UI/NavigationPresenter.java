package com.oliverbud.android.imagelist.UI;

import android.util.Log;

import com.oliverbud.android.imagelist.EventBus.removeSavedItem;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavigationPresenter {

    NavView navigationView;
    public ArrayList<String> navList = new ArrayList<String>();

    public ArrayList<String> savedList = new ArrayList<String>();


    @Inject
    public NavigationPresenter( NavView navigationView){
        Log.d("itemListApp", "NavigationPresenterInstantiate");
        this.navigationView = navigationView;

    }

    public void registerBus(){
        EventBus.getDefault().register(this);
    }

    public void unregisterBus(){
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(removeSavedItem event){
        Log.d("itemListApp", "removeSavedItem");
        removeSavedItem(event.item);
    }

    public void updateNavItems(ArrayList<String> navList){
        for (int i = 0; i < navList.size(); i ++) {
            if (!containsNavItem(navList.get(i))) {
                this.navList.add(navList.get(i));
            }
        }
        navigationView.updateNavigationWithItems(this.navList);

    }

    public void updateSavedItems(ArrayList<String> savedList){
        for (int i = 0; i < savedList.size(); i ++) {
            if (!containsSavedItem(savedList.get(i))) {
                this.savedList.add(savedList.get(i));
            }
        }
        navigationView.updateSavedWithItems(this.savedList);

    }

    public void removeSavedItem(String item){
        if (containsSavedItem(item)){
            this.savedList.remove(item);
        }
        navigationView.updateSavedWithItems(this.savedList);

    }

    public boolean containsNavItem(String query) {

        if (this.navList.size() == 0){
            return false;
        }

        for (int i = 0; i < this.navList.size(); i++) {
            if (this.navList.get(i).toString().toLowerCase().equals(query.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsSavedItem(String query) {
        if (this.savedList.size() == 0){
            return false;
        }
        for (int i = 0; i < this.savedList.size(); i++) {
            if (this.savedList.get(i).toString().toLowerCase().equals(query.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}
