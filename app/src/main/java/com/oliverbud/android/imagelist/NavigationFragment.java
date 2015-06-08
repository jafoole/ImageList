package com.oliverbud.android.imagelist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.AddItemsEvent;
import com.oliverbud.android.imagelist.EventBus.GenericEvent;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavigationFragment extends Fragment implements NavView{

    @InjectView(R.id.navigation) NavigationView navigation;

    @Inject NavigationPresenter navPresenter;

    ObjectGraph activityGraph;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_layout, container);
        Log.d("itemListApp", "onCreateView Fragment");

        ButterKnife.inject(this, view);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                EventBus.getDefault().post(new NavItemSelectedEvent(menuItem));


                return false;
            }
        });

        for (int i = 0; i < navPresenter.navList.size(); i ++) {
            navigation.getMenu().add(navPresenter.navList.get(i));
        }

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        Log.d("itemListApp", "onAttach");

        super.onAttach(activity);
        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(new NavigationModule(NavigationFragment.this));
        activityGraph.inject(this);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("itemListApp", "onCreateFragment: " + (savedInstanceState != null));
        if (savedInstanceState != null){
            navPresenter.navList = savedInstanceState.getStringArrayList("navList");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("itemListApp", "onSaveInstanceState size: " + navPresenter.navList.size());

        outState.putStringArrayList("navList", navPresenter.navList);
        super.onSaveInstanceState(outState);

    }

    public boolean containsItem(String query) {

        for (int i = 0; i < navigation.getMenu().size(); i++) {
            if (navigation.getMenu().getItem(i).getTitle().toString().toLowerCase().equals(query.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void onEvent(AddItemsEvent event) {

        navPresenter.updateNavItems(event.addItems);
    }

    @Override
    public void updateNavigationWithItems(ArrayList<String> items) {
        Log.d("itemListApp", "updateNavigationWithItems size: " + items.size());

        for (int i = 0; i < items.size(); i ++){
            if (!containsItem(items.get(i))) {
                navigation.getMenu().add(items.get(i));
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}