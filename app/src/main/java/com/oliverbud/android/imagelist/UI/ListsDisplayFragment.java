package com.oliverbud.android.imagelist.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;
import com.oliverbud.android.imagelist.EventBus.SearchEvent;
import com.oliverbud.android.imagelist.EventBus.UpdateListAtPosition;
import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;
import com.oliverbud.android.imagelist.R;
import com.oliverbud.android.imagelist.UI.Util.EndlessScrollListener;
import com.oliverbud.android.imagelist.UI.Util.ImageListAdapter;
import com.oliverbud.android.imagelist.UI.Util.SmartListView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */
public class ListsDisplayFragment extends Fragment implements ImageListView {

    ObjectGraph activityGraph;

    @InjectView(R.id.imageList) public RecyclerView imageList;
    @InjectView(R.id.spinner) public ProgressBar spinner;

    ImageListAdapter listAdapter;
    LinearLayoutManager layoutManager;
    EndlessScrollListener scrollListener;


    @Inject
    ImageListPresenter presenter;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lists_display_layout, container);
        ButterKnife.inject(this, view);
        Log.d("itemListApp", "onCreateView ListDisplayFragment");


        imageList.setLayoutManager(layoutManager = new LinearLayoutManager(getActivity()));


        if (presenter.list != null){
            imageList.setAdapter(listAdapter = new ImageListAdapter(presenter.list));
        }
        imageList.setOnScrollListener(scrollListener = new EndlessScrollListener(((LinearLayoutManager) imageList.getLayoutManager())) {
            @Override
            public void onLoadMore(int current_page) {
                presenter.loadMore(((MainActivity) getActivity()).currentSearch);

            }
        });

        spinner.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("itemListApp", "onAttach ListDisplayFragment");

        super.onAttach(activity);
        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(new PresenterModule(ListsDisplayFragment.this));
        activityGraph.inject(this);



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("itemListApp", "onCreateFragment: " + (savedInstanceState != null));
        if (savedInstanceState != null){
            presenter.list = savedInstanceState.getParcelableArrayList("list");
            presenter.page = savedInstanceState.getInt("page");
        }




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("list", presenter.list);
        outState.putInt("page", presenter.page);
        super.onSaveInstanceState(outState);

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

    public void onEvent(SearchEvent event){
        presenter.searchFor(event.search);
        scrollListener.tryLoading();

    }

    public void onEvent(NavItemSelectedEvent event){
        if (!event.item.equals(((MainActivity)getActivity()).currentSearch)) {
            scrollListener.tryLoading();
            presenter.searchFor(event.item.toString());
        }
    }

    public void onEvent(UpdateListAtPosition event){
        listAdapter.notifyItemChanged(event.position);
        if (event.success){
            Snackbar
                    .make((View) ((MainActivity)getActivity()).getCoordinatorLayout(), "successful ping", Snackbar.LENGTH_LONG)
                    .show();
        }
        else{
            Snackbar
                    .make((View) ((MainActivity)getActivity()).getCoordinatorLayout(), "ping Failed", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void setItems(ArrayList<ImageDataItem> listData) {
        spinner.setVisibility(View.GONE);
        imageList.setVisibility(View.VISIBLE);

        imageList.setAdapter(listAdapter = new ImageListAdapter(presenter.list));

        Log.d("itemListApp", "listData Fragment");


    }

    @Override
    public void addItems(ArrayList<ImageDataItem> listData) {
        Log.d("itemListApp", "addItems Fragment");
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void displayLoading() {
        Log.d("itemListApp", "displayLoading Fragment");
        spinner.setVisibility(View.VISIBLE);
        imageList.setVisibility(View.GONE);

    }

    @Override
    public void displayError() {
        scrollListener.stopLoading();

        Log.d("itemListApp", "displayError Fragment");
    }


}
