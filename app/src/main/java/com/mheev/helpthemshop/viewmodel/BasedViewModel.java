package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.util.Log;

import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/25/2016.
 */

public class BasedViewModel extends BaseObservable {
    private String TAG = "ItemManagmentViewModel";
    public ItemAdapter itemAdapter;

    protected ShoppingItemRepository itemRepository;


    private OnEditItemListener listener;

    public BasedViewModel(OnEditItemListener listener) {
        this.listener = listener;
        itemAdapter = new ItemAdapter(listener);
    }

    public OnEditItemListener getListener() {
        return listener;
    }

    public void initData(ShoppingItemRepository itemRepository) {
        Log.d(TAG, "bind repository and display items");
        this.itemRepository = itemRepository;
        itemAdapter.setItem(itemRepository.getItems());
    }

    public void updateItem(ShoppingItem item) {
        itemRepository.addItem(item);
        itemAdapter.setItem(itemRepository.getItems());
    }
    public String removeItem(int position){
        return itemAdapter.removeItem(position);
    }
    public int getPosition(ShoppingItem item){
        return itemAdapter.getPosition(item);
    }

}
