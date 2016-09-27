package com.mheev.helpthemshop.api.api_service;

import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by mheev on 9/26/2016.
 */

public class UserItemRequestManager extends RequestManager{
    private String userId;
    public UserItemRequestManager(ShoppingItemClient client, String userId){
        super(client);
        this.userId = userId;
    }
    public Observable<List<ShoppingItem>> loadItemList() {
        return getRequester(client.getUserItemList(userId))
                .map(apiResponse -> apiResponse.getResults());
    }
    public Observable<ShoppingItem> getItem(String itemId){
        return getRequester(client.getUserItem(userId, itemId));
    }

    public Observable<ApiCreateResponse> createItem(ShoppingItem item) {
        return getRequester(client.createUserItem(userId, item));
    }

    public Observable<ApiEditResponse> updateItem(ShoppingItem item) {
        return getRequester(client.editUserItem(userId, item.getId(), item));
    }

    public Observable<ResponseBody> removeItem(String itemId){
        return getRequester(client.deleteUserItem(userId, itemId));
    }

}
