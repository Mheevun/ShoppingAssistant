package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */

public class EditItemEvent extends ItemEvent {
    private OnEditItemListener listener;

    public EditItemEvent(ShoppingItem item, OnEditItemListener listener) {
        super(item);
        this.listener = listener;
    }

    public void onEditItemCallback(ShoppingItem item){
        listener.onEditItemDetailsResult(item);
    }

}
