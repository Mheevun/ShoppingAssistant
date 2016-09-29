package com.mheev.helpthemshop.activity;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import permissions.dispatcher.NeedsPermission;

/**
 * Created by mheev on 9/17/2016.
 */
public interface ItemDetailsListener {
    //    public void showQuantityValuePicker(int amount, String unit);
//    public void showQuantityUnitPicker(String unit);

    public void showAvatarPicker(ImageView imgView);
    public void onDeleteClick();
}
