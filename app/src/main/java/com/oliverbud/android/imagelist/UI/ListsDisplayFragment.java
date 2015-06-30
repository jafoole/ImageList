package com.oliverbud.android.imagelist.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;
import com.oliverbud.android.imagelist.R;
import com.oliverbud.android.imagelist.UI.Util.EndlessScrollListener;
import com.oliverbud.android.imagelist.UI.Util.ImageListAdapter;

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
    @InjectView(R.id.networkError) public TextView networkError;

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
            presenter.currentSearchDisplay = savedInstanceState.getString("currentSearchDisplay");
        }




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("list", presenter.list);
        outState.putInt("page", presenter.page);
        outState.putString("currentSearchDisplay", presenter.currentSearchDisplay);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unregisterBus();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.registerBus();
    }


    @Override
    public void setItems(ArrayList<ImageDataItem> listData) {
        spinner.setVisibility(View.GONE);
        imageList.setVisibility(View.VISIBLE);
        networkError.setVisibility(View.GONE);

        imageList.setAdapter(listAdapter = new ImageListAdapter(presenter.list));

        Log.d("itemListApp", "listData Fragment");


    }

    @Override
    public void updateItems(int position) {
        Log.d("itemListApp", "addItems Fragment");
        if (position >= 0){
            listAdapter.notifyItemChanged(position);
        }
        else {
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void displayLoading() {
        scrollListener.tryLoading();
        Log.d("itemListApp", "displayLoading Fragment");
        spinner.setVisibility(View.VISIBLE);
        imageList.setVisibility(View.GONE);
        networkError.setVisibility(View.GONE);

    }

    @Override
    public void displayError() {
        scrollListener.stopLoading();
        spinner.setVisibility(View.GONE);
        networkError.setVisibility(View.VISIBLE);
        Log.d("itemListApp", "displayError Fragment");
    }


}
