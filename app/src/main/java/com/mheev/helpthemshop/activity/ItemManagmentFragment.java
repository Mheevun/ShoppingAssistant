package com.mheev.helpthemshop.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.di.module.NavigatorModule;
import com.mheev.helpthemshop.databinding.SelectItemsBinding;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.di.component.NetNavigatorItemComponent;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.model.eventbus.ItemSelectedEvent;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.ItemManagmentViewModel;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by mheev on 9/13/2016.
 */
//public class ItemManagmentFragment extends Fragment implements OnEditItemListener, SwipeRefreshLayout.OnRefreshListener {
public class ItemManagmentFragment extends BaseRxFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = getTag();
    private static NetNavigatorItemComponent netNavigatorItemComponent;
    private SelectItemsBinding binding;

    @Inject
    ItemManagmentViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreated()");
        initDagger();

        binding = DataBindingUtil.inflate(inflater, R.layout.select_items, container, false);
        View view = binding.getRoot();
        binding.setViewModel(viewModel);
        binding.swipeContainer.setOnRefreshListener(this);


        return view;
    }

    //for swipeContainer
    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(false);
        viewModel.isLoadingItems.set(true);
        binding.myRecyclerView.setVisibility(View.INVISIBLE);

    }

    private void initDagger(){
        if (netNavigatorItemComponent == null) {
            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
                    .netComponent(App.getNetComponent())
                    .itemModule(new ItemModule(this))
                    .navigatorModule(new NavigatorModule(getActivity()))
                    .build();
        }
        netNavigatorItemComponent.inject(this);

//        viewModel.isLoadingItems.set(false);
//        binding.myRecyclerView.setVisibility(View.VISIBLE);
//        viewModel.initData(repository);

    }

    public static NetNavigatorItemComponent getNetNavigatorComponent() {
        return netNavigatorItemComponent;
    }



//    private void loadItems() {
//        Log.d(TAG, "load item");
////        Subscription loadSubscription = requestManager.loadItemList().compose(RxLoader.from((AppCompatActivity) getActivity()))
//        Subscription loadSubscription = itemRequestManager.loadItemList()
//                .subscribe(new Observer<List<ShoppingItem>>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(getContext(), "Load data complete", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(getContext(), "Can't load data" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<ShoppingItem> shoppingItemList) {
//                        onRecievedItemList(shoppingItemList);
//                    }
//                });
//        subscriptions.add(loadSubscription);
//    }

//    public void onEditItemDetails(ShoppingItem item, View view) {
//        Log.d(TAG, "create intent: " + item);
//
//        int position = viewModel.getPosition(item);
//        item.position = position;
//
//        Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
//        EventBus.getDefault().postSticky(new EditItemEvent(item, this));
//        startActivity(intent, getTransitionOption(view).toBundle());
//    }
//
//    private ActivityOptionsCompat getTransitionOption(View view) {
//        return ActivityOptionsCompat.makeSceneTransitionAnimation(
//                getActivity(),
//                new Pair<View, String>(view, this.getString(R.string.transition_avatar))
////                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
//        );
//    }

//    public void onEditItemDetailsResult(ShoppingItem item) {
//
//        Log.d(TAG, "get item from ItemDetailsActivity: " + item.getItemName());
//        if (item.getItemName() == null) return;
//        Subscription updateSubscription;
//        viewModel.updateItem(item);
//        Log.d(TAG, "item id: " + item.getId());
//        if (item.isNew()) {
//            Log.d(TAG, "perform create item request" + item.getItemName());
//            updateSubscription = itemRequestManager.createItem(item).subscribe(new Action1<ApiCreateResponse>() {
//                @Override
//                public void call(ApiCreateResponse apiCreateResponse) {
//                    Log.d(TAG, "create item response details: " + apiCreateResponse.getObjectId());
//                    item.setId(apiCreateResponse.getObjectId());
//                    itemRequestManager.getItem(item.getId()).subscribe(new Action1<ShoppingItem>() {
//                        @Override
//                        public void call(ShoppingItem shoppingItem) {
//                            viewModel.updateItem(item);
//                        }
//                    });
//                }
//            });
//        } else {
//            Log.d(TAG, "perform update item request: " + item.getItemName());
//            updateSubscription = itemRequestManager.updateItem(item).subscribe(new Action1<ApiEditResponse>() {
//                @Override
//                public void call(ApiEditResponse apiEditResponse) {
//                    Log.d(TAG, "update item response details: " + apiEditResponse.getUpdatedAt());
//                    itemRequestManager.getItem(item.getId()).subscribe(new Action1<ShoppingItem>() {
//                        @Override
//                        public void call(ShoppingItem shoppingItem) {
//                            viewModel.updateItem(item);
//                        }
//                    });
//
//                }
//            });
//        }
//        subscriptions.add(updateSubscription);
//    }

//
//    public void onDeleteItem(ShoppingItem item) {
////        int position = viewModel.getPosition(item);
//        if (item.position < 0) return;
//        String itemId = viewModel.removeItem(item.position);
//        if (itemId != null) {
//            subscriptions.add(
//                    itemRequestManager.removeItem(itemId)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .unsubscribeOn(Schedulers.io())
//                            .subscribe(new Observer<ResponseBody>() {
//                        @Override
//                        public void onCompleted() {
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onNext(ResponseBody responseBody) {
//                        }
//                    }));
//        }
//    }

    public void onSelectItem(ShoppingItem item) {
        EventBus.getDefault().post(new ItemSelectedEvent(item));
    }

//    protected void onRecievedItemList(List<ShoppingItem> shoppingItemList) {
//        ShoppingItemRepository repository = new ShoppingItemRepository(shoppingItemList);
//
//        if (netNavigatorItemComponent == null) {
//            ItemModule itemModule = new ItemModule(repository);
//            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
//                    .netComponent(App.getNetComponent())
//                    .itemModule(itemModule)
//                    .build();
//        }
//        viewModel.isLoadingItems.set(false);
//        binding.myRecyclerView.setVisibility(View.VISIBLE);
//        viewModel.initData(repository);
//        initItemTouch(binding.myRecyclerView);
//    }


    private void initItemTouch(final RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {
                    ShoppingItem item = viewModel.moveItemToActivePlan(position);
                    onSelectItem(item);
                }
//              else {
//                    String id = viewModel.removeMemItem(position);
//                    onRemoveItem(id);
//                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_shopping_cart_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
