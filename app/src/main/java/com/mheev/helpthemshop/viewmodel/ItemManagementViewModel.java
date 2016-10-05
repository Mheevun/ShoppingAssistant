package com.mheev.helpthemshop.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.EditItemHandler;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.ItemRepository;
import com.mheev.helpthemshop.model.UserItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.Navigator;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by mheev on 9/14/2016.
 */
public class ItemManagementViewModel {
    private String TAG = "ItemManagementViewModel";
    public ObservableField<String> searchText = new ObservableField<String>();
    public ObservableBoolean isLoadingItems;

    private ItemAdapter itemAdapter;
    public Navigator navigator;

    private UserItemRepository userItemRepository;
    private ItemRepository itemRepository;

    @Inject
    public ItemManagementViewModel(ItemRepository itemRepository, Navigator navigator, UserItemRepository userItemRepository) {
        this.navigator = navigator;
        this.userItemRepository = userItemRepository;
        this.itemRepository = itemRepository;
        itemAdapter = new ItemAdapter(itemRepository, R.layout.shopping_item);
        isLoadingItems = itemRepository.getIsLoading();
    }

    //bind to view
    public ItemAdapter getItemDisplayAdapter(){
        return itemAdapter;
    }

    public void moveItemToBuying(int position) {
        ShoppingItem targetItem = itemAdapter.getDisplayItems().get(position);
        Log.d(TAG, "move item to buying: "+targetItem.getItemName());
        userItemRepository.updateItem(targetItem);
    }

    public void onAddButton(View view) {
        EditItemHandler handler = new EditItemHandler(itemAdapter.getRepository(), navigator);
        handler.handlerItemUpdate(view, new ShoppingItem());
    }

    public void onTextChanged(final CharSequence c) {
        final String text = c.toString();
        Log.d(TAG, "searchText change: " + text);

        List displayItems  =  itemAdapter.getDisplayItems();
        List<ShoppingItem> repoItems = itemAdapter.getRepository().getItems();

        displayItems.clear();
        if (TextUtils.isEmpty(text)) {
            displayItems.addAll(repoItems);
        } else {
            // Iterate in the original List and add it to filter list...
            for (ShoppingItem item : repoItems) {
                if(item.getItemName()==null) continue;
                if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                    displayItems.add(item);
                }
            }
            if (displayItems.isEmpty()) {
                Log.d(TAG, "create new item for: " + text);
                displayItems.add(new ShoppingItem(text));
            }
        }
    }

    public void loadItems() {
       itemRepository.loadItems();
    }
}
