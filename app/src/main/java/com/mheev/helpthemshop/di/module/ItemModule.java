package com.mheev.helpthemshop.di.module;

import com.mheev.helpthemshop.activity.BaseRxFragment;
import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.DataRepository;
import com.mheev.helpthemshop.model.ItemRequestManager;
import com.mheev.helpthemshop.model.ShoppingItemRepository;
import com.mheev.helpthemshop.model.UserItemRepository;
import com.mheev.helpthemshop.util.ItemScope;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mheev on 9/13/2016.
 */
@Module
public class ItemModule {
    public static final String ITEM_REPOSITORY = "item_repository";
    public static final String USER_REPOSITORY = "user_repository";
    private BaseRxFragment baseRxFragment;
    public ItemModule(BaseRxFragment baseRxFragment){
        this.baseRxFragment = baseRxFragment;
    }

    @Provides
    @ItemScope
    @Named(ITEM_REPOSITORY)
    DataRepository provideRepository(ItemRequestManager requestManager){
        return new ShoppingItemRepository(baseRxFragment, requestManager);
    }

    @Provides
    @ItemScope
    UserItemDbHelper provideDbHelper(){
        return  new UserItemDbHelper(baseRxFragment.getContext());
    }

    @Provides
    @ItemScope
    @Named(USER_REPOSITORY)
    DataRepository provideUserRepository(UserItemDbHelper dbHelper){
        return new UserItemRepository(dbHelper);
    }

//    private ShoppingItemRepository repository;
//
//    public ItemModule(ShoppingItemRepository repository){
//        this.repository = repository;
//    }
//
//    @Provides
//    @ItemScope
//    ShoppingItemRepository provideRepository(){
//        return repository;
//    }


//    private AppCompatActivity baseRxFragment;
//    public ItemModule(AppCompatActivity activityCompat){
//        this.baseRxFragment = activityCompat;
//    }
//
//    @Provides
//    @ItemScope
//    ShoppingItemRepository provideRepository(ItemRequestManager requestManager){
//
//        requestManager.loadItemList()
//                .compose(RxLoader.from(baseRxFragment))
//                .toBlocking()
//                .subscribe(new Observer<List<ShoppingItem>>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(baseRxFragment, "Load data complete", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(baseRxFragment, "Can't load data" + e.getMessage(), Toast.LENGTH_LONG).show();
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
