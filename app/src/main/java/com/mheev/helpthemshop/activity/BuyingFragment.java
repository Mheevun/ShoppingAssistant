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
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.databinding.BuyingItemsBinding;
import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.eventbus.EditItemEvent;
import com.mheev.helpthemshop.model.eventbus.ItemSelectedEvent;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;
import com.mheev.helpthemshop.viewmodel.BuyingViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by mheev on 9/15/2016.
 */
public class BuyingFragment extends Fragment implements OnEditItemListener {

    private BuyingViewModel viewModel;
    private BuyingItemsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App.getNetComponent().inject(this);
        EventBus.getDefault().register(this);

//        setRequestManager(userRequestManager);

        viewModel = new BuyingViewModel(this, new UserItemDbHelper(this.getContext()));
        binding = DataBindingUtil.inflate(inflater, R.layout.buying_items, container, false);
        View view = binding.getRoot();
        binding.setViewModel(viewModel);
        initItemTouch(binding.itemList);

        return view;
    }


    @Subscribe
    public void onRecieveSelectedItem(ItemSelectedEvent event){
        viewModel.addItem(event.getItem());
    }


    public void onEditItemDetails(ShoppingItem item, View view) {
        Log.d("BuyingFragment", "create intent: " + item);
        Intent intent = new Intent(getContext(), ItemDetailsActivity.class);
        EventBus.getDefault().postSticky(new EditItemEvent(item, this));
        startActivity(intent, getTransitionOption(view).toBundle());
    }
    private ActivityOptionsCompat getTransitionOption(View view) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                new Pair<View, String>(view, this.getString(R.string.transition_avatar))
//                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
        );
    }


    @Override
    public void onEditItemDetailsResult(ShoppingItem item) {
        Log.d(getTag(), "on recieve edit item detail result (id): "+item.getId());
        if(item.isNew())
            viewModel.addItem(item);
        else
            viewModel.updateItem(item);
    }

    @Override
    public void onDeleteItem(ShoppingItem item) {
        viewModel.removeItem(item.position);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initItemTouch(final RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.removeItem(position);
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

                    } else {
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
