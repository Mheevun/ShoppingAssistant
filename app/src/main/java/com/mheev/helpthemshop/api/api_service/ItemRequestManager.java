package com.mheev.helpthemshop.api.api_service;

import android.util.Log;

import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by mheev on 9/19/2016.
 */
public class ItemRequestManager {
    private ShoppingItemClient client;
    private final String TAG = ItemRequestManager.class.getSimpleName();

    public ItemRequestManager(ShoppingItemClient client) {
        this.client = client;
    }

    public Observable<ShoppingItem> getItem(String id){
        return client.getItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<List<ShoppingItem>> loadItemList() {
        return client.getItemList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .map(apiResponse -> apiResponse.getResults())
                ;
    }

    public Observable<ApiCreateResponse> createItem(ShoppingItem item) {
        return client.createItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<ApiEditResponse> updateItem(ShoppingItem item) {
        return client.editItem(item.getId(), item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
    public Observable<ResponseBody> removeItem(String id){
        return client.deleteItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public void syncItem(ShoppingItem item) {
        Log.d(TAG, "syncing item");
        if (item.getId() == null) {
            Log.d(TAG, "perform create item request" + item.getItemName());
            createItem(item).subscribe(new CreateItemHandler());
        } else {
            Log.d(TAG, "perform update item request: " + item.getItemName());
            updateItem(item).subscribe(new UpdateItemHandler());
        }

    }

    public class CreateItemHandler implements Observer<ApiCreateResponse> {
        @Override
        public void onCompleted() {
            Log.d(TAG, "create item complete");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "error on create item");
        }

        @Override
        public void onNext(ApiCreateResponse apiCreateResponse) {
            Log.d(TAG, "create item response details: " + apiCreateResponse.getObjectId());
        }
    }

    public class UpdateItemHandler implements Observer<ApiEditResponse> {
        @Override
        public void onCompleted() {
            Log.d(TAG, "update item complete");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "error on update item");
        }

        @Override
        public void onNext(ApiEditResponse apiCreateResponse) {
            Log.d(TAG, "update item response details: " + apiCreateResponse.getUpdatedAt());
        }
    }

}
