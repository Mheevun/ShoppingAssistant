package com.mheev.helpthemshop.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.mheev.helpthemshop.BR;
import com.mheev.helpthemshop.activity.OnEditItemListener;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.activity.ItemDetailsActivity;
import com.mheev.helpthemshop.model.pojo.Quantity;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

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

    public ObservableBoolean isSync = new ObservableBoolean();
    private OnEditItemListener listener;

    public ItemViewModel(ShoppingItem item, OnEditItemListener listener){
        this.item = item;
        this.listener = listener;
//        App.getItemComponent().inject(this);
    }

    public String getItemSummary(){
        String summary = item.getItemName();
        Quantity quantity = item.getQuantity();
        if(quantity!=null){
            String amount, unit;
            summary = summary + LINE +item.getQuantity().getAmount() + SPACE +  item.getQuantity().getUnit();
        }
        return summary;
    }

    @Bindable
    public String getAvatarUrl(){
        return item.getItemAvatarURL();
    }
    public void setAvatarUrl(String url){
        item.setItemAvatarURL(url);
        notifyPropertyChanged(BR.avatarUrl);
    }

    public void onItemClick(View view){
        listener.onEditItemDetails(item);
//        Intent intent = ItemDetailsActivity.getStartIntent(view.getContext(),item);
//        ActivityOptionsCompat transitionOption = getDetailTransitionOption(view);
//        ActivityCompat.startActivity((Activity) view.getContext(), intent,transitionOption.toBundle());
    }

//    private ActivityOptionsCompat getDetailTransitionOption(View view){
//        Context context = view.getContext();
//        return ActivityOptionsCompat.makeSceneTransitionAnimation(
//                (Activity) view.getContext(),
//                new Pair<View, String>(view.findViewById(R.id.avatar),context.getString(R.string.transition_avatar))
////                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
//        );
//    }

}
