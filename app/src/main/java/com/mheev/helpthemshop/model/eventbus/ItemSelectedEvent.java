package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/25/2016.
 */

public class ItemSelectedEvent extends ItemEvent{

    public ItemSelectedEvent(ShoppingItem item) {
        super(item);
    }
}
