package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.mheev.helpthemshop.BR;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.EditItemHandler;
import com.mheev.helpthemshop.activity.ItemManagmentFragment;
import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.model.pojo.Quantity;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.Navigator;

import javax.inject.Inject;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemViewModel extends BaseObservable{
    private ShoppingItem item;

    @Inject
    Navigator navigator;


    private String SPACE = " ";
    private String LINE = "\n";

    private DataRepository dataRepository;

    public ItemViewModel(DataRepository dataRepository){
        ItemManagmentFragment.getNetNavigatorComponent().inject(this);
        this.dataRepository = dataRepository;
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

    @Bindable
    public String getAvatarUrl(){
        return item.getItemAvatarURL();
    }
    public void setAvatarUrl(String url){
        item.setItemAvatarURL(url);
        notifyPropertyChanged(BR.avatarUrl);
    }

    public void onItemClick(View view){
        EditItemHandler handler = new EditItemHandler(dataRepository, navigator);
        handler.handlerItemUpdate(view, new ShoppingItem());
    }

}
