package com.mheev.helpthemshop.model;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mheev on 9/15/2016.
 */
public class ShoppingItemRepository {
    private ObservableArrayList<ShoppingItem> items  = new ObservableArrayList<ShoppingItem>();
    private static final String TAG = "ShoppingItemRepository";

    public ShoppingItemRepository(List<ShoppingItem> items) {
        this.items.addAll(items);
    }

    public ObservableArrayList<ShoppingItem> getItems() {
        return items;
    }

    public ShoppingItem getItem(String id) {
        Log.d("ShoppingItemRepository", "item size: " + items.size());
        for (ShoppingItem item : items) {
            if (item.getId().equals(id))
                return item;
        }
        return null;
    }
    public void removeItem(String id){
        ShoppingItem item = getItem(id);
        items.remove(item);
    }

    public void addItem(ShoppingItem item) {
        if (item.isNew()) {
            Log.d(TAG, "add new item in repository");
            addNewItem(item);
        } else {
            Log.d(TAG, "update current item in repository");
            updateItem(item);
        }
    }

    public void addNewItem(ShoppingItem item) {
        items.add(item);
    }
    public void updateItem(ShoppingItem updatedItem) {
        int index = getIndex(updatedItem);
        items.set(index, updatedItem);
    }

    private int getIndex(ShoppingItem item) {
        for (ShoppingItem repoItem : items) {
            if (item.getId().equals(repoItem.getId())) {
                int index = items.indexOf(repoItem);
                return index;
            }
        }
        return -1;
    }


}
