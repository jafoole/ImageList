package com.oliverbud.android.imagelist;

import com.oliverbud.android.imagelist.Application.AppModule;
import com.oliverbud.android.imagelist.UI.ListsDisplayFragment;

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

    @Provides @Singleton public NetworkManager provideNetworkManager() {

        return new NetworkManager();
    }

    @Provides
    public ImageListPresenter provideImageListPresenter(ImageListView ilv, NetworkManager service){
        return new ImageListPresenter(ilv, service);

    }
}
