package com.mheev.helpthemshop.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.repository.ShoppingItemRepository;
import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.model.Quantity;
import com.mheev.helpthemshop.model.ShoppingItem;

import javax.inject.Inject;

/**
 * Created by mheev on 9/12/2016.
 */
public class ItemViewModel extends BaseObservable{
    private ShoppingItem item;

    @Inject
    ShoppingItemRepository repo;

    private String SPACE = " ";
    private String LINE = "\n";

    private Class<?> detailsActivity = ItemDetailsActivity.class;


    public ItemViewModel(ShoppingItem item){
        this.item = item;
//        App.getItemComponent().inject(this);
    }

    public String getItemSummary(){
        String summary = item.getItemName();
        Quantity quantity = item.getQuantity();
        if(quantity!=null){
            summary = summary + LINE +item.getQuantity().getAmount() + SPACE +  item.getQuantity().getUnit();
        }
        return summary;
    }

    public void onItemClick(View view){
//        repo.addItem(item);
        Intent intent = ItemDetailsActivity.getStartIntent(view.getContext(),item);
        ActivityOptionsCompat transitionOption = getDetailTransitionOption(view);
        ActivityCompat.startActivity((Activity) view.getContext(), intent,transitionOption.toBundle());
    }

    private ActivityOptionsCompat getDetailTransitionOption(View view){
        Context context = view.getContext();
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) view.getContext(),
                new Pair<View, String>(view.findViewById(R.id.avatar),context.getString(R.string.transition_avatar))
//                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
        );
    }

}
