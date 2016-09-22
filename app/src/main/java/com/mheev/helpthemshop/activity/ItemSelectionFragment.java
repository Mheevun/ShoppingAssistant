package com.mheev.helpthemshop.activity;

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
import android.view.WindowManager;
import android.widget.Toast;

import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.databinding.SelectItemsBinding;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.di.component.NetNavigatorItemComponent;
import com.mheev.helpthemshop.di.module.ItemModule;
import com.mheev.helpthemshop.model.ShoppingItem;
import com.mheev.helpthemshop.repository.ShoppingItemRepository;
import com.mheev.helpthemshop.viewmodel.ItemSelectionViewModel;
import com.philosophicalhacker.lib.RxLoader;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by mheev on 9/13/2016.
 */
public class ItemSelectionFragment extends BaseFragment {
    private final String TAG = "ItemSelectionFragment";

    @Inject
    ItemRequestManager requestManager;

    private static NetNavigatorItemComponent netNavigatorItemComponent;
    private  SelectItemsBinding binding;

    private ItemSelectionViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreated()");


//        MainActivity.getNavigationComponent().inject(this);
        App.getNetComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.select_items, container, false);
        View view = binding.getRoot();
        viewModel = new ItemSelectionViewModel();
        binding.setViewModel(viewModel);

        loadItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"syncing items");
        for(ShoppingItem item:viewModel.displayItems){
            requestManager.syncItem(item);
        }

    }

    private void loadItems() {
        Log.d(TAG,"load item");
        viewModel.isLoadingItems.set(true);
        requestManager.loadItemList().compose(RxLoader.from((AppCompatActivity) getActivity()))
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
    }
    private void loadIntoRepository(List<ShoppingItem> shoppingItemList){
        ShoppingItemRepository repository = new ShoppingItemRepository(shoppingItemList);

        if(netNavigatorItemComponent==null){
            ItemModule itemModule = new ItemModule(repository);
            netNavigatorItemComponent = DaggerNetNavigatorItemComponent.builder()
                    .netComponent(App.getNetComponent())
                    .navigatorModule(MainActivity.getNavigatorModule())
                    .itemModule(itemModule)
                    .build();
        }
        viewModel.isLoadingItems.set(false);
        viewModel.initData(repository, MainActivity.getNavigatorModule().provideNavigator());
        initItemTouch(binding.myRecyclerView);
    }

    public static NetNavigatorItemComponent getNetNavigatorComponent(){
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
                    ShoppingItem item = viewModel.moveItemToActivePlan(position);
                    mCallback.toBuyingFragment(item);
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
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
