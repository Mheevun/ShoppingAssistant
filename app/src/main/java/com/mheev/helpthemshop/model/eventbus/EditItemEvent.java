package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */

public class EditItemEvent extends ItemEvent {
    public EditItemEvent(ShoppingItem item) {
        super(item);
    }
}
