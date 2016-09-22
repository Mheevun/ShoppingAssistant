package com.mheev.helpthemshop.binding_adapter;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ObservableArrayList;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.ShoppingItem;

/**
 * Created by mheev on 9/14/2016.
 */
public class CustomAttribute {
    @BindingAdapter("displayItems")
    public static void bindList(RecyclerView view, ObservableArrayList<ShoppingItem> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new ItemAdapter(list));
    }

    @BindingConversion
    public static @ColorRes int convertStringToColor(ColorEnum value){
        switch (value){
            case Primary:
                return R.color.colorPrimary;
            case PrimaryDark:
                return R.color.colorPrimaryDark;
            case White:
                return R.color.colorWhite;
            default:
                return R.color.colorPrimary;
        }
    }



}
