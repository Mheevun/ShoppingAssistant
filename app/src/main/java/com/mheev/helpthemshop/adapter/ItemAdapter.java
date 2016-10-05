package com.mheev.helpthemshop.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.databinding.ShoppingItemBinding;
import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemViewModel;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ShoppingViewHodler>{
    private int itemLayout;
    private DataRepository repository;
    public ObservableList<ShoppingItem> displayItems = new ObservableArrayList<ShoppingItem>();
    private ItemViewModel itemViewModel;

    public ItemAdapter(DataRepository repository, int itemLayout, ItemViewModel itemViewModel){
        this.itemLayout = itemLayout;
        this.repository = repository;
        this.itemViewModel = itemViewModel;

        //bind display with repository
        displayItems = repository.getItems();
    }


    @Override
    public ShoppingViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        ShoppingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                itemLayout,
                parent,
                false);


        return new ShoppingViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHodler holder, int position) {
        ShoppingItemBinding binding = holder.getBinding();
        ShoppingItem item = repository.getItems().get(position);
        itemViewModel.setItem(item);

        binding.setVm(itemViewModel);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return repository.getItems().size();
    }

    public ObservableList<ShoppingItem> getDisplayItems() {
        return displayItems;
    }
    public DataRepository getRepository() {
        return repository;
    }

    //    public String removeItem(int position){
//        ShoppingItem item = items.remove(position); //int position = list.indexOf(data)
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,items.size());
//        return item.getId();
//    }

//    public int getPosition(ShoppingItem item){
//        return repository.getItems().indexOf(item);
//    }



//
//    public void setItem(List<ShoppingItem> items){
//        this.items.clear();
//        this.items.addAll(items);
//        notifyDataSetChanged();
//   }
//    public ShoppingItem get(int position){
//        return items.get(position);
//    }
//
//    public void clear() {
//        items = new ObservableArrayList<>();
//        notifyDataSetChanged();
//    }

//
//    public void add(ShoppingItem item) {
//        items.add(item);
//        notifyItemInserted(items.size());
//    }
//
//    public boolean isEmpty() {
//        return items.isEmpty();
//    }
}
