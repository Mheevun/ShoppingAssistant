package com.mheev.helpthemshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.databinding.ActivityMainBinding;
import com.mheev.helpthemshop.di.module.NavigatorModule;
import com.mheev.helpthemshop.model.pojo.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivityNavigator {
    private ItemManagmentFragment selectionFragment;
    private BuyingFragment buyingFragment;
    private ActivityMainBinding binding;
    private static NavigatorModule navigatorModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //adding toolbar to mainscreen
        setSupportActionBar(binding.toolbar);

        ViewPager viewPager = binding.viewpager;
        setupViewPager(viewPager);
        binding.tabs.setupWithViewPager(viewPager);

        navigatorModule = new NavigatorModule(this);
    }


    public static NavigatorModule getNavigatorModule() {
        return navigatorModule;
    }



    private void setupViewPager(ViewPager viewPager) {
        selectionFragment = new ItemManagmentFragment();
        buyingFragment = new BuyingFragment();

        StateFragmentAdapter adapter = new StateFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(selectionFragment, "Select Item");
        adapter.addFragment(buyingFragment, "Manage Item");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void toBuyingFragment(ShoppingItem buyingItem) {
        buyingFragment.getViewModel().addBuyingItem(buyingItem);
    }

    @Override
    public void toItemDetailsActivity(ShoppingItem item, View transitionView) {
        Intent intent = ItemDetailsActivity.getStartIntent(this, item);
        ActivityOptionsCompat transitionOption = getDetailTransitionOption(transitionView);
        if (transitionOption == null) {
            ActivityCompat.startActivity(this, intent, transitionOption.toBundle());
        } else {
            ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeBasic().toBundle());
        }
    }

    @Override
    public void toItemDetailsActivity(ShoppingItem item, View transitionView, Activity listenerResultActivity) {
        Intent intent = ItemDetailsActivity.getStartIntent(this, item);
        ActivityOptionsCompat transitionOption = getDetailTransitionOption(transitionView);
        if (transitionOption == null) {
            listenerResultActivity.startActivity(intent, transitionOption.toBundle());
        } else {
            listenerResultActivity.startActivity(intent, ActivityOptionsCompat.makeBasic().toBundle());
        }
    }

    @NonNull
    private ActivityOptionsCompat getDetailTransitionOption(View avatarView) {
        View view = binding.getRoot();
//        Log.d("qweqwe",view.findViewById(R.id.avatar)+"");
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) view.getContext(),
                new Pair<View, String>(avatarView, this.getString(R.string.transition_avatar))
//                new Pair<View, String>(view.findViewById(R.id.item_name),context.getString(R.string.transition_name))
        );
    }

    static class StateFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public StateFragmentAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
