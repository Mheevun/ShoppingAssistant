package com.mheev.helpthemshop.di.component;

import com.mheev.helpthemshop.activity.ItemSelectionFragment;
import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.di.module.AppModule;
import com.mheev.helpthemshop.di.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by mheev on 9/19/2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    Retrofit retrofit();
    ItemRequestManager requestManager();
    ShoppingItemClient client();

    void inject(ItemSelectionFragment itemSelectionFragment);
}
