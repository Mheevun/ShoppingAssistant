package com.mheev.helpthemshop.activity;

import android.app.Activity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by mheev on 10/3/2016.
 */

public class RxHandler {
    protected CompositeSubscription subscriptions = new CompositeSubscription();

    public CompositeSubscription getSubscriptions() {
        return subscriptions;
    }

    public void unSubscribe(){
        subscriptions.unsubscribe();
    }
}
