package com.mheev.helpthemshop;

import android.app.Application;

import com.mheev.helpthemshop.api.retrofit.ApiUtil;
import com.mheev.helpthemshop.di.component.DaggerNetComponent;
import com.mheev.helpthemshop.di.component.NetComponent;
import com.mheev.helpthemshop.di.module.AppModule;
import com.mheev.helpthemshop.di.module.NetModule;

/**
 * Created by mheev on 9/13/2016.
 */
public class App extends Application {
    //    private static ItemComponent itemComp;
    private static NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        itemComp = DaggerItemComponent.builder()
//                .itemModule(new ItemModule())
//                .build();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(ApiUtil.ROOT_URI))
                .build();
    }

//    public static ItemComponent getItemComponent() {
//        return itemComp;
//    }

    public static NetComponent getNetComponent() {
        return netComponent;
    }
}
