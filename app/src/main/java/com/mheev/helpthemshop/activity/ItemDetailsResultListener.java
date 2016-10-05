package com.mheev.helpthemshop.activity;

import com.mheev.helpthemshop.model.eventbus.DetailActivityResultType;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 10/5/2016.
 */

public interface ItemDetailsResultListener {
    public void onReceiveResult(ShoppingItem item, DetailActivityResultType resultType);
}
