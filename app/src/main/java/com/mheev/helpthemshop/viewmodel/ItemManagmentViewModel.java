package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.activity.ItemManagmentListener;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.activity.ActivityNavigator;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import rx.Observable;


/**
 * Created by mheev on 9/14/2016.
 */
public class ItemManagmentViewModel extends BaseObservable {
    private String TAG = "ItemManagmentViewModel";
    public ObservableField<String> searchText = new ObservableField<String>();
    public ObservableBoolean isLoadingItems = new ObservableBoolean(false);
    public ItemAdapter itemAdapter;

    private ItemManagmentListener listener;
    private ShoppingItemRepository itemRepository;

    public ItemManagmentViewModel(ItemManagmentListener listener) {
        this.listener = listener;
        itemAdapter = new ItemAdapter(listener);
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
        Log.d(TAG,"remove item: "+position);
        return itemAdapter.removeItem(position);
    }

    public ShoppingItem moveItemToActivePlan(int position) {
        ShoppingItem targetItem = itemAdapter.get(position);

        //clear searchText
        searchText.set("");
        Log.d(TAG, "clear text");

        return targetItem;
    }

    public void onAddButton(View view) {
        listener.onEditItemDetails(new ShoppingItem());
    }

    public void onTextChanged(final CharSequence c) {
        final String text = c.toString();
        Log.d(TAG, "searchText change: " + text);
        List<ShoppingItem> allItems = itemRepository.getItems();
        itemAdapter.clear();
        if (TextUtils.isEmpty(text)) {
            itemAdapter.setItem(allItems);
        } else {
            // Iterate in the original List and add it to filter list...
            for (ShoppingItem item : allItems) {
                if(item.getItemName()==null) continue;
                if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                    itemAdapter.add(item);
                }
            }
            if (itemAdapter.isEmpty()) {
                Log.d(TAG, "create new item for: " + text);
                itemAdapter.add(new ShoppingItem(text));
            }
        }
    }
}
