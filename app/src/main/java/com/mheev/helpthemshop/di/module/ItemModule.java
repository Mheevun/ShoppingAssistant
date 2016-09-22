package com.mheev.helpthemshop.di.module;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mheev.helpthemshop.App;
import com.mheev.helpthemshop.activity.MainActivity;
import com.mheev.helpthemshop.api.api_service.ItemRequestManager;
import com.mheev.helpthemshop.api.retrofit.ShoppingItemClient;
import com.mheev.helpthemshop.di.component.DaggerNetNavigatorItemComponent;
import com.mheev.helpthemshop.model.ShoppingItem;
import com.mheev.helpthemshop.repository.ShoppingItemRepository;
import com.mheev.helpthemshop.util.ItemScope;
import com.mheev.helpthemshop.util.NetNavigatorItemScope;
import com.mheev.helpthemshop.viewmodel.ItemSelectionViewModel;
import com.philosophicalhacker.lib.RxLoader;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;

/**
 * Created by mheev on 9/13/2016.
 */
@Module
public class ItemModule {
    private ShoppingItemRepository repository;

    public ItemModule(ShoppingItemRepository repository){
        this.repository = repository;
    }

    @Provides
    @ItemScope
    ShoppingItemRepository provideRepository(){
        return repository;
    }


//    private AppCompatActivity activity;
//    public ItemModule(AppCompatActivity activityCompat){
//        this.activity = activityCompat;
//    }
//
//    @Provides
//    @ItemScope
//    ShoppingItemRepository provideRepository(ItemRequestManager requestManager){
//
//        requestManager.loadItemList()
//                .compose(RxLoader.from(activity))
//                .toBlocking()
//                .subscribe(new Observer<List<ShoppingItem>>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(activity, "Load data complete", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(activity, "Can't load data" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<ShoppingItem> shoppingItemList) {
////                        viewModel.setItems();
////                        viewModel.isLoadingItems.set(false);
//                        repository = new ShoppingItemRepository(shoppingItemList);
//                    }
//                });
//        Log.d("ItemModule", "repository value: "+ repository);
//        return repository;
//    }


}
