package com.mheev.helpthemshop.repository;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.ApiCreateResponse;
import com.mheev.helpthemshop.model.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 9/15/2016.
 */
public class ShoppingItemRepository {
    private ObservableArrayList<ShoppingItem> items;

    public ShoppingItemRepository(ObservableArrayList<ShoppingItem> items) {
        this.items = items;
    }
    public ShoppingItemRepository(List<ShoppingItem> items){
        this.items = new ObservableArrayList<ShoppingItem>();
        this.items.addAll(items);
    }

    public ObservableArrayList<ShoppingItem> getItems(){
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

    public void addItem(ShoppingItem item) {
        if (getItem(item.getId()) == null) {
            items.add(item);
        }
    }

    public List<ShoppingItem> getNonSyncItems(){
        List<ShoppingItem> items = new ArrayList<>();
        for (ShoppingItem item : items) {
            if(item.isSync())
                items.add(item);
        }
        return items;
    }

}
