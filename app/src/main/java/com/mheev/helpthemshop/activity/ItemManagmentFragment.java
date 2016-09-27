package com.mheev.helpthemshop.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.databinding.SelectItemsBinding;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.di.component.NetNavigatorItemComponent;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.model.eventbus.ItemSelectedEvent;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.viewmodel.ItemManagmentViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by mheev on 9/13/2016.
 */
public class ItemManagmentFragment extends BaseFragment {
    private final String TAG = "ItemManagmentFragment";

    @Inject
    ItemRequestManager itemRequestManager;

    private static NetNavigatorItemComponent netNavigatorItemComponent;
    private SelectItemsBinding binding;

    private ItemManagmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreated()");
        EventBus.getDefault().register(this);
        App.getNetComponent().inject(this);

        setRequestManager(itemRequestManager);

        binding = DataBindingUtil.inflate(inflater, R.layout.select_items, container, false);
        View view = binding.getRoot();
        viewModel = new ItemManagmentViewModel(this);
        binding.setViewModel(viewModel);

        viewModel.isLoadingItems.set(true);

        return view;
    }


    @Override
    protected void onItemRecievedFromDetailsActivity(ShoppingItem item) {
        viewModel.updateItem(item);
    }

    @Override
    protected void onItemUpdateOnNetwork(ShoppingItem item) {
        viewModel.updateItem(item);
    }

    @Override
    protected void onItemCreateOnNetwork(ShoppingItem item) {
        viewModel.updateItem(item);
    }

    public void onSelectItem(ShoppingItem item) {
        EventBus.getDefault().post(new ItemSelectedEvent(item));
    }

    @Override
    protected void onRecievedItemList(List<ShoppingItem> shoppingItemList) {
        ShoppingItemRepository repository = new ShoppingItemRepository(shoppingItemList);

        if (netNavigatorItemComponent == null) {
            ItemModule itemModule = new ItemModule(repository);
            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
                    .netComponent(App.getNetComponent())
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

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
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
                } else {
                    String id = viewModel.removeItem(position);
                    onRemoveItem(id);
                }
            }


        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
        viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

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
                } else {
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    ;

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
}

}
