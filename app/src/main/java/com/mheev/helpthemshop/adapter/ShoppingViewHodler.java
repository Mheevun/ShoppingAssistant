package com.mheev.helpthemshop.adapter;

import android.support.v7.widget.RecyclerView;

import com.mheev.helpthemshop.databinding.ShoppingItemBinding;

/**
 * Created by mheev on 9/13/2016.
 */
public class ShoppingViewHodler extends RecyclerView.ViewHolder{
    private ShoppingItemBinding binding;
    public ShoppingViewHodler(ShoppingItemBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public ShoppingItemBinding getBinding(){
        return binding;
    }
}
