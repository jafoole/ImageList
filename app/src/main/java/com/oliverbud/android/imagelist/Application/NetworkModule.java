package com.oliverbud.android.imagelist.Application;

import com.oliverbud.android.imagelist.NetworkManager;
import com.oliverbud.android.imagelist.Networking.ImageApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by oliverbudiardjo on 6/12/15.
 */
@Module(
        library = true
)
public class NetworkModule {
    @Provides
    @Singleton
    public ImageApi provideImageApi(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .build();
        return restAdapter.create(ImageApi.class);
    }

    @Provides @Singleton public NetworkManager provideNetworkManager(ImageApi service) {

        return new NetworkManager(service);
    }
}
