package com.mheev.helpthemshop.activity;

import android.Manifest;
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

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.databinding.ItemDetailsV2Binding;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.ImagePicker;
import com.mheev.helpthemshop.viewmodel.ItemDetailsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

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


    @Subscribe(sticky = true)
    public void onReciveEditEvent(EditItemEvent event) {
//        ShoppingItem item = itemRepo.getItem(getIntent().getStringExtra(ID));
//        if(item==null) {
//            item = new ShoppingItem();
//            item.setNew(true);
//        }
//        Log.d(TAG, "recive intent: " + getIntent().getSerializableExtra(ID));
//        item = (ShoppingItem) getIntent().getSerializableExtra(ID);
        Log.d(TAG, "recieve item: " + event.getItem());
        item = event.getItem();
    }

    @Override
    protected void onDestroy() {

//        itemRepo.addNewItem(viewModel.getItem());
//        Intent returnIntent = getIntent();
//        returnIntent.putExtra(ID, item);
//        setResult(RESULT_OK,returnIntent);
//        finish();
        EventBus.getDefault().post(new EditItemEventResult(item));
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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

    private ImageView imageView;

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showAvatarPickerAfterPermission(final ImageView imgView) {
        this.imageView = imgView;
        ImagePicker.pickImage(this, "Select picture", imgView);
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
            imageView.setImageBitmap(bitmap);
        }
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
