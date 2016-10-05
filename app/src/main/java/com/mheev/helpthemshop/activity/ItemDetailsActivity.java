package com.mheev.helpthemshop.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.api.retrofit.ApiUtil;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.databinding.ItemDetailsBinding;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.eventbus.DetailActivityResultType;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.ImagePicker;
import com.mheev.helpthemshop.util.Navigator;
import com.mheev.helpthemshop.viewmodel.ItemDetailsViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 9/18/2016.
 */
@RuntimePermissions
public class ItemDetailsActivity extends AppCompatActivity
        implements ItemDetailsListener {
    private static final String TAG = ItemDetailsActivity.class.getSimpleName();
    private ItemDetailsBinding binding;
    private ItemDetailsViewModel viewModel;
    private Subscription subscription;
    private ItemDetailsResultListener listener;
    private Pair<ShoppingItem, DetailActivityResultType> updateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        int layout = R.layout.item_details;
        setContentView(layout);
        binding = DataBindingUtil.setContentView(this, layout);

        subscription = Navigator.editItemBus
                .observeEvents(EditItemEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<EditItemEvent>() {
                    @Override
                    public void call(EditItemEvent editItemEvent) {
                        bindViewModel(editItemEvent.getItem(), editItemEvent.getListener());
                        subscription.unsubscribe();
                    }
                });
    }

    private void bindViewModel(ShoppingItem item, ItemDetailsResultListener listener){
        this.listener = listener;

        viewModel = new ItemDetailsViewModel(this, item);
        binding.setViewModel(viewModel);

        //set default update result
        updateResult = new Pair<ShoppingItem, DetailActivityResultType>(viewModel.getItem(), DetailActivityResultType.UPDATE);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "on destroy; return item: " + updateResult.first +", resultType: "+updateResult.second);
        listener.onReceiveResult(updateResult.first, updateResult.second);
        super.onDestroy();

//        if(event.getDeleteFlag())
//            event.onDeleteCallback(viewModel.getItem());
//        else
//            event.onEditItemCallback(viewModel.getItem());
    }

    public void onDeleteClick(){
        updateResult = new Pair<ShoppingItem, DetailActivityResultType>(viewModel.getItem(), DetailActivityResultType.DELETE);

//        event.setDeleteFlag(true);
//        finishAfterTransition();

    }

    /********
     * image picker
     *******/
    @Override
    public void showAvatarPicker(ImageView imgView) {
        ItemDetailsActivityPermissionsDispatcher.showAvatarPickerAfterPermissionWithCheck(this, imgView);
    }


    private static ImageView imageView;

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showAvatarPickerAfterPermission(ImageView imgView) {
        this.imageView = imgView;
        ImagePicker.pickImage(this, "Select picture");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ItemDetailsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (bitmap != null) {
            File imageFile = ImagePicker.createFileFromBitmap(this, bitmap);
            imageView.setImageBitmap(bitmap);
            uploadImage(imageFile);
        }
    }


    private void uploadImage(File imageFile) {
        Log.d(TAG, "test upload image");

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", imageFile.getName(), requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(ApiUtil.IMG_ROOT_URI)
                .client(provideOkhttpClient())
                .build();

        retrofit.create(ShoppingItemClient.class).createImage(body)
//        client.createImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ApiCreateResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "upload image complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error for uploading the image");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ApiCreateResponse apiCreateResponse) {
                        Log.d(TAG, "create image id:" + apiCreateResponse.getObjectId());
                        viewModel.setAvatarUrl(ApiUtil.IMG_ROOT_URI + "/" + apiCreateResponse.getObjectId());
                    }
                });
    }

    private Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return gsonBuilder.create();
    }

    private OkHttpClient provideOkhttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.addInterceptor(logging);

        return client.build();
    }

}
