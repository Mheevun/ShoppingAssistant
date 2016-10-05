package com.mheev.helpthemshop.util;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by mheev on 10/5/2016.
 */

public class ShoppingListUtils {
    private List<ShoppingItem>  list;
    public ShoppingListUtils(List<ShoppingItem> list){
        this.list = list;
    }

    public void updateItemInMemory(ShoppingItem updatedItem) {
        int index = getIndexById(updatedItem);
        list.set(index, updatedItem);
    }

    public int getIndexById(ShoppingItem item) {
        for (ShoppingItem repoItem : list) {
            if (item.getId().equals(repoItem.getId())) {
                int index = list.indexOf(repoItem);
                return index;
            }
        }
        return -1;
    }
}
