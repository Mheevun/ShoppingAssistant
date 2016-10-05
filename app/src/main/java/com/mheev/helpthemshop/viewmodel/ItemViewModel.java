package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.activity.EditItemHandler;
import com.mheev.helpthemshop.activity.ItemManagementFragment;
import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.pojo.Quantity;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.Navigator;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemViewModel extends BaseObservable{
    private ShoppingItem item;

    @Inject
    Navigator navigator;

    public ObservableField<String> avatarUrl = new ObservableField<>();

    private String SPACE = " ";
    private String LINE = "\n";

    private DataRepository dataRepository;

    public ItemViewModel(ShoppingItem item, DataRepository dataRepository){
        ItemManagementFragment.getNetNavigatorComponent().inject(this);
        this.item = item;
        this.dataRepository = dataRepository;
        avatarUrl.set(item.getItemAvatarURL());
    }

    public void setItem(ShoppingItem item){
        this.item = item;
    }

    public String getItemSummary(){
        String summary = item.getItemName();
        Quantity quantity = item.getQuantity();
        if(quantity!=null){
            summary = summary + LINE +item.getQuantity().getAmount() + SPACE +  item.getQuantity().getUnit();
        }
        return summary;
    }

    public boolean onItemClick(View view){
        EditItemHandler handler = new EditItemHandler(dataRepository, navigator);
        Log.d(TAG, "item name click:"+item.getItemName());
        handler.handlerItemUpdate(view, item);

        return true;
    }

}
