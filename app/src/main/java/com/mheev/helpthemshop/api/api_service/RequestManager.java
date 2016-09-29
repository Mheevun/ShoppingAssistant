package com.mheev.helpthemshop.api.api_service;

import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 9/26/2016.
 */

public abstract class RequestManager {
    protected ShoppingItemClient client;
    public RequestManager(ShoppingItemClient client){
        this.client = client;
    }
    public <T> Observable<T> getRequester(Observable<T> observable){
        return observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread());
    }
    public abstract Observable<List<ShoppingItem>> loadItemList();

    public abstract Observable<ShoppingItem> getItem(String id);


    public abstract Observable<ApiCreateResponse> createItem(ShoppingItem item);

    public abstract Observable<ApiEditResponse> updateItem(ShoppingItem item);

    public abstract Observable<ResponseBody> removeItem(String itemId);
}
