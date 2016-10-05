package com.mheev.helpthemshop.di.module;

import android.app.Activity;

import com.mheev.helpthemshop.util.ItemScope;
import com.mheev.helpthemshop.util.Navigator;
import com.mheev.helpthemshop.util.NavigatorScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mheev on 10/3/2016.
 */

@Module
public class NavigatorModule {
    private Navigator navigator;

    public NavigatorModule(Activity currentActivity) {
        this.navigator = new Navigator(currentActivity);
    }

    @ItemScope
    @Provides
    public Navigator provideNavigator(){
        return navigator;
    }
}
