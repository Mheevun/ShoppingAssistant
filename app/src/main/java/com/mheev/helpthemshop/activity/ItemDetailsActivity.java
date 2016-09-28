package com.mheev.helpthemshop.activity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.api.retrofit.ApiUtil;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.databinding.ItemDetailsV2Binding;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.ImagePicker;
import com.mheev.helpthemshop.viewmodel.ItemDetailsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 9/18/2016.
 */
@RuntimePermissions
public class ItemDetailsActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, ItemDetailsDialogView {
    private static final String TAG = ItemDetailsActivity.class.getSimpleName();
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

//    @Inject
//    ShoppingItemRepository itemRepo;

    public static final String ID = "ID";
    private ItemDetailsV2Binding binding;
    private ItemDetailsViewModel viewModel;
    private ShoppingItem item;

    @Inject
    ShoppingItemClient client;

    @Inject
    Retrofit retrofit;

    public ItemDetailsActivity() {

    }

    public static Intent getStartIntent(Context context, ShoppingItem item) {
        Intent intent = new Intent(context, ItemDetailsActivity.class);
//        intent.putExtra(ID, item.getId());
//        intent.putExtra(ID, item);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        App.getNetComponent().inject(this);
//        ItemManagmentFragment.getNetNavigatorComponent().inject(this);

        Log.d(TAG, "onCreate()");
        ItemManagmentFragment.getNetNavigatorComponent().inject(this);

        int layout = R.layout.item_details_v2;
        setContentView(layout);
        binding = DataBindingUtil.setContentView(this, layout);

        bindActivity();
//        initViewModel();
    }

    private void bindActivity() {
        mToolbar = binding.toolbar;
        mTitle = binding.textviewTitle;
        mTitleContainer = binding.linearlayoutTitle;
        mAppBarLayout = binding.appbar;

        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);


        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        Log.d(TAG, "onCreate item:"+item);
        viewModel = new ItemDetailsViewModel(this, item);
        binding.setViewModel(viewModel);
    }

    private EditItemEvent event;
    @Subscribe(sticky = true)
    public void onReciveEditEvent(EditItemEvent event) {
        this.event = event;
        Log.d(TAG, "recieve item: " + event.getItem());
        item = event.getItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        event.onEditItemCallback(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
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
        File imageFile = ImagePicker.createFileFromBitmap(this,bitmap);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            uploadImage(imageFile);
        }
    }


    private void uploadImage(File imageFile) {
        Log.d(TAG,"test upload image");

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
                Log.d(TAG, "create image id:"+apiCreateResponse.getObjectId());
                viewModel.setAvatarUrl(ApiUtil.IMG_ROOT_URI+"/"+apiCreateResponse.getObjectId());
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
//        client.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header("X-Parse-Application-Id", "myAppId");
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });

        client.addInterceptor(logging);

        return client.build();
    }



    //    @Override
//    public boolean onMenuItemClick(MenuItem menu) {
//        switch (menu.getItemId()){
//            case R.id.menu_favorite:
//                Drawable icon= menu.getIcon();
//                menu.setIcon(R.drawable.heart);
//                ColorFilter filter = new LightingColorFilter( Color.RED, Color.RED);
//                icon.setColorFilter(filter);
//                Drawable wrappedDrawable = DrawableCompat.wrap(icon);
//                DrawableCompat.setTint(wrappedDrawable, Color.RED);
//
//        }
//        return super.onOptionsItemSelected(menu);
//    }
}
