package com.mheev.helpthemshop.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.mheev.helpthemshop.model.ItemRepository;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemDetailsViewModel;

import javax.inject.Inject;

/**
 * Created by mheev on 9/15/2016.
 */

public class ItemDetailActivity_deprecate extends AppCompatActivity{
    public static final String ID = "ID";
    @Inject
    ItemRepository itemRepo;

    private ItemDetailsViewModel viewModel;

    public ItemDetailActivity_deprecate() {
//        App.getItemComponent().inject(this);
    }

    public static Intent getStartIntent(Context context, ShoppingItem item) {
        Intent intent = new Intent(context, ItemDetailActivity_deprecate.class);
        intent.putExtra(ID, item.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }





}
