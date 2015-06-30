package com.oliverbud.android.imagelist.UI.Util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.oliverbud.android.imagelist.R;

import de.greenrobot.event.EventBus;

/**
 * Created by oliverbudiardjo on 6/4/15.
 */
public class SmartListView extends LinearLayout {

    RecyclerView lv;

    LinearLayoutManager layoutManager;

    EndlessScrollListener scrollListener;

    EventBus bus = EventBus.getDefault();

    public SmartListView(Context context){
        super(context);

        View view =  LayoutInflater.from(getContext()).inflate(
                R.layout.smart_list, null);

        lv = (RecyclerView)view.findViewById(R.id.imageList);


        layoutManager = new LinearLayoutManager(context);

        lv.setLayoutManager(layoutManager);


        this.addView(view);


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
