package com.mheev.helpthemshop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mheev.helpthemshop.model.pojo.Price;
import com.mheev.helpthemshop.model.pojo.Quantity;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

import static com.mheev.helpthemshop.db.UserItemContract.UserItemEntry;


/**
 * Created by mheev on 9/26/2016.
 */

public class UserItemDbHelper extends SQLiteOpenHelper{
    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserItemEntry.TABLE_NAME + " (" +
                    UserItemEntry._ID + " INTEGER PRIMARY KEY," +
                    UserItemEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_GROUP + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_QUANTITY_AMOUNT + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_QUANTITY_UNIT + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_NOTE + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_PRICE_VALUE + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_PRICE_CURRENCY + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_AVATAR_URL + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COLUMN_AVATAR_WALL_URL + TEXT_TYPE + COMMA_SEP +
                    UserItemEntry.COULUMN_FAVORITE + BOOLEAN_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserItemContract.UserItemEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "UserShopItem.db";

    public UserItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(SQL_DELETE_ENTRIES);

        // Create tables again
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addItem(ShoppingItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = toContentValue(item);

        // Inserting Row
        db.insert(UserItemEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public ShoppingItem getItem(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        // Filter results WHERE "title" = 'My Title'
        String selection = UserItemEntry._ID + " = ?";
        String[] selectionArgs = {id};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                UserItemEntry.COLUMN_NAME + " DESC";

        Cursor cursor = db.query(
                UserItemEntry.TABLE_NAME,                     // The table to query
                getProjection(),                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if (cursor != null)
            cursor.moveToFirst();

        return toShoppingItem(cursor);
    }
    public List<ShoppingItem> getItems(){
        List<ShoppingItem> shoppingItemList = new ArrayList<ShoppingItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + UserItemEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShoppingItem item = toShoppingItem(cursor);
                shoppingItemList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return shoppingItemList;
    }
    public int getItemsCount(){
        String countQuery = "SELECT  * FROM " + UserItemEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateItem(ShoppingItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = toContentValue(item);

        // updating row
        return db.update(UserItemEntry.TABLE_NAME, values, UserItemEntry._ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }
    public void removeItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserItemEntry.TABLE_NAME, UserItemEntry._ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    private ContentValues toContentValue(ShoppingItem item){
        ContentValues values = new ContentValues();
        values.put(UserItemEntry.COLUMN_NAME, item.getItemName());
        values.put(UserItemEntry.COLUMN_DESCRIPTION, item.getItemDescription());
        values.put(UserItemEntry.COLUMN_GROUP, item.getGroup());
        if(item.getQuantity()!=null){
            values.put(UserItemEntry.COLUMN_QUANTITY_UNIT, item.getQuantity().getUnit());
            values.put(UserItemEntry.COLUMN_QUANTITY_AMOUNT, String.valueOf(item.getQuantity().getAmount()));
        }
        values.put(UserItemEntry.COLUMN_NOTE, item.getNote());
        if(item.getPrice()!=null){
            values.put(UserItemEntry.COLUMN_PRICE_VALUE, item.getPrice().getValue());
            values.put(UserItemEntry.COLUMN_PRICE_CURRENCY, item.getPrice().getCurrency());
        }
        values.put(UserItemEntry.COLUMN_AVATAR_URL, item.getItemAvatarURL());
        values.put(UserItemEntry.COLUMN_AVATAR_WALL_URL, item.getItemWallpaperURL());
        values.put(UserItemEntry.COULUMN_FAVORITE, item.isFavorite());
        return values;
    }

    private ShoppingItem toShoppingItem(Cursor cursor){
        ShoppingItem item = new ShoppingItem();
        item.setId(cursor.getString(0).toString());
        item.setItemName(cursor.getString(1));
        item.setItemDescription(cursor.getString(2));
        item.setGroup(cursor.getString(3));
        Quantity quantiy = new Quantity(cursor.getString(4), cursor.getString(5));
        item.setQuantity(quantiy);
        item.setNote(cursor.getString(6));
        Price price = new Price(cursor.getString(7), cursor.getString(8));
        item.setPrice(price);
        item.setItemAvatarURL(cursor.getString(9));
        item.setItemWallpaperURL(cursor.getString(10));
        item.setFavorite(cursor.getInt(10)==1);

        return item;
    }

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
    private String[] getProjection(){
        String[] projection = {
                UserItemEntry._ID,
                UserItemEntry.COLUMN_NAME,
                UserItemEntry.COLUMN_DESCRIPTION,
                UserItemEntry.COLUMN_GROUP,
                UserItemEntry.COLUMN_QUANTITY_UNIT,
                UserItemEntry.COLUMN_QUANTITY_AMOUNT,
                UserItemEntry.COLUMN_NOTE,
                UserItemEntry.COLUMN_PRICE_VALUE,
                UserItemEntry.COLUMN_PRICE_CURRENCY,
                UserItemEntry.COLUMN_AVATAR_URL,
                UserItemEntry.COLUMN_AVATAR_WALL_URL,
                UserItemEntry.COULUMN_FAVORITE,
        };
        return projection;
    }
}
