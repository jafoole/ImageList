package com.oliverbud.android.imagelist;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/4/15.
 */
public class smartListView extends LinearLayout {

    RecyclerView lv;

    LinearLayoutManager layoutManager;

    EndlessScrollListener scrollListener;

    EventBus bus = EventBus.getDefault();

    public smartListView(Context context){
        super(context);

        View view =  LayoutInflater.from(getContext()).inflate(
                R.layout.smart_list, null);

        lv = (RecyclerView)view.findViewById(R.id.imageList);


        layoutManager = new LinearLayoutManager(context);

        lv.setLayoutManager(layoutManager);


        this.addView(view);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        bus.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bus.unregister(this);
    }

    public void onEvent(GenericEvent event) {
        Log.d("itemListApp", "ListView ReceivedEvent");

        if (event.event == "SNACKS") {

            Snackbar
                    .make((View)this.getParent(), "bullllllyyyyyyy", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public LinearLayoutManager getLayoutManager(){
        return layoutManager;
    }

    public EndlessScrollListener getScrollListener() {
        return scrollListener;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        lv.setAdapter(adapter);
    }

    public void setScrollListener(RecyclerView.OnScrollListener scrollListener){
        this.scrollListener = (EndlessScrollListener)scrollListener;
        lv.setOnScrollListener(scrollListener);

    }






}
