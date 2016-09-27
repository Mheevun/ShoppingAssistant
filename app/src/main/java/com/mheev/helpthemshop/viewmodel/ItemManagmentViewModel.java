package com.mheev.helpthemshop.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;


/**
 * Created by mheev on 9/14/2016.
 */
public class ItemManagmentViewModel extends BasedViewModel {
    private String TAG = "ItemManagmentViewModel";
    public ObservableField<String> searchText = new ObservableField<String>();
    public ObservableBoolean isLoadingItems = new ObservableBoolean(false);

    private OnEditItemListener listener;
    public ItemManagmentViewModel(OnEditItemListener listener) {
        super(listener);
        this.listener = listener;
    }

    public ShoppingItem moveItemToActivePlan(int position) {
        ShoppingItem targetItem = itemAdapter.get(position);
        removeItem(position);
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
