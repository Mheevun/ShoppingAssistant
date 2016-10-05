package com.mheev.helpthemshop.viewmodel;

import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.model.UserItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import javax.inject.Inject;

/**
 * Created by mheev on 9/13/2016.
 */
public class BuyingViewModel{
    private static final String TAG = "BuyingViewModel";
    public ItemAdapter itemAdapter;

    @Inject
    public BuyingViewModel(UserItemRepository repository){
        itemAdapter = new ItemAdapter(repository, R.layout.shopping_item);
    }

    //bind to view
    public ItemAdapter getItemDisplayAdapter(){
        return itemAdapter;
    }

    public void updateItem(ShoppingItem item) {
        itemAdapter.getRepository().updateItem(item);
    }

    public void removeItem(int position){
        itemAdapter.getRepository().removeItem(position);
    }

    public void onDoneButton(View v){
        itemAdapter.getRepository().deleteAll();
    }

}
