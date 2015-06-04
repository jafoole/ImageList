package com.oliverbud.android.imagelist;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by oliverbudiardjo on 6/3/15.
 */

//@Module(
//        injects =
//                listFragment.class,
//                addsTo = AppModule.class
//
//)
//public class PresenterModule {
//
//    ImageListView ilv;
//
//    public PresenterModule(ImageListView ilv){
//        this.ilv = ilv;
//    }
//
//    @Provides @Singleton public ImageListView provideView() {
//        return ilv;
//    }
//
//    @Provides
//    public ImageListPresenter provideImageListPresenter(ImageListView ilv){
//        return new ImageListPresenter(ilv);
//
//    }
//}
