package com.mheev.helpthemshop.viewmodel;

import android.databinding.ObservableArrayList;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/13/2016.
 */
public class BuyingViewModel {

    public ObservableArrayList<ShoppingItem> displayItems = new ObservableArrayList<>();

    public void addBuyingItem(ShoppingItem item) {
        displayItems.add(item);
    }

    public void removeBuyingItem(int position) {
        displayItems.remove(position);
    }
}
