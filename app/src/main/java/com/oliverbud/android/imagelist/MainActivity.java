package com.oliverbud.android.imagelist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import icepick.Icepick;


public class MainActivity extends AppCompatActivity{

    @InjectView(R.id.searchInput)Toolbar searchInput;
    @InjectView(R.id.listPager) ViewPager listPager;
    @InjectView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @InjectView(R.id.navigation) NavigationView navigation;
    @InjectView(R.id.tabLayout) TabLayout tabLayout;



    MyPagerAdapter myPagerAdapter;

    String currentSearch = null;
    ArrayList<String> searchStrings;

    ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("itemListApp", "onCreate Activity");

        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


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

                    EventBus.getDefault().post(new searchMessage(searchParam));
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

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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

            EventBus.getDefault().post(new searchMessage(query));

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
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

//        EventBus.getDefault().register(this);
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

}
