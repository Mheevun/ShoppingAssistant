package com.mheev.helpthemshop.activity;

import android.app.Activity;
import android.view.View;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/21/2016.
 */ // Container Activity must implement this interface
public interface ActivityNavigator {
    public void toBuyingFragment(ShoppingItem item);
    public void toItemDetailsActivity(ShoppingItem item, View transitionView);
    public void toItemDetailsActivity(ShoppingItem item, View transitionView, Activity listenerResultActivity);
}
