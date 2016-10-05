package com.mheev.helpthemshop.di.module;

import com.mheev.helpthemshop.activity.BaseRxFragment;
import com.mheev.helpthemshop.db.UserItemDbHelper;
import com.mheev.helpthemshop.model.ItemRepository;
import com.mheev.helpthemshop.model.ItemRequestManager;
import com.mheev.helpthemshop.model.UserItemRepository;
import com.mheev.helpthemshop.util.ItemScope;

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
    ItemRepository provideRepository(ItemRequestManager requestManager){
        return new ItemRepository(baseRxFragment, requestManager);
    }

    @Provides
    @ItemScope
    UserItemDbHelper provideDbHelper(){
        return  new UserItemDbHelper(baseRxFragment.getContext());
    }

    @Provides
    @ItemScope
    UserItemRepository provideUserRepository(UserItemDbHelper dbHelper){
        return new UserItemRepository(dbHelper);
    }

}
