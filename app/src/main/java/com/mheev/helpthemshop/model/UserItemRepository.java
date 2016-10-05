package com.mheev.helpthemshop.model;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.ShoppingListUtils;

/**
 * Created by mheev on 10/5/2016.
 */

public class UserItemRepository implements DataRepository {
    private UserItemDbHelper dbHelper;
    private ObservableBoolean isLoading = new ObservableBoolean(true);
    private ObservableArrayList<ShoppingItem> items = new ObservableArrayList<>();

    public UserItemRepository(UserItemDbHelper dbHelper) {
        this.dbHelper = dbHelper;
        loadItems();
    }

    @Override
    public ShoppingItem getItem(String id) {
        return dbHelper.getItem(id);
    }

    @Override
    public ShoppingItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public ObservableArrayList<ShoppingItem> getItems() {
        return items;
    }

    @Override
    public void loadItems() {
        isLoading.set(true);
        items.addAll(dbHelper.getItems());
        isLoading.set(false);
    }

    @Override
    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    /****need update list*****/
    @Override
    public void updateItem(ShoppingItem item) {
        ShoppingListUtils utils = new ShoppingListUtils(items);
        if (item.isNew()) {
            dbHelper.addItem(item);
            items.add(item);
        }else{
            dbHelper.updateItem(item);
            utils.updateItemInMemory(item);
        }
        dbHelper.updateItem(item);
    }

    @Override
    public void removeItem(ShoppingItem item) {
        dbHelper.removeItem(item.getId());
        items.remove(item);
    }

    @Override
    public void removeItem(int position) {
        ShoppingItem item = items.get(position);
        removeItem(item);
    }

    @Override
    public void deleteAll() {
        dbHelper.clear();
    }


}
