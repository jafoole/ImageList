package com.oliverbud.android.imagelist;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by oliverbudiardjo on 6/15/15.
 */

@Module(
        library = true
)
public class ImageIdKeeperModule {

    @Provides
    @Singleton
    public ImageIDKeeper provideIdKeeper(){
        return new ImageIDKeeper();
    }
}
