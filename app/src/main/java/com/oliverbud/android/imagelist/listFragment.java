package com.oliverbud.android.imagelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */
public class listFragment extends Fragment implements ImageListView{

    @Inject
    ImageListPresenter listPresenter;
    private ObjectGraph activityGraph;


    @InjectView(R.id.imageList) ListView imageList;
    ImageListAdapter adapter;

    @Arg
    String title;

    @Arg
    ArrayList<ImageDataItem> dataList;

    EndlessScrollListener scrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("itemListApp", "onCreate Fragment");

        FragmentArgs.inject(this);

        if (savedInstanceState != null){
            dataList = (ArrayList<ImageDataItem>)savedInstanceState.get("listData");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("listData", dataList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("itemListApp", "onActivityCreated Fragment");

        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(new PresenterModule(this));
        activityGraph.inject(this);
    }

    @Override
    public void onDestroy() {
        Log.d("itemListApp", "onDestroy Fragment");
        super.onDestroy();
        activityGraph = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("itemListApp", "onCreateView Fragment");

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ButterKnife.inject(this, view);

        this.scrollListener = new EndlessScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("itemListApp", "onLoadMore Activity");

                listPresenter.loadMore(title);
            }

            @Override
            public void onLoadFail() {
                Log.d("itemListApp", "onLoadFail scrollListener");

                setLoading(false);
            }
        };
        imageList.setOnScrollListener(scrollListener);

        if (dataList != null && !dataList.isEmpty()) {
            imageList.setAdapter(this.adapter = new ImageListAdapter(getActivity(), dataList));
        }
        return view;
    }


    @Override
    public void onStart() {
        Log.d("itemListApp", "onStart Fragment");

        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("itemListApp", "onStop Fragment");

        EventBus.getDefault().unregister(this);
        listPresenter.decouple();

    }

    public void onEvent(searchMessage event){
        title = event.search;
        listPresenter.searchFor(title);
    }

    @Override
    public void setItems(ArrayList<ImageDataItem> listData) {
        this.imageList.setVisibility(View.VISIBLE);
        this.dataList = listData;
        if (adapter == null || adapter.getData() != dataList) {
            imageList.setAdapter(this.adapter = new ImageListAdapter(getActivity(), dataList));
        }
        else{
            imageList.setAdapter(this.adapter);
        }
    }

    @Override
    public void addItems(ArrayList<ImageDataItem> listData) {

        this.adapter.notifyDataSetChanged();

    }

    @Override
    public void displayLoading() {
        Log.d("itemListApp", "displayLoading Activity");
        imageList.setVisibility(View.INVISIBLE);


    }

    @Override
    public void displayError() {
        Log.d("itemListApp", "displayError Activity");
        this.scrollListener.onLoadFail();
    }




}
