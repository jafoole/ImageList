package com.oliverbud.android.imagelist.Application;

import android.content.Context;

import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.UI.MainActivity;
import com.oliverbud.android.imagelist.UI.NavigationModule;
import com.oliverbud.android.imagelist.UI.PresenterModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */
@Module(
        injects = {
                App.class,
                MainActivity.class
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

    @Provides
    @Singleton
    public ImageIDKeeper provideIdKeeper(){
        return new ImageIDKeeper();
    }


}
