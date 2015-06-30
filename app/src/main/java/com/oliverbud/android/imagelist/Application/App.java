package com.oliverbud.android.imagelist.Application;

import android.app.Application;
import android.content.Context;

import com.oliverbud.android.imagelist.ImageIdKeeperModule;
import com.oliverbud.android.imagelist.Networking.NetworkModule;
import com.squareup.leakcanary.LeakCanary;

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
//        LeakCanary.install(this);
        App.context = getApplicationContext();
        objectGraph = ObjectGraph.create(getModules().toArray());
        objectGraph = objectGraph.plus(new NetworkModule());
        objectGraph.inject(this);
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AppModule(this), new ImageIdKeeperModule());
    }

    public ObjectGraph createScopedGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    public static Context getAppContext() {
        return App.context;
    }

    public ObjectGraph getObjectGraph(){
        return objectGraph;
    }
}
