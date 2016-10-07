package com.mheev.helpthemshop.viewmodel;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.mheev.helpthemshop.anim.BounceInterpolator;
import android.widget.Button;

import com.mheev.helpthemshop.R;

/**
 * Created by mheev on 10/7/2016.
 */

public class StoreViewModel {
    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce);

        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }

}
