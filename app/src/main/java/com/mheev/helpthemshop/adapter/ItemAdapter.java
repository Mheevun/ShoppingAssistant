package com.mheev.helpthemshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.databinding.ShoppingItemBinding;
import com.mheev.helpthemshop.model.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemViewModel;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ShoppingViewHodler>{
    public ObservableArrayList<ShoppingItem> items = new ObservableArrayList<>();

    public ItemAdapter(ObservableArrayList<ShoppingItem> items){
        this.items = items;
    }

    @Override
    public ShoppingViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        ShoppingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.shopping_item,
                parent,
                false);


        return new ShoppingViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHodler holder, int position) {
        ShoppingItemBinding binding = holder.getBinding();
        ShoppingItem item = items.get(position);

        binding.setVm(new ItemViewModel(item));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if(items==null)
            return 0;
        else
            return items.size();
    }


//    public void removeItem(int position){
//        items.remove(position); //int position = list.indexOf(data)
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,items.size());
//    }
//
//    public void addItem(ShoppingItem item){
//        items.add(item);
//        notifyItemInserted(items.size());
//    }

}
