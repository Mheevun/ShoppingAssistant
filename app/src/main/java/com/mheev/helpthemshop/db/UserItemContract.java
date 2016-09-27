package com.mheev.helpthemshop.db;

import android.provider.BaseColumns;

import static com.mheev.helpthemshop.db.UserItemContract.UserItemEntry.TABLE_NAME;

/**
 * Created by mheev on 9/26/2016.
 */

public class UserItemContract {
    private UserItemContract(){}

    /* Inner class that defines the table contents */
    public static class UserItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "shop_item";
        public static final String COLUMN_NAME = "item_name";
        public static final String COLUMN_DESCRIPTION = "item_description";
        public static final String COLUMN_GROUP = "item_group";
        public static final String COLUMN_QUANTITY_UNIT = "quantity_unit";
        public static final String COLUMN_QUANTITY_AMOUNT = "quantity_amount";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_PRICE_VALUE = "price_value";
        public static final String COLUMN_PRICE_CURRENCY = "price_currency";
        public static final String COLUMN_AVATAR_URL = "avatar_url";
        public static final String COLUMN_AVATAR_WALL_URL = "avatar_wall_url";
        public static final String COULUMN_FAVORITE = "favorite";
    }



}
