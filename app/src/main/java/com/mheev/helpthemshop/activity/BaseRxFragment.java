package com.mheev.helpthemshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mheev on 10/3/2016.
 */

public abstract class BaseRxFragment extends Fragment{
    protected CompositeSubscription subscriptions = new CompositeSubscription();

    public CompositeSubscription getSubscriptions() {
        return subscriptions;
    }
    public void addSubscription(Subscription subs){
        subscriptions.add(subs);
    }

    public void showToast(String msg, int toast_duration_length){
        Toast.makeText(getActivity(), msg, toast_duration_length).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

}
