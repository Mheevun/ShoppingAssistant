package com.mheev.helpthemshop.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mheev.helpthemshop.databinding.ShoppingItemBinding;
import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemViewModel;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemAdapter extends RecyclerView.Adapter<ShoppingViewHodler>{
    public static final String TAG = "ItemAdapter";
    private int itemLayout;
    private DataRepository repository;
    public ObservableArrayList<ShoppingItem> displayItems;

    public ItemAdapter(DataRepository repository, int itemLayout){
        this.itemLayout = itemLayout;
        this.repository = repository;

        //bind display with repository
        Log.d(TAG, "bind displayItems with repository items");
        displayItems = repository.getItems();
        displayItems.addOnListChangedCallback(callback);
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
        ShoppingItem item = displayItems.get(position);

        Log.d(TAG, "bind item with viewModel; item name: "+item.getItemName());

        binding.setVm(new ItemViewModel(item, repository));
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return displayItems.size();
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

    ObservableList.OnListChangedCallback<ObservableList<Object>> callback = new ObservableList.OnListChangedCallback<ObservableList<Object>>() {
        @Override
        public void onChanged(ObservableList<Object> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<Object> sender, int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<Object> sender, int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<Object> sender, int fromPosition, int toPosition, int itemCount) {
            notifyItemRangeRemoved(fromPosition, itemCount);
            notifyItemRangeInserted(toPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<Object> sender, int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };
}
