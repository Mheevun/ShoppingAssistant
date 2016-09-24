package com.mheev.helpthemshop.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.ItemManagmentListener;
import com.mheev.helpthemshop.databinding.ShoppingItemBinding;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemViewModel;

import java.util.List;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ShoppingViewHodler>{
    public ObservableArrayList<ShoppingItem> items = new ObservableArrayList<ShoppingItem>();
    private ItemManagmentListener listener;

    public ItemAdapter(ItemManagmentListener listener){
        this.listener = listener;
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

        binding.setVm(new ItemViewModel(item, listener));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if(items==null)
            return 0;
        else
            return items.size();
    }


    public String removeItem(int position){
        ShoppingItem item = items.remove(position); //int position = list.indexOf(data)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,items.size());
        return item.getId();
    }

    public void setItem(List<ShoppingItem> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
   }
    public ShoppingItem get(int position){
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(ShoppingItem item) {
        items.add(item);
        notifyItemInserted(items.size());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
