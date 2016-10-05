package com.mheev.helpthemshop.model;

import android.databinding.ObservableArrayList;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 10/5/2016.
 */

public interface DataRepository {
    public void loadItems();
    public void updateItem(ShoppingItem item);
    public void removeItem(ShoppingItem item);
    public void removeItem(int position);
    public void deleteAll();
    public ObservableArrayList<ShoppingItem> getItems();
    public ShoppingItem getItem(String id);
    public ShoppingItem getItem(int position);

}
