package com.mheev.helpthemshop.di.module;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mheev on 9/15/2016.
 */
@Module
public class AppModule {
    private final Application application;
    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

}
