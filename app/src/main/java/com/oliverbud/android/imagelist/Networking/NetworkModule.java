package com.oliverbud.android.imagelist.Networking;

import com.oliverbud.android.imagelist.Networking.NetworkManager;
import com.oliverbud.android.imagelist.Networking.ImageApi;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by oliverbudiardjo on 6/12/15.
 */
@Module(
        library = true
)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(){
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    public ImageApi provideImageApi(OkHttpClient client){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://ajax.googleapis.com")
                .setClient(new OkClient(client))
                .build();
        return restAdapter.create(ImageApi.class);
    }

    @Provides @Singleton public NetworkManager provideNetworkManager(ImageApi service) {

        return new NetworkManager(service);
    }
}
