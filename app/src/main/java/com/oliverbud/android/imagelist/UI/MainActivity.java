package com.oliverbud.android.imagelist.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.EventBus.AddItemsEvent;
import com.oliverbud.android.imagelist.EventBus.ItemClickedEvent;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;
import com.oliverbud.android.imagelist.EventBus.SearchEvent;
import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.R;

import java.util.ArrayList;


import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.searchInput)Toolbar searchInput;
    @Optional @InjectView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Optional @InjectView(R.id.linearLayout) LinearLayout linearLayout;
    @InjectView(R.id.coordinatorLayout)CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.collapsingToolbarLayout)CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject ImageIDKeeper idKeeper;

    ObjectGraph activityGraph;

    String currentSearch = null;
    ArrayList<String> searchStrings = new ArrayList();

    ActionBarDrawerToggle abdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("itemListApp", "onCreate Activity");

        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        if (isTablet(this)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_main_tablet_landscape);
            ButterKnife.inject(this);
            setSupportActionBar(searchInput);


        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main_phone_protrait);
            ButterKnife.inject(this);
            setSupportActionBar(searchInput);
            abdt = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
            drawerLayout.setDrawerListener(abdt);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            abdt.syncState();
        }

        searchStrings = new ArrayList();

        if (savedInstanceState != null){
            currentSearch = savedInstanceState.getString("currentSearchString");
            searchStrings = savedInstanceState.getStringArrayList("searchStrings");
            EventBus.getDefault().post(new AddItemsEvent(searchStrings));

        }

        activityGraph = ((App) getApplication()).getObjectGraph();
        activityGraph.inject(this);
        handleIntent(getIntent());



    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
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

            MenuItem menuItem = this.menu.findItem(R.id.search);
            menuItem.collapseActionView();
//            collapsingToolbarLayout.setTitle(query);

            ArrayList<String> addItems = new ArrayList<String>();
            addItems.add(query);
            EventBus.getDefault().post(new AddItemsEvent(addItems));
            EventBus.getDefault().post(new SearchEvent(query));

            currentSearch = query;
            if (!searchStrings.contains(currentSearch)) {
                searchStrings.add(currentSearch);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("itemListApp", "onResume Activity");
        if (currentSearch != null){
//            collapsingToolbarLayout.setTitle(currentSearch);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("itemListApp", "onSaveInstanceState Activity");

        outState.putString("currentSearchString", currentSearch);
        outState.putStringArrayList("searchStrings", searchStrings);

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

    public void onEvent(ItemClickedEvent event){
        if (!event.getStatus()) {

            idKeeper.addToList(event.getTitle());
            Observable<String> observable = Observable.just("im an observable");
            observable.subscribe(s -> {

                event.setStatus(true);
                event.getStatusView().setBackground(new ColorDrawable(App.getAppContext().getResources().getColor(R.color.red)));
                Snackbar
                        .make((View) coordinatorLayout, event.getTitle(), Snackbar.LENGTH_LONG)
                        .show();
            });

        }
    }


    public void onEvent(NavItemSelectedEvent event) {

        String searchParam = (String)event.item;
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (!searchParam.equals(currentSearch)) {

//            collapsingToolbarLayout.setTitle(searchParam);
            currentSearch = searchParam;
            if (!searchStrings.contains(currentSearch)) {
                searchStrings.add(currentSearch);
            }
        }
        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.collapseActionView();
    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

}
