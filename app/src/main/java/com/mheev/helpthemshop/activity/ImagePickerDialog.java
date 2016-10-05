package com.mheev.helpthemshop.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mheev.helpthemshop.api.retrofit.ApiUtil;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.util.ImagePicker;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import permissions.dispatcher.NeedsPermission;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 10/4/2016.
 */

public class ImagePickerDialog extends DialogFragment implements ItemDetailsListener {
    private static final String TAG = "ImagePickerDialog";
    @Override
    public void showAvatarPicker(ImageView imgView) {
        ImagePickerDialogPermissionsDispatcher.showAvatarPickerAfterPermissionWithCheck(this, imgView);
    }

    @Override
    public void onDeleteClick() {

    }


    private static ImageView imageView;

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showAvatarPickerAfterPermission(ImageView imgView) {
        this.imageView = imgView;
        ImagePicker.pickImage(getActivity(), "Select picture");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ImagePickerDialogPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(getContext(), requestCode, resultCode, data);
        if (bitmap != null) {
            File imageFile = ImagePicker.createFileFromBitmap(getContext(), bitmap);
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
//                        viewModel.setAvatarUrl(ApiUtil.IMG_ROOT_URI + "/" + apiCreateResponse.getObjectId());
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
