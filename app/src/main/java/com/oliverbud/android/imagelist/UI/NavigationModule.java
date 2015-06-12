package com.oliverbud.android.imagelist.UI;

import android.util.Log;

import com.oliverbud.android.imagelist.Application.AppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by oliverbudiardjo on 6/8/15.
 */

@Module(
        injects =
                NavigationFragment.class,
        addsTo = AppModule.class

)
public class NavigationModule {

    NavView nv;

    public NavigationModule(NavView nv){
        this.nv = nv;
    }

    @Provides
    @Singleton
    public NavView provideView() {
        return nv;
    }

    @Provides
    public NavigationPresenter provideNavigationPresenter(NavView nv){
        Log.d("itemListApp", "provideNavigationPresenter");

        return new NavigationPresenter(nv);

    }

}
