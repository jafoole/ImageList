package com.oliverbud.android.imagelist.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;
import com.oliverbud.android.imagelist.EventBus.SearchEvent;
import com.oliverbud.android.imagelist.UI.Util.ImageDataItem;
import com.oliverbud.android.imagelist.ImageListPresenter;
import com.oliverbud.android.imagelist.ImageListView;
import com.oliverbud.android.imagelist.PresenterModule;
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

    @InjectView(R.id.listPager) public ViewPager listsViewPager;
    @InjectView(R.id.spinner) public ProgressBar spinner;

    @Inject ImageListView ilv;

    listPagerAdapter myPagerAdapter;


    @Inject
    ImageListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lists_display_layout, container);
        ButterKnife.inject(this, view);
        Log.d("itemListApp", "onCreateView ListDisplayFragment");

        listsViewPager.setAdapter(myPagerAdapter);
        listsViewPager.setOffscreenPageLimit(2);
        spinner.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("itemListApp", "onAttach ListDisplayFragment");

        super.onAttach(activity);
        activityGraph = ((App) getActivity().getApplication()).createScopedGraph(new PresenterModule(ListsDisplayFragment.this));
        activityGraph.inject(this);

        myPagerAdapter = new listPagerAdapter();


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
        for (int i = 0; i < 3; i ++){
            myPagerAdapter.setLoadTrue(i);
        }
    }

    public void onEvent(NavItemSelectedEvent event){
        if (!event.item.toString().equals(((MainActivity)getActivity()).currentSearch)) {
            for (int i = 0; i < 3; i ++){
                myPagerAdapter.setLoadTrue(i);
            }
            presenter.searchFor(event.item.toString());
        }
    }

    @Override
    public void setItems(ArrayList<ImageDataItem> listData) {
        spinner.setVisibility(View.GONE);
        listsViewPager.setVisibility(View.VISIBLE);

        Log.d("itemListApp", "listData Fragment");
        for (int i = 0; i < 3; i ++){

            myPagerAdapter.setListAdapterForPosition(new ImageListAdapter(listData), i);
        }

    }

    @Override
    public void addItems(ArrayList<ImageDataItem> listData) {
        Log.d("itemListApp", "addItems Fragment");

        for (int i = 0; i < 3; i ++){
            myPagerAdapter.updateAdapter(i);
        }

    }

    @Override
    public void displayLoading() {
        Log.d("itemListApp", "displayLoading Fragment");
        spinner.setVisibility(View.VISIBLE);
        listsViewPager.setVisibility(View.GONE);

    }

    @Override
    public void displayError() {
        for (int i = 0; i < 3; i ++) {
            myPagerAdapter.setLoadFalse(i);
        }
        Log.d("itemListApp", "displayError Fragment");
    }



    public class listPagerAdapter extends PagerAdapter {

        SmartListView[] listViews = new SmartListView[3];
        ImageListAdapter[] listViewAdapters = new ImageListAdapter[3];


        public void setListAdapterForPosition(ImageListAdapter ila, int position){
            listViews[position].setAdapter(ila);
            listViewAdapters[position] = ila;
        }

        public void updateAdapter(int i ) {
            if (listViewAdapters[i] != null) {
                listViewAdapters[i].notifyDataSetChanged();

            }
        }

        public void setLoadFalse(int i){
            listViews[i].getScrollListener().stopLoading();
        }

        public void setLoadTrue(int i){
            listViews[i].getScrollListener().tryLoading();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "bacon";
                case 1:
                    return "salad";
                case 2:
                    return "spoons";

            }
            return "default";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SmartListView lv = new SmartListView(getActivity());
            Log.d("itemListApp", "instantiateItem listPagerAdapter");

            listViews[position] = lv;

            lv.setScrollListener(new EndlessScrollListener(lv.getLayoutManager()) {
                @Override
                public void onLoadMore(int current_page) {
                    presenter.loadMore(((MainActivity)getActivity()).currentSearch);

                }
            });

            if (presenter.list != null){
                lv.setAdapter(new ImageListAdapter(presenter.list));
            }


            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(lv, layoutParams);
            return lv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            listViews[position] = null;
            listViewAdapters[position] = null;

            super.destroyItem(container, position, object);
        }
    }
}
