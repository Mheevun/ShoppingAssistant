package com.mheev.helpthemshop.binding_adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ObservableArrayList;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/14/2016.
 */
public class CustomAttribute {
    @BindingAdapter("adapter")
    public static void bindList(RecyclerView view, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(layoutManager);
        view.setAdapter(adapter);
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
