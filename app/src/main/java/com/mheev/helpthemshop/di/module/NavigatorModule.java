package com.mheev.helpthemshop.di.module;

import com.mheev.helpthemshop.activity.ActivityNavigator;
import com.mheev.helpthemshop.util.ItemScope;
import com.mheev.helpthemshop.util.NavigatorScope;
import com.mheev.helpthemshop.util.NetNavigatorItemScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mheev on 9/21/2016.
 */
@Module
public class NavigatorModule {
    private final ActivityNavigator navigator;
    public NavigatorModule(ActivityNavigator navigationListener) {
        this.navigator = navigationListener;
    }

    @Provides
    @ItemScope
    public ActivityNavigator provideNavigator(){
        return navigator;
    }

//    @Singleton
//    @Provides
//    public Intent provideItemDetailsActivityIntent() {
//        Intent intent = new Intent(rootContext, ItemDetailsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        return intent;
//    }
//
//    @Singleton
//    @Provides
//    public ActivityOptionsCompat provideTransitionAnimation() {
//        View avatar = (View) ((Activity) rootContext).findViewById(R.id.avatar);
//        return ActivityOptionsCompat.makeSceneTransitionAnimation(
//                (Activity) rootContext,
//                new Pair<View, String>(avatar, rootContext.getString(R.string.transition_avatar))
////                new Pair<View, String>(view.findViewById(R.id.item_name),rootContext.getString(R.string.transition_name))
//        );
//    }
//
//    @Singleton
//    @Provides
//    public Activity provideActivity() {
//        return (Activity) rootContext;
//    }


}
