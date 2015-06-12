package com.oliverbud.android.imagelist.Application;

import android.content.Context;

import com.oliverbud.android.imagelist.Application.App;
import com.oliverbud.android.imagelist.NetworkManager;
import com.oliverbud.android.imagelist.Networking.ImageApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */
@Module(
        injects = {
                App.class
        },
        library = true

)
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }


}
