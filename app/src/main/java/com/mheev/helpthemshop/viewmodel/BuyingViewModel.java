package com.mheev.helpthemshop.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.model.ShoppingItem;

/**
 * Created by mheev on 9/13/2016.
 */
public class BuyingViewModel {

    public ObservableArrayList<ShoppingItem> displayItems = new ObservableArrayList<>();

    public void addBuyingItem(ShoppingItem item) {
        displayItems.add(item);
    }

    public void removeBuyingItem(int position) {
        displayItems.remove(position);
    }
}
