package com.mheev.helpthemshop.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.adapter.ItemAdapter;
import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.UserItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import javax.inject.Inject;
import javax.inject.Named;

import static com.mheev.helpthemshop.di.module.ItemModule.USER_REPOSITORY;

/**
 * Created by mheev on 9/13/2016.
 */
public class BuyingViewModel{
    private static final String TAG = "BuyingViewModel";
    public ItemAdapter itemAdapter;

    @Inject
    @Named(USER_REPOSITORY)
    public BuyingViewModel(UserItemRepository repository){ //todo use repository -wrap> dbHelper
        itemAdapter = new ItemAdapter(repository, R.layout.shopping_item, new ItemViewModel(repository));
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
