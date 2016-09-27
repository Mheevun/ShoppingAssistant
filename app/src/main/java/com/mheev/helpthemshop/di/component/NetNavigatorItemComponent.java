package com.mheev.helpthemshop.di.component;

import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.activity.ItemManagmentFragment;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.util.ItemScope;

import dagger.Component;

/**
 * Created by mheev on 9/22/2016.
 */

@ItemScope
@Component(dependencies = NetComponent.class, modules = {ItemModule.class})
public interface NetNavigatorItemComponent {
    void inject(ItemDetailsActivity itemDetailsActivity);
}
