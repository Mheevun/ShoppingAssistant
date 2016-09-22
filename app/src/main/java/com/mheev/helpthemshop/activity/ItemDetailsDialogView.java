package com.mheev.helpthemshop.activity;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;

import permissions.dispatcher.NeedsPermission;

/**
 * Created by mheev on 9/17/2016.
 */
public interface ItemDetailsDialogView {
    //    public void showQuantityValuePicker(int amount, String unit);
//    public void showQuantityUnitPicker(String unit);

    public void showAvatarPicker(ImageView imgView);
}
