package com.mheev.helpthemshop.activity;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.util.Navigator;


/**
 * Created by mheev on 10/7/2016.
 */

public class NavigationDrawer {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Activity activity;
    private Navigator navigator;

    public NavigationDrawer(Activity activity, Toolbar toolbar ){
        this.activity = activity;
        this.toolbar = toolbar;
        navigator = new Navigator(activity);
    }
    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)activity.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        Toast.makeText(activity,"Home",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.store:
                        Toast.makeText(activity,"Store",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        navigator.toStoreActivity();
                        break;
                    case R.id.shopping_list:
                        Toast.makeText(activity,"Shopping List",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.checkout:
                        Toast.makeText(activity,"Checkout",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("sample.email.com");
        drawerLayout = (DrawerLayout)activity.findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
