package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */

public class EditItemEventResult extends ItemEvent{

    public EditItemEventResult(ShoppingItem item) {
        super(item);
    }
}
