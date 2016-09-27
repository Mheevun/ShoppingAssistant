package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/25/2016.
 */

public class ItemEvent {
    private ShoppingItem item;

    public ItemEvent(ShoppingItem item) {
        this.item = item;
    }



    public ShoppingItem getItem() {
        return item;
    }

    public void setItem(ShoppingItem item) {
        this.item = item;
    }


}
