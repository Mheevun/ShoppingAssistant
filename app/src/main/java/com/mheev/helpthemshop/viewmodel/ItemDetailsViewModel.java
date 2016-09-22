package com.mheev.helpthemshop.viewmodel;

import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alertdialogpro.AlertDialogPro;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.mheev.helpthemshop.BR;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.ItemDetailsDialogView;
import com.mheev.helpthemshop.model.Price;
import com.mheev.helpthemshop.model.Quantity;
import com.mheev.helpthemshop.model.SavedCurrency;
import com.mheev.helpthemshop.model.ShoppingItem;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * Created by mheev on 9/15/2016.
 */
public class ItemDetailsViewModel extends BaseObservable {
    private ShoppingItem item;
    private String TAG = ItemDetailsViewModel.class.getSimpleName();

    private AppCompatActivity context;
    private ItemDetailsDialogView activity;

    public ItemDetailsViewModel(ItemDetailsDialogView activity, ShoppingItem item) {
        this.item = item;
        this.context = (AppCompatActivity) activity;
        this.activity = activity;
    }

    public ShoppingItem getItem() {
        return item;
    }

    @Bindable
    public String getItemName() {
        return item.getItemName();
    }
    public void setItemName(String name){
        item.setItemName(name);
        notifyPropertyChanged(BR.itemName);
    }

    @Bindable
    public String getGroup() {
        return item.getGroup();
    }
    public void setGroup(String group){
        item.setGroup(group);
        notifyPropertyChanged(BR.group);
    }

    @Bindable
    public int getAmount() {
        if (item.getQuantity() == null) return 1;
        return item.getQuantity().getAmount();
    }

    public void setAmount(int amount) {
        item.setQuantity(new Quantity(amount, getUnit()));
        notifyPropertyChanged(BR.amount);
    }

    @Bindable
    public String getUnit() {
        if (item.getQuantity() == null) return "Units";
        return item.getQuantity().getUnit();
    }

    public void setUnit(String unit) {
        item.setQuantity(new Quantity(getAmount(), unit));
        notifyPropertyChanged(BR.unit);
    }

    @Bindable
    public int getPriceValue() {
        return item.getPrice() != null ? item.getPrice().getValue() : 0;
    }

    public void setPriceValue(int priceValue) {
        if (item.getPrice() == null) item.setPrice(new Price());
        item.getPrice().setValue(priceValue);
        notifyPropertyChanged(BR.priceValue);
    }

    @Bindable
    public String getPriceCurrency() {
        return item.getPrice() != null ? item.getPrice().getCurrency() : "฿";
    }

    public void setPriceCurrency(String unit) {
        if (item.getPrice() == null) item.setPrice(new Price());
        item.getPrice().setCurrency(unit);
        notifyPropertyChanged(BR.priceCurrency);
    }

    public String getPriceTotal() {
        Price price = item.getPrice();
        Quantity quantity = item.getQuantity();
        if (price != null && item.getQuantity() != null) {
            int total = price.getValue() * quantity.getAmount();
            return String.valueOf(total);
        }
        return null;
    }


    //todo move to another class
    public enum DIALOG_TAG {
        QUANTITY_AMOUNT, QUANTITY_UNIT, PRICE_VALUE, PRICE_CURRENCY
    }

    final String[] list = new String[]{"Units", "Gram (g)", "Kilogram (kg)", "Ounce (oz)", "Millitere (mL)", "Litre (L)"};

    public void onQuantityValueClick() {
        showQuantityValuePicker(getAmount(), getUnit(), DIALOG_TAG.QUANTITY_AMOUNT);
    }

    public void onQuantityUnitClick() {
        showQuantityUnitPicker(getUnit(), list, DIALOG_TAG.QUANTITY_UNIT);
    }

    public void onPriceValueClick() {
        showQuantityValuePicker(getPriceValue(), getPriceCurrency(), DIALOG_TAG.PRICE_VALUE);
    }

    public void onPriceCurrencyClick() {
        showQuantityUnitPicker(getPriceCurrency(), SavedCurrency.symbols, DIALOG_TAG.PRICE_CURRENCY);
    }

    public void showQuantityValuePicker(int amount, String unit, final DIALOG_TAG tag) {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setLabelText("Quantity amount")
                .setFragmentManager(context.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setCurrentNumber(amount)
                .setLabelText(unit)
                .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                        switch (tag) {
                            case QUANTITY_AMOUNT:
                                setAmount(number.intValue());
                                break;
                            case PRICE_VALUE:
                                setPriceValue(number.intValue());
                                break;
                        }
                    }
                });
        npb.show();
    }

    public void showQuantityUnitPicker(String unit, final String[] list, final DIALOG_TAG tag) {
        AlertDialogPro.Builder builder = new AlertDialogPro.Builder(context);
        AlertDialogPro dialog = builder
                .setTitle("Quantity Unit")
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (tag) {
                            case QUANTITY_UNIT:
                                setUnit(list[which]);
                                break;
                            case PRICE_CURRENCY:
                                setPriceCurrency(list[which]);
                                break;
                            default:
                                Log.d(TAG,"Unit fail");
                        }

                    }
                })
                .show();
        Window window = dialog.getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }

    private ImageView imageView;
    public void onClickImage(View view){
        activity.showAvatarPicker((ImageView) view);
    }
    public boolean onMenuItemClick(MenuItem menu) {
        switch (menu.getItemId()){
            case R.id.menu_favorite:
                if(!item.isFavorite()){
                    item.setFavorite(true);
                    menu.setIcon(R.drawable.heart);
                    Drawable icon= menu.getIcon();
                    ColorFilter filter = new LightingColorFilter( Color.RED, Color.RED);
                    icon.setColorFilter(filter);
                }
                else{
                    item.setFavorite(false);
                    menu.getIcon().setColorFilter(new LightingColorFilter( Color.WHITE, Color.WHITE));
                    menu.setIcon(R.drawable.ic_heart_outline_white_24dp);
                }
                break;
        }
        return true;
    }

}
