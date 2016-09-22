package com.mheev.helpthemshop.activity;

import android.content.Context;
import android.view.View;

import com.mheev.helpthemshop.model.ShoppingItem;

import rx.Observable;

/**
 * Created by mheev on 9/21/2016.
 */ // Container Activity must implement this interface
public interface ActivityNavigator {
    public void toBuyingFragment(ShoppingItem item);
    public void toItemDetailsActivity(ShoppingItem item, View transitionView);
}
