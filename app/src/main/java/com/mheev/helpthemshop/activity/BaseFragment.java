package com.mheev.helpthemshop.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.mheev.helpthemshop.api.api_service.RequestManager;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mheev on 9/15/2016.
 */
public abstract class BaseFragment extends Fragment implements OnEditItemListener {
    public final String TAG = getTag();
    private RequestManager requestManager;


    protected CompositeSubscription subscriptions = new CompositeSubscription();

    protected void setRequestManager(RequestManager requestManager){
        this.requestManager = requestManager;
        loadItems();
    }

    private void loadItems() {
        Log.d(TAG, "load item");
//        Subscription loadSubscription = requestManager.loadItemList().compose(RxLoader.from((AppCompatActivity) getActivity()))
        Subscription loadSubscription = requestManager.loadItemList()
                .subscribe(new Observer<List<ShoppingItem>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getContext(), "Load data complete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "Can't load data" + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<ShoppingItem> shoppingItemList) {
                        onRecievedItemList(shoppingItemList);
                    }
                });
        subscriptions.add(loadSubscription);
    }
    /*********************EditItemDetail****************/
    @Override
    public void onEditItemDetails(ShoppingItem item) {
        Log.d(TAG, "create intent: " + item);
        Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
        EventBus.getDefault().postSticky(new EditItemEvent(item));
        startActivity(intent);
    }

    @Subscribe
    public void onRecieveEditEventResult(EditItemEventResult event) {
        Log.d(TAG, "get item from ItemDetailsActivity: " + event.getItem().getItemName());
        if(event.getItem().getItemName()==null)return;
        ShoppingItem item = event.getItem();
        Subscription updateSubscription;
        onItemRecievedFromDetailsActivity(item);
        Log.d(TAG, "item id: "+item.getId());
        if(item.isNew()) {
            Log.d(TAG, "perform create item request"+item.getItemName());
            updateSubscription = requestManager.createItem(item).subscribe(new Action1<ApiCreateResponse>() {
                @Override
                public void call(ApiCreateResponse apiCreateResponse) {
                    Log.d(TAG, "create item response details: "+apiCreateResponse.getObjectId());
                    item.setId(apiCreateResponse.getObjectId());
                    requestManager.getItem(item.getId()).subscribe(new Action1<ShoppingItem>() {
                        @Override
                        public void call(ShoppingItem shoppingItem) {
                            onItemUpdateOnNetwork(shoppingItem);
                        }
                    });
                }
            });
        }
        else {
            Log.d(TAG, "perform update item request: "+item.getItemName());
            updateSubscription = requestManager.updateItem(item).subscribe(new Action1<ApiEditResponse>() {
                @Override
                public void call(ApiEditResponse apiEditResponse) {
                    Log.d(TAG, "update item response details: "+apiEditResponse.getUpdatedAt());
                    requestManager.getItem(item.getId()).subscribe(new Action1<ShoppingItem>() {
                        @Override
                        public void call(ShoppingItem shoppingItem) {
                            onItemCreateOnNetwork(shoppingItem);
                        }
                    });

                }
            });
        }
        subscriptions.add(updateSubscription);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        subscriptions.unsubscribe();
    }

    protected abstract void onRecievedItemList(List<ShoppingItem> shoppingItemList);
    protected abstract void onItemRecievedFromDetailsActivity(ShoppingItem item);
    protected abstract void onItemUpdateOnNetwork(ShoppingItem item);
    protected abstract void onItemCreateOnNetwork(ShoppingItem item);
    protected void onRemoveItem(String itemId){
        if(itemId!=null) {
            subscriptions.add(requestManager.removeItem(itemId).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(ResponseBody responseBody) {

                }
            }));
        }
    }


//    /**
//     * Dummy delegate to avoid nullpointers when
//     * the fragment is not attached to an activity
//     */
//    private final ActivityNavigator sDummyDelegate = new ActivityNavigator() {
//        @Override
//        public void toBuyingFragment(ShoppingItem item) {
//        }
//
//        @Override
//        public void toItemDetailsActivity(ShoppingItem item, View transitionView) {
//
//        }
//
//        @Override
//        public void toItemDetailsActivity(ShoppingItem item, View transitionView, Activity listenerResultActivity) {
//
//        }
//
//    };
//
//    /***********for communication between fragment***************/
//    protected ActivityNavigator mCallback;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (ActivityNavigator) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        mCallback = sDummyDelegate;
//        super.onDetach();
//    }

}
