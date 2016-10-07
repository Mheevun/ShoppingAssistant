package com.mheev.helpthemshop.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.databinding.ItemStoreBinding;
import com.mheev.helpthemshop.viewmodel.StoreViewModel;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;

/**
 * Created by mheev on 10/7/2016.
 */

public class StoreActivity extends AppCompatActivity {
    private ItemStoreBinding binding;
    private int badgeCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_store);

        binding = DataBindingUtil.setContentView(this, R.layout.item_store);

        //adding toolbar to mainscreen
        setSupportActionBar(binding.toolbar);
        NavigationDrawer navigationDrawer = new NavigationDrawer(this, binding.toolbar);
        navigationDrawer.initNavigationDrawer();

        binding.setViewModel(new StoreViewModel());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_store, menu);

        //set color to white
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(new LightingColorFilter(Color.WHITE, Color.WHITE));
            }
        }


        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Log.d("StoreActivity", "SearchOnQueryTextSubmit: " + query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("StoreActivity", "SearchOnQueryTextSubmit: " + s);
                return false;
            }
        });

        //you can add some logic (hide it if the count == 0)
        badgeCount = 20;
        if (badgeCount > 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.shopping_list), ContextCompat.getDrawable(this, R.drawable.list), ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.shopping_list));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.shopping_list) {
            Toast.makeText(this, badgeCount, Toast.LENGTH_SHORT).show();
            badgeCount++;
            invalidateOptionsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
