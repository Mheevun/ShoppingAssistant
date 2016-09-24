package com.mheev.helpthemshop.di.module;

import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.util.ItemScope;

import dagger.Module;
import dagger.Provides;

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
