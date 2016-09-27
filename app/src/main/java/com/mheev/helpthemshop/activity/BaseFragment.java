package com.mheev.helpthemshop.activity;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.mheev.helpthemshop.api.api_service.RequestManager;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        subscriptions.unsubscribe();
    }

    protected abstract void onRecievedItemList(List<ShoppingItem> shoppingItemList);
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
