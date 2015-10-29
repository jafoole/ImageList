package com.oliverbud.android.imagelist.UI;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.Database.Image;
import com.oliverbud.android.imagelist.Database.Image$Table;
import com.oliverbud.android.imagelist.EventBus.AddItemsEvent;
import com.oliverbud.android.imagelist.EventBus.ItemClickedEvent;
import com.oliverbud.android.imagelist.EventBus.NavItemSelectedEvent;
import com.oliverbud.android.imagelist.EventBus.SearchEvent;
import com.oliverbud.android.imagelist.EventBus.UpdateListAtPosition;
import com.oliverbud.android.imagelist.EventBus.removeSavedItem;
import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.Networking.NetworkManager;
import com.oliverbud.android.imagelist.Networking.NetworkResponseData;
import com.oliverbud.android.imagelist.Networking.NetworkResponseData$Table;
import com.oliverbud.android.imagelist.R;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import dagger.ObjectGraph;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.searchInput)Toolbar searchInput;
    @Optional @InjectView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Optional @InjectView(R.id.linearLayout) LinearLayout linearLayout;
    @InjectView(R.id.coordinatorLayout)CoordinatorLayout coordinatorLayout;

    @Inject ImageIDKeeper idKeeper;
    @Inject NetworkManager networkManager;

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

        activityGraph = ((App)getApplication()).getObjectGraph();
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


            ArrayList<String> addItems = new ArrayList<String>();
            addItems.add(query);
            EventBus.getDefault().post(new AddItemsEvent(addItems));
            EventBus.getDefault().post(new SearchEvent(query));

            currentSearch = query;
            if (currentSearch != null){
                searchInput.setTitle(currentSearch);
            }
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
            searchInput.setTitle(currentSearch);
        }

        Image image1 = new Image();
        image1.setName("robert");
        image1.save();

        Image image2 = new Image();
        image2.setName("flaubert");
        image2.save();

        try {
            List<Image> images11 = new Select().from(Image.class)
                    .where(
                            Condition.column(Image$Table.NAME).eq("robert"),
                            Condition.column(Image$Table.FEET).eq(true)).queryList();
            Log.d("DBFlow", "roberts: " + images11.size());

            List<Image> images22 = new Select().from(Image.class)
                    .where(
                            Condition.column(Image$Table.NAME).eq("flaubert"),
                            Condition.column(Image$Table.FEET).eq(true)).queryList();
            Log.d("DBFlow", "flauberts: " + images22.size());

            List<NetworkResponseData> images33 = new Select().from(NetworkResponseData.class)
                    .where(
                            Condition.column(NetworkResponseData$Table.FEET).eq(true)).queryList();
            Log.d("DBFlow", "datas: " + images33.size());

        }
        catch(SQLiteException s){

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
        if (event.getStatus() == 0) {

            idKeeper.addToList(event.getTitle());
            Observable<Object> observable = networkManager.ping();
            event.setStatus(1);
            Subscriber pingSubscriber = new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    event.setStatus(0);
                    EventBus.getDefault().post(new UpdateListAtPosition(event.position, false, true));
                    idKeeper.removeFromList(event.getTitle());
                    EventBus.getDefault().post(new removeSavedItem(event.getTitle()));

                }

                @Override
                public void onNext(Object o) {
                    event.setStatus(3);
                    EventBus.getDefault().post(new UpdateListAtPosition(event.position, true, true));

                }
            };

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(pingSubscriber);

        }
        else if(event.getStatus() == 3){
            event.setStatus(0);
            idKeeper.removeFromList(event.getTitle());
            EventBus.getDefault().post(new UpdateListAtPosition(event.position, true, false));
            EventBus.getDefault().post(new removeSavedItem(event.getTitle()));

        }
    }

    public CoordinatorLayout getCoordinatorLayout(){
        return coordinatorLayout;
    }

    public void onEvent(NavItemSelectedEvent event) {

        String searchParam = (String)event.item;
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (!searchParam.equals(currentSearch)) {

            searchInput.setTitle(searchParam);
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
            Log.d("itemListApp", "homeSelected: " + id);

        }

        Log.d("itemListApp", "onOptionsItemSelected: " + id);

        return super.onOptionsItemSelected(item);
    }

}
