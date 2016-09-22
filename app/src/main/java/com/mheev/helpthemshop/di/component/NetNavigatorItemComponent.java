package com.mheev.helpthemshop.di.component;

import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.activity.ItemSelectionFragment;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.di.module.NavigatorModule;
import com.mheev.helpthemshop.util.ItemScope;
import com.mheev.helpthemshop.util.NetNavigatorItemScope;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mheev on 9/22/2016.
 */

@ItemScope
@Component(dependencies = NetComponent.class, modules = {NavigatorModule.class, ItemModule.class})
public interface NetNavigatorItemComponent {
    void inject(ItemDetailsActivity itemDetailsActivity);
    void inject(ItemSelectionFragment itemManagment);
}
