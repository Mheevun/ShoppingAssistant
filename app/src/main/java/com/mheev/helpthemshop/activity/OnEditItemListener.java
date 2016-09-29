package com.mheev.helpthemshop.activity;

import android.view.View;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */
public interface OnEditItemListener {
    public void onEditItemDetails(ShoppingItem item, View view);
    public void onEditItemDetailsResult(ShoppingItem item);
    public void onDeleteItem(ShoppingItem item);
}
