package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */

public class EditItemEventResult {
    private ShoppingItem item;

    public EditItemEventResult(ShoppingItem item) {
        this.item = item;
    }

    public ShoppingItem getItem() {
        return item;
    }

    public void setItem(ShoppingItem item) {
        this.item = item;
    }
}
