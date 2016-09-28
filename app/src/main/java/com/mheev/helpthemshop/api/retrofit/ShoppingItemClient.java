package com.mheev.helpthemshop.api.retrofit;

import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.api.ApiQueryResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

import static android.os.FileObserver.DELETE;

/**
 * Created by mheev on 9/19/2016.
 */
public interface ShoppingItemClient {
    @GET(ApiUtil.APP_PATH)
    Observable<ApiQueryResponse> getItemList();

    @POST(ApiUtil.APP_PATH)
    Observable<ApiCreateResponse> createItem(@Body ShoppingItem item);

    @GET(ApiUtil.APP_PATH+"/{id}")
    Observable<ShoppingItem> getItem(@Path("id")String itemId);

    @PUT(ApiUtil.APP_PATH+"/{id}")
    Observable<ApiEditResponse> editItem(@Path("id")String itemId, @Body ShoppingItem item);

    @DELETE(ApiUtil.APP_PATH+"/{id}")
    Observable<ResponseBody> deleteItem(@Path("id")String itemId);

    /***********user**************/
    @GET(ApiUtil.APP_PATH+"/user/{uid}/item")
    Observable<ApiQueryResponse> getUserItemList(@Path("uid")String userId);

    @POST(ApiUtil.APP_PATH+"/user/{uid}/item")
    Observable<ApiCreateResponse> createUserItem(@Path("uid") String userId, @Body ShoppingItem item);

    @GET(ApiUtil.APP_PATH+"/user/{uid}/item/{iid}")
    Observable<ShoppingItem> getUserItem(@Path("id")String userId, @Path("iid") String itemId);

    @PUT(ApiUtil.APP_PATH+"/user/{uid}/item/{iid}")
    Observable<ApiEditResponse> editUserItem(@Path("id")String userId, @Path("iid") String itemId, @Body ShoppingItem item);

    @DELETE(ApiUtil.APP_PATH+"/user/{uid}/item/{iid}")
    Observable<ResponseBody> deleteUserItem(@Path("id")String userId, @Path("iid") String itemId);


    /**********image********/

    @POST("/")
    @Multipart
    Observable<ApiCreateResponse> createImage(@Part MultipartBody.Part file);

    @DELETE("/{id}")
    Observable<ResponseBody> deleteImage(@Path("id")String imageId);
}
