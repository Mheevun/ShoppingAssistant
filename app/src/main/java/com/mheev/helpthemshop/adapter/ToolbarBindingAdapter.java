package com.mheev.helpthemshop.adapter;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v7.widget.Toolbar;

@BindingMethods({
        @BindingMethod(type = Toolbar.class, attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener"),
        @BindingMethod(type = Toolbar.class, attribute = "android:onNavigationClick", method = "setNavigationOnClickListener"),
})
public class ToolbarBindingAdapter {
}
