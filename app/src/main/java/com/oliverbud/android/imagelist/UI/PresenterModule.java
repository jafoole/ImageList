package com.oliverbud.android.imagelist.UI;

import com.oliverbud.android.imagelist.Application.AppModule;
import com.oliverbud.android.imagelist.ImageIDKeeper;
import com.oliverbud.android.imagelist.Networking.NetworkManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */

@Module(
        injects =
                ListsDisplayFragment.class,
                addsTo = AppModule.class,
                complete = false

)
public class PresenterModule {

    ImageListView ilv;

    public PresenterModule(ImageListView ilv){
        this.ilv = ilv;
    }

    @Provides @Singleton public ImageListView provideView() {
        return ilv;
    }

    @Provides
    public ImageListPresenter provideImageListPresenter(ImageListView ilv, NetworkManager service, ImageIDKeeper idKeeper){
        return new ImageListPresenter(ilv, service, idKeeper);

    }
}
