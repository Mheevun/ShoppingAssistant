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
import com.mheev.helpthemshop.databinding.SelectItemsBinding;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.di.component.NetNavigatorItemComponent;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.di.module.NavigatorModule;
import com.mheev.helpthemshop.viewmodel.ItemManagementViewModel;

import javax.inject.Inject;

/**
 * Created by mheev on 9/13/2016.
 */
public class ItemManagementFragment extends BaseRxFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = getTag();
    private static NetNavigatorItemComponent netNavigatorItemComponent;
    private SelectItemsBinding binding;

    @Inject
    ItemManagementViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreated() ItemManagementFragment");
        initDagger();

        Log.d(TAG, "after init dagger");
        binding = DataBindingUtil.inflate(inflater, R.layout.select_items, container, false);
        View view = binding.getRoot();
        Log.d(TAG, "viewModel: "+viewModel);
        binding.setViewModel(viewModel);
        binding.swipeContainer.setOnRefreshListener(this);

        return view;
    }

    //for swipeContainer
    @Override
    public void onRefresh() {
        binding.swipeContainer.setRefreshing(false);
        viewModel.loadItems();
    }

    private void initDagger(){
        if (netNavigatorItemComponent == null) {
            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
                    .netComponent(App.getNetComponent())
                    .itemModule(new ItemModule(this))
                    .navigatorModule(new NavigatorModule(getActivity()))
                    .build();
        }
        Log.d(TAG, "after create component");
        netNavigatorItemComponent.inject(this);
    }

    public static NetNavigatorItemComponent getNetNavigatorComponent() {
        return netNavigatorItemComponent;
    }


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
                    viewModel.moveItemToBuying(position);
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

}
