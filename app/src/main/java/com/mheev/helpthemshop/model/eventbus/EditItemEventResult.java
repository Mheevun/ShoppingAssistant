package com.mheev.helpthemshop.model.eventbus;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 10/5/2016.
 */

public class EditItemEventResult extends ItemEvent {
    private DetailActivityResultType updateResult;
    public EditItemEventResult(ShoppingItem item, DetailActivityResultType updateResult) {
        super(item);
        this.updateResult = updateResult;
    }

    public DetailActivityResultType getUpdateResultType() {
        return updateResult;
    }

    public void setUpdateResult(DetailActivityResultType updateResult) {
        this.updateResult = updateResult;
    }
}
