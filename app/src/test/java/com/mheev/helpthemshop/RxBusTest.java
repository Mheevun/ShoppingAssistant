package com.mheev.helpthemshop;

import com.mheev.helpthemshop.activity.MainActivity;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.RxBus;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static org.junit.Assert.assertEquals;

/**
 * Created by mheev on 10/4/2016.
 */

public class RxBusTest {
    private static final RxBus<ShoppingItem> sAppBus = RxBus.createWithLatest();//singleton bus
    private static final String ITEM_NAME = "dummy item";
    private static Subscription subscription;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void testObserver(){
        ShoppingItem item = new ShoppingItem();
        item.setItemName(ITEM_NAME);
        sAppBus.post(item);

        subscription = sAppBus.observeEvents(ShoppingItem.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ShoppingItem>() {
                    @Override
                    public void call(ShoppingItem shoppingItem) {
                        assertEquals(shoppingItem.getItemName(), ITEM_NAME);
                    }
                });
    }

    @AfterClass
    public static void afterClass(){
        subscription.unsubscribe();
    }
}
