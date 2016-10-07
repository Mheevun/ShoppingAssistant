package com.mheev.helpthemshop.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.activity.ItemDetailsResultListener;
import com.mheev.helpthemshop.activity.StoreActivity;
import com.mheev.helpthemshop.model.eventbus.DetailActivityResultType;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mheev on 10/3/2016.
 */

public class Navigator {
    private Activity currentActivity;
    public static final RxBus<EditItemEvent> editItemBus = RxBus.createWithLatest();//singleton bus

    public Navigator(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity){
        this.currentActivity = currentActivity;
    }

    public Observable<EditItemEventResult> toItemDetails(View sharedElement, ShoppingItem item) {
        Observable<EditItemEventResult> obsItemEventResult =  Observable.create(new Observable.OnSubscribe<EditItemEventResult>() {
            @Override
            public void call(Subscriber<? super EditItemEventResult> subscriber) {
                ItemDetailsResultListener listener = new ItemDetailsResultListener() {

                    @Override
                    public void onReceiveResult(ShoppingItem item, DetailActivityResultType resultType) {
                        subscriber.onNext(new EditItemEventResult(item, resultType));
                    }
                };
                editItemBus.post(new EditItemEvent(item, listener));
            }
        });

        Intent intent = new Intent(currentActivity, ItemDetailsActivity.class);
//        intent.putExtra(ItemDetailsActivity.ID, itemId);//if item in itemViewModel does not has id?
        //If a task is already running for the activity you are now starting, that task is brought to the foreground with its last state restored and the activity receives the new intent in onNewIntent().
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currentActivity.startActivity(intent, getTransitionOption(sharedElement).toBundle());

        return obsItemEventResult;
    }
    private ActivityOptionsCompat getTransitionOption(View view) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                currentActivity,
                new Pair<View, String>(view, currentActivity.getString(R.string.transition_avatar))
//                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
        );
    }

    public void toStoreActivity(){
        currentActivity.startActivity(new Intent(currentActivity, StoreActivity.class));
    }




}
