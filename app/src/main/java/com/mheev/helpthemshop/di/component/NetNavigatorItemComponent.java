package com.mheev.helpthemshop.di.component;

import com.mheev.helpthemshop.activity.BuyingFragment;
import com.mheev.helpthemshop.activity.ItemManagementFragment;
import com.mheev.helpthemshop.di.module.NavigatorModule;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.util.ItemScope;
import com.mheev.helpthemshop.viewmodel.ItemViewModel;

import dagger.Component;

/**
 * Created by mheev on 9/22/2016.
 */

@ItemScope
@Component(dependencies = NetComponent.class, modules = {ItemModule.class, NavigatorModule.class})
public interface NetNavigatorItemComponent {

    void inject(ItemManagementFragment itemManagementFragment);
    void inject(ItemViewModel itemViewModel);//move to navigatorComponent
    void inject(BuyingFragment buyingFragment);
}
