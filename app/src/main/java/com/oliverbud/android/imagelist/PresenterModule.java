package com.oliverbud.android.imagelist;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */

@Module(
        injects =
                MainActivity.class,
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

    @Provides @Singleton public ImageApi provideApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .build();
        return restAdapter.create(ImageApi.class);
    }

    @Provides
    public ImageListPresenter provideImageListPresenter(ImageListView ilv, ImageApi service){
        return new ImageListPresenter(ilv, service);

    }
}
