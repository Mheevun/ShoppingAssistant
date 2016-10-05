package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.activity.ItemDetailsResultListener;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/23/2016.
 */

public class EditItemEvent extends ItemEvent {
    private ItemDetailsResultListener listener;
    public EditItemEvent(ShoppingItem item, ItemDetailsResultListener listener) {
        super(item);
        this.listener = listener;
    }

    public ItemDetailsResultListener getListener() {
        return listener;
    }
}
