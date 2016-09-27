package com.mheev.helpthemshop.model;

import android.databinding.ObservableArrayList;

import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

/**
 * Created by mheev on 9/27/2016.
 */

public class ShoppingItemDbRepository {
    private UserItemDbHelper dbHelper;
    private ObservableArrayList obsItems = new ObservableArrayList<ShoppingItem>();
    public ShoppingItemDbRepository(UserItemDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public ObservableArrayList<ShoppingItem> getItems() {
        List<ShoppingItem> items = dbHelper.getItems();
        obsItems.clear();
        obsItems.add(items);
        return obsItems;
    }


    public ShoppingItem getItem(String id) {
        return dbHelper.getItem(id);
    }


    public void removeItem(String id) {
        dbHelper.removeItem(id);
    }

    public void addItem(ShoppingItem item) {
        dbHelper.addItem(item);
    }

    public void updateItem(ShoppingItem updatedItem) {
        dbHelper.updateItem(updatedItem);
    }
}
