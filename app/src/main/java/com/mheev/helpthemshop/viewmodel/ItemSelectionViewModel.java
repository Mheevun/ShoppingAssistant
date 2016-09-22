package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.repository.ShoppingItemRepository;
import com.mheev.helpthemshop.activity.ActivityNavigator;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.model.ShoppingItem;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by mheev on 9/14/2016.
 */
public class ItemSelectionViewModel extends BaseObservable {
    private String TAG = "ItemSelectionViewModel";


    public ObservableField<String> searchText = new ObservableField<String>();

    private ShoppingItem newItem;


//    public ItemSelectionViewModel(ObservableArrayList<ShoppingItem> items) {
//        this.displayItems.addAll(items);
//        allItems = items;
//    }

    private ActivityNavigator navigator;
    public ObservableBoolean isLoadingItems = new ObservableBoolean(false);

    public ObservableArrayList<ShoppingItem> displayItems = new ObservableArrayList<ShoppingItem>();
    public ObservableArrayList<ShoppingItem> allItems = new ObservableArrayList<ShoppingItem>();

    public void initData(ShoppingItemRepository itemRepository, ActivityNavigator navigator) {
        this.navigator = navigator;
        allItems = itemRepository.getItems();
        displayItems.clear();
        displayItems.addAll(allItems);
    }

    public ShoppingItem moveItemToActivePlan(int position) {
        ShoppingItem targetItem = displayItems.get(position);

        //add new item in the allitem list
        if (isNewItem((targetItem))) {
            allItems.add(targetItem);
        }

        //mark item (might change to color mark or star)
        displayItems.remove(targetItem);

        //clear searchText
        searchText.set("");
        Log.d(TAG, "clear text");

        return targetItem;
    }

    private boolean isNewItem(ShoppingItem item) {
        return newItem == null ? false : newItem.equals(item);
    }

    public void onAddButton(View view) {
        navigator.toItemDetailsActivity(new ShoppingItem(), view);
    }

    public void onTextChanged(final CharSequence c) {
        final String text = c.toString();
        Log.d(TAG, "searchText change: " + text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                displayItems.clear();

                if (TextUtils.isEmpty(text)) {
                    displayItems.addAll(allItems);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (ShoppingItem item : allItems) {
                        if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            displayItems.add(item);
                        }
                    }
                    if (displayItems.isEmpty()) {
                        Log.d(TAG, "create new item for: " + text);
                        newItem = new ShoppingItem(text);
                        displayItems.add(newItem);
                    }
                }
            }
        }).start();
    }

}
