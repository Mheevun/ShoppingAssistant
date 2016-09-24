package com.mheev.helpthemshop.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mheev.helpthemshop.model.pojo.ShoppingItem;

/**
 * Created by mheev on 9/15/2016.
 */
public class BaseFragment extends Fragment {
    /**
     * Dummy delegate to avoid nullpointers when
     * the fragment is not attached to an activity
     */
    private final ActivityNavigator sDummyDelegate = new ActivityNavigator() {
        @Override
        public void toBuyingFragment(ShoppingItem item) {
        }

        @Override
        public void toItemDetailsActivity(ShoppingItem item, View transitionView) {

        }

        @Override
        public void toItemDetailsActivity(ShoppingItem item, View transitionView, Activity listenerResultActivity) {

        }

    };

    /***********for communication between fragment***************/
    protected ActivityNavigator mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ActivityNavigator) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        mCallback = sDummyDelegate;
        super.onDetach();
    }

}
