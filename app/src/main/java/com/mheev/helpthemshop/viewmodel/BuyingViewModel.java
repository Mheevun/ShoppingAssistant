package com.mheev.helpthemshop.viewmodel;

import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/13/2016.
 */
public class BuyingViewModel{
    private UserItemDbHelper dbHelper;
    private static final String TAG = "BuyingViewModel";
    public ItemAdapter itemAdapter;


    public BuyingViewModel(OnEditItemListener listener, UserItemDbHelper dbHelper){
        this.dbHelper = dbHelper;

        itemAdapter = new ItemAdapter(listener);
        initData();

    }

    public void initData() {
        Log.d(TAG, "bind repository and display items");
        itemAdapter.setItem(dbHelper.getItems());
    }

    public void addItem(ShoppingItem item) {
        dbHelper.addItem(item);
        itemAdapter.setItem(dbHelper.getItems());
    }
    public void updateItem(ShoppingItem item){
        dbHelper.updateItem(item);
        itemAdapter.setItem(dbHelper.getItems());
    }
    public void removeItem(int position){
        String id = itemAdapter.removeItem(position);
        dbHelper.removeItem(id);
    }

    public void onDoneButton(View v){

    }
}
