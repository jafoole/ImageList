package com.oliverbud.android.imagelist.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.AddItemsEvent;
import com.oliverbud.android.imagelist.EventBus.ItemClickedEvent;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;
import com.oliverbud.android.imagelist.EventBus.removeSavedItem;
import com.oliverbud.android.imagelist.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class NavigationFragment extends Fragment implements NavView {

    @InjectView(R.id.history) ListView history;

    @InjectView(R.id.saved) ListView saved;


    ArrayAdapter<String> historyAdapter;

    ArrayAdapter<String> savedAdapter;


    @Inject
    NavigationPresenter navPresenter;

    ObjectGraph activityGraph;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_layout, container);
        Log.d("itemListApp", "onCreateView Fragment");

        ButterKnife.inject(this, view);

        history.setOnItemClickListener((parent, view1, position, id) ->  EventBus.getDefault().post(new NavItemSelectedEvent(navPresenter.navList.get(position))));



        historyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, navPresenter.navList);

        history.setAdapter(historyAdapter);

        savedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, navPresenter.savedList);

        saved.setAdapter(savedAdapter);

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
            navPresenter.savedList = savedInstanceState.getStringArrayList("savedList");

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("itemListApp", "onSaveInstanceState size: " + navPresenter.navList.size());

        outState.putStringArrayList("navList", navPresenter.navList);
        outState.putStringArrayList("savedList", navPresenter.savedList);

        super.onSaveInstanceState(outState);

    }


    public void onEvent(AddItemsEvent event) {

        navPresenter.updateNavItems(event.addItems);
    }

    public void onEvent(ItemClickedEvent event) {
        ArrayList<String> list = new ArrayList<>();
        list.add(event.getTitle());
        navPresenter.updateSavedItems(list);
    }


    @Override
    public void updateNavigationWithItems(ArrayList<String> items) {
        Log.d("itemListApp", "updateNavigationWithItems size: " + items.size());

        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateSavedWithItems(ArrayList<String> items) {
        savedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        navPresenter.unregisterBus();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        navPresenter.registerBus();

    }
}