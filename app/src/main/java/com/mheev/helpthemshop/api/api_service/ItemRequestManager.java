package com.mheev.helpthemshop.api.api_service;

import android.databinding.ObservableArrayList;
import android.util.Log;

import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.ApiCreateResponse;
import com.mheev.helpthemshop.model.ApiEditResponse;
import com.mheev.helpthemshop.model.ShoppingItem;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by mheev on 9/19/2016.
 */
public class ItemRequestManager {
    private ShoppingItemClient client;
    private final String TAG = ItemRequestManager.class.getSimpleName();
    public ItemRequestManager(ShoppingItemClient client){
        this.client = client;
    }
    public Observable<List<ShoppingItem>> loadItemList() {
        return client.getItemList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .map(apiResponse -> apiResponse.getResults())
                ;
    }
    public Observable<ApiCreateResponse> createItem(ShoppingItem item){
        return client.createItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
    public Observable<ApiEditResponse> updateItem(ShoppingItem item){
     return client.editItem(item.getId(), item)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .unsubscribeOn(Schedulers.io());
    }

    public void syncItem(ShoppingItem item ){
        if(item.isNew())
            createItem(item);
        else
            updateItem(item);

        item.setSync(true);
    }

}
