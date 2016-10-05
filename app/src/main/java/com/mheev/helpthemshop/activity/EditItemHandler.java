package com.mheev.helpthemshop.activity;

import android.util.Log;
import android.view.View;

import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.util.Navigator;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mheev on 10/5/2016.
 */

public class EditItemHandler {
    DataRepository repository;
    Navigator navigator;
    public EditItemHandler(DataRepository repository, Navigator navigator) {
        this.repository = repository;
        this.navigator = navigator;
    }

    public void handlerItemUpdate(View view, ShoppingItem item) {
        navigator.toItemDetails(view, item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<EditItemEventResult>() {
                    @Override
                    public void call(EditItemEventResult editItemEventResult) {
                        switch (editItemEventResult.getUpdateResultType()){
                            case UPDATE: updateItem(editItemEventResult.getItem()); break;
                            case DELETE: removeItem(editItemEventResult.getItem()); break;
                            default: Log.w("EditItemHandler", "unknown edit item event result type");
                        }
                    }
                })
        ;
    }
    private void updateItem(ShoppingItem item){
        repository.updateItem(item);
    }
    private void removeItem(ShoppingItem item){
        repository.removeItem(item);
    }
}
