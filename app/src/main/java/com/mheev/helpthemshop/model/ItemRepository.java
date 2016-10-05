package com.mheev.helpthemshop.model;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.widget.Toast;

import com.mheev.helpthemshop.activity.BaseRxFragment;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by mheev on 9/15/2016.
 */
public class ItemRepository implements DataRepository {
    private ObservableArrayList<ShoppingItem> items = new ObservableArrayList<ShoppingItem>();
    private ObservableBoolean isLoading = new ObservableBoolean(true);
    private static final String TAG = "ItemRepository";
    private ItemRequestManager requestManager;
    private BaseRxFragment baseActivity;

    public ItemRepository(BaseRxFragment baseActivity, ItemRequestManager requestManager) {
        this.requestManager = requestManager;
        this.baseActivity = baseActivity;

        loadItems();
    }

    public void loadItems() {
        Log.d(TAG, "load item");
        isLoading.set(true);
        Subscription loadSubscription = requestManager.loadItemList()
                .doOnNext(shoppingItems -> setItems(shoppingItems))
                .subscribe(new Observer<List<ShoppingItem>>() {
                    @Override
                    public void onCompleted() {
                        baseActivity.showToast("Load data complete", Toast.LENGTH_LONG);
                        isLoading.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        baseActivity.showToast("Can't load data", Toast.LENGTH_SHORT);
                        e.printStackTrace();
                        isLoading.set(false);
                    }

                    @Override
                    public void onNext(List<ShoppingItem> shoppingItemList) {
                        Log.d(TAG, "set item in the memory");
                        setItems(shoppingItemList);
                    }
                });
        baseActivity.getSubscriptions().add(loadSubscription);
    }
    public ObservableBoolean getIsLoading() {
        return isLoading;
    }


    private void setItems(List<ShoppingItem> shoppingItemList) {
        items.clear();
        items.addAll(shoppingItemList);
    }

    public void updateItem(ShoppingItem item) {
        Log.d(TAG, "get item from ItemDetailsActivity: " + item.getItemName());
        if (item.getItemName() == null) return;
        Subscription updateSubscription;

        //syncing
        Log.d(TAG, "sync on the network: " + item.getItemName());
        if (item.isNew()) {
            items.add(item);
            int index = items.indexOf(item);

            Log.d(TAG, "perform create item request" + item.getItemName());
            updateSubscription = requestManager.createItem(item).subscribe(new Subscriber<ApiCreateResponse>() {
                @Override
                public void onCompleted() {
                    baseActivity.showToast("create item complete", Toast.LENGTH_SHORT);
                }

                @Override
                public void onError(Throwable e) {
                    items.remove(item);
                    baseActivity.showToast("error cant create the item", Toast.LENGTH_LONG);
                }

                @Override
                public void onNext(ApiCreateResponse apiCreateResponse) {
                    Log.d(TAG, "create item response details: " + apiCreateResponse.getObjectId());
                    items.get(index).setId(apiCreateResponse.getObjectId());
                }

            });
        } else {
            Log.d(TAG, "perform update item request: " + item.getItemName());
            updateSubscription = requestManager.updateItem(item).subscribe(new Subscriber<ApiEditResponse>() {
                @Override
                public void onCompleted() {
                    baseActivity.showToast("update item complete", Toast.LENGTH_SHORT);
                }

                @Override
                public void onError(Throwable e) {
                    baseActivity.showToast("error cant update the item", Toast.LENGTH_LONG);
                }

                @Override
                public void onNext(ApiEditResponse apiEditResponse) {
                    Log.d(TAG, "update item response details: " + apiEditResponse.getUpdatedAt());
                    updateItemInMemory(item);
                }
            });
        }

        baseActivity.addSubscription(updateSubscription);
    }

    public void removeItem(ShoppingItem item) {
        String itemId = item.getId();
        if (itemId != null) {
            Subscription updateSubscription = requestManager.removeItem(itemId)
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            baseActivity.showToast("error can't delete the item", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            items.remove(item);
                        }
                    });
            baseActivity.addSubscription(updateSubscription);
        }
    }

    @Override
    public void removeItem(int position) {
        ShoppingItem item = getItem(position);
        removeItem(item);
    }

    @Override
    public void deleteAll() {
        Log.d(TAG, "does not implement yet");
    }


    public ObservableArrayList<ShoppingItem> getItems() {
        return items;
    }

    public ShoppingItem getItem(String id) {
        Log.d("ItemRepository", "item size: " + items.size());
        for (ShoppingItem item : items) {
            if (item.getId().equals(id))
                return item;
        }
        return null;
    }

    public ShoppingItem getItem(int position){
        return items.get(position);
    }

//    private void removeMemItem(String id) {
//        ShoppingItem item = getItem(id);
//        items.remove(item);
//    }
//
//    private void removeMemItem(ShoppingItem item){
//        items.remove(item);
//    }

//    private void addItemInMemory(ShoppingItem item) {
//        if (item.isNew()) {
//            Log.d(TAG, "add new item in repository");
//            addNewItemInMem(item);
//        } else {
//            Log.d(TAG, "update current item in repository");
//            updateItemInMem(item);
//        }
//    }

//    private void addNewItemInMem(ShoppingItem item) {
//        items.add(item);
//    }


    private void updateItemInMemory(ShoppingItem updatedItem) {
        int index = getIndexById(updatedItem);
        items.set(index, updatedItem);
    }

    private int getIndexById(ShoppingItem item) {
        for (ShoppingItem repoItem : items) {
            if (item.getId().equals(repoItem.getId())) {
                int index = items.indexOf(repoItem);
                return index;
            }
        }
        return -1;
    }


}
