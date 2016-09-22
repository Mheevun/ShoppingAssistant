package com.mheev.helpthemshop.api.retrofit;

import com.mheev.helpthemshop.model.ApiCreateResponse;
import com.mheev.helpthemshop.model.ApiEditResponse;
import com.mheev.helpthemshop.model.ApiQueryResponse;
import com.mheev.helpthemshop.model.ShoppingItem;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by mheev on 9/19/2016.
 */
public interface ShoppingItemClient {
    @GET("shop_item")
    Observable<ApiQueryResponse> getItemList();

    @POST("shop_item")
    Observable<ApiCreateResponse> createItem(@Body ShoppingItem item);

    @GET("shop_item/{id}")
    Observable<ShoppingItem> getItem(@Path("id")String itemId);

    @PUT("shop_item/{id}")
    Observable<ApiEditResponse> editItem(@Path("id")String itemId, @Body ShoppingItem item);

    @DELETE("shop_item/{id}")
    Observable<Response> deleteItem(@Path("id")String itemId);


}
