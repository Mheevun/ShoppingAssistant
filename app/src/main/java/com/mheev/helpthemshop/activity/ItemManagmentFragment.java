package com.mheev.helpthemshop.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.databinding.SelectItemsBinding;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.di.component.NetNavigatorItemComponent;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.EditItemEventResult;
import com.mheev.helpthemshop.model.api.ApiCreateResponse;
import com.mheev.helpthemshop.model.api.ApiEditResponse;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.viewmodel.ItemManagmentViewModel;
import com.philosophicalhacker.lib.RxLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mheev on 9/13/2016.
 */
public class ItemManagmentFragment extends BaseFragment implements ItemManagmentListener {
    private final String TAG = "ItemManagmentFragment";

    @Inject
    ItemRequestManager requestManager;

    private static NetNavigatorItemComponent netNavigatorItemComponent;
    private SelectItemsBinding binding;

    private ItemManagmentViewModel viewModel;

    private CompositeSubscription subscriptions = new CompositeSubscription();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreated()");
        EventBus.getDefault().register(this);

//        MainActivity.getNavigationComponent().inject(this);
        App.getNetComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.select_items, container, false);
        View view = binding.getRoot();
        viewModel = new ItemManagmentViewModel(this);
        binding.setViewModel(viewModel);

        loadItems();

        return view;
    }

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
        viewModel.updateItem(item);
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
                                viewModel.updateItem(item);
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
                                viewModel.updateItem(shoppingItem);
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

    private void loadItems() {
        Log.d(TAG, "load item");
        viewModel.isLoadingItems.set(true);
        Subscription loadSubscription = requestManager.loadItemList().compose(RxLoader.from((AppCompatActivity) getActivity()))
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
                        loadIntoRepository(shoppingItemList);
                    }
                });
        subscriptions.add(loadSubscription);
    }

    private void loadIntoRepository(List<ShoppingItem> shoppingItemList) {
        ShoppingItemRepository repository = new ShoppingItemRepository(shoppingItemList);

        if (netNavigatorItemComponent == null) {
            ItemModule itemModule = new ItemModule(repository);
            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
                    .netComponent(App.getNetComponent())
                    .navigatorModule(MainActivity.getNavigatorModule())
                    .itemModule(itemModule)
                    .build();
        }
        viewModel.isLoadingItems.set(false);
        viewModel.initData(repository);
        initItemTouch(binding.myRecyclerView);
    }

    public static NetNavigatorItemComponent getNetNavigatorComponent() {
        return netNavigatorItemComponent;
    }


    private void initItemTouch(final RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT) {
                    ShoppingItem item = viewModel.moveItemToActivePlan(position);
                    mCallback.toBuyingFragment(item);
                }else {
                    String id = viewModel.removeItem(position);
                    subscriptions.add(requestManager.removeItem(id).subscribe(new Action1<ResponseBody>() {
                        @Override
                        public void call(ResponseBody response) {

                        }
                    }));
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

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
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                    else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
