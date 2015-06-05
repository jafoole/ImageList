package com.oliverbud.android.imagelist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.GenericEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import icepick.Icepick;


public class MainActivity extends AppCompatActivity implements ImageListView{

    @InjectView(R.id.searchInput)Toolbar searchInput;
    @InjectView(R.id.listPager) ViewPager listPager;
    @InjectView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @InjectView(R.id.navigation) NavigationView navigation;
    @InjectView(R.id.tabLayout) TabLayout tabLayout;
    @InjectView(R.id.coordinatorLayout)CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.spinner)ProgressBar spinner;


    listPagerAdapter myPagerAdapter;

    String currentSearch = null;
    ArrayList<String> searchStrings;

    ActionBarDrawerToggle abdt;

    @Inject
    ImageListPresenter presenter;

    private ObjectGraph activityGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("itemListApp", "onCreate Activity");

        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        spinner.setIndeterminate(true);
        spinner.setVisibility(View.GONE);


        activityGraph = ((App) this.getApplication()).createScopedGraph(new PresenterModule(MainActivity.this));
        activityGraph.inject(this);

        setSupportActionBar(searchInput);

        searchStrings = new ArrayList<String>();
        abdt = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.setDrawerListener(abdt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        abdt.syncState();



        if (savedInstanceState != null){
            currentSearch = savedInstanceState.getString("currentSearchString");
            searchStrings = savedInstanceState.getStringArrayList("searchStrings");
            presenter.list = savedInstanceState.getParcelableArrayList("presenterList");
            presenter.page = savedInstanceState.getInt("page");
            for (String searchString : searchStrings) {
                navigation.getMenu().add(searchString);
            }
        }



        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                String searchParam = (String)menuItem.getTitle();
                drawerLayout.closeDrawer(GravityCompat.START);
                if (!searchParam.equals(currentSearch)) {
                    for (int i = 0; i < 3; i ++){
                        myPagerAdapter.setLoadTrue(i);
                    }
                    presenter.searchFor(searchParam);

                    EventBus.getDefault().post(new GenericEvent("SNACKS"));


                    searchInput.setTitle(searchParam);
                    currentSearch = searchParam;
                    if (!searchStrings.contains(currentSearch)) {
                        searchStrings.add(currentSearch);
                    }
                }
                MenuItem searchItem = menu.findItem(R.id.search);
                searchItem.collapseActionView();

                return false;
            }
        });

        initializePaging();

        handleIntent(getIntent());

    }

    public void initializePaging(){
        Log.d("itemListApp", "initializePaging");

        myPagerAdapter = new listPagerAdapter();
        listPager.setAdapter(myPagerAdapter);
        listPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(listPager);
    }

    @Override
    public void onBackPressed() {
        MenuItem menuItem = this.menu.findItem(R.id.search);
        if (!menuItem.collapseActionView()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("itemListApp", "onNewIntent");

        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("itemListApp", "handling intent: " + query);

            for (int i = 0; i < 3; i ++){
                myPagerAdapter.setLoadTrue(i);
            }
            presenter.searchFor(query);

            MenuItem menuItem = this.menu.findItem(R.id.search);
            menuItem.collapseActionView();
            searchInput.setTitle(query);
            if (!containsItem(query)) {
                navigation.getMenu().add(query);
            }
            currentSearch = query;
            if (!searchStrings.contains(currentSearch)) {
                searchStrings.add(currentSearch);
            }
        }
    }

    public boolean containsItem(String query){

        for (int i = 0; i < navigation.getMenu().size(); i ++){
            if (navigation.getMenu().getItem(i).getTitle().toString().toLowerCase().equals(query.toLowerCase())){
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("itemListApp", "onResume Activity");
        if (currentSearch != null){
            getSupportActionBar().setTitle(currentSearch);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("itemListApp", "onSaveInstanceState Activity");

        outState.putString("currentSearchString", currentSearch);
        outState.putStringArrayList("searchStrings", searchStrings);
        outState.putParcelableArrayList("presenterList", presenter.list);
        outState.putInt("page", presenter.page);
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    public void onEvent(GenericEvent event) {
        if (event.event != "SNACKS") {
            View.OnClickListener myOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            };

            Snackbar
                    .make(coordinatorLayout, "HULLO", Snackbar.LENGTH_LONG)
                    .setAction("CLICK", myOnClickListener)
                    .show();
        }
    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setItems(ArrayList<ImageDataItem> listData) {
        listPager.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        for (int i = 0; i < 3; i ++){


            myPagerAdapter.setListAdapterForPosition(new ImageListAdapter(listData), i);
        }

    }

    @Override
    public void addItems(ArrayList<ImageDataItem> listData) {

        for (int i = 0; i < 3; i ++){
            myPagerAdapter.updateAdapter(i);
        }

    }

    @Override
    public void displayLoading() {
        Log.d("itemListApp", "displayLoading Activity");
        listPager.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

    }

    @Override
    public void displayError() {
        for (int i = 0; i < 3; i ++) {
            myPagerAdapter.setLoadFalse(i);
        }
        Log.d("itemListApp", "displayError Activity");
    }


    public class listPagerAdapter extends PagerAdapter{

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
            SmartListView lv = new SmartListView(MainActivity.this);

            listViews[position] = lv;

            lv.setScrollListener(new EndlessScrollListener(lv.getLayoutManager()) {
                @Override
                public void onLoadMore(int current_page) {
                    MainActivity.this.presenter.loadMore(currentSearch);

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
