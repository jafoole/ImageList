package com.oliverbud.android.imagelist;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
