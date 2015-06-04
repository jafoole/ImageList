package com.oliverbud.android.imagelist;

import android.app.Application;
import android.content.Context;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */
public class App extends Application {
    private ObjectGraph objectGraph;

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph.inject(this);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this));
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    public static Context getAppContext() {
        return App.context;
    }
}
