package com.mheev.helpthemshop.binding_adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mheev.helpthemshop.R;
import com.mheev.helpthemshop.activity.ImageLoaderCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by mheev on 9/14/2016.
 */
public class CustomAttribute {
    @BindingAdapter("adapter")
    public static void bindList(RecyclerView view, RecyclerView.Adapter adapter) {
        Log.d("CustomAttribute", "set RecyclerView attribute");
//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        view.setLayoutManager(new GridLayoutManager(view.getContext(),2));
        view.setAdapter(adapter);
    }

//    @BindingAdapter({"items", "itemLayout", "layoutManager"})
//    public static void bindChild(RecyclerView view, ObservableList list, int item, RecyclerView.LayoutManager layoutManager){
////        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        view.setLayoutManager(layoutManager);
//        view.setAdapter(new ItemAdapter(list, item));
//    }

    @BindingConversion
    public static @ColorRes int convertStringToColor(ColorEnum value){
        switch (value){
            case Primary:
                return R.color.colorPrimary;
            case PrimaryDark:
                return R.color.colorPrimaryDark;
            case White:
                return R.color.colorWhite;
            default:
                return R.color.colorPrimary;
        }
    }

    @BindingAdapter({"imgUrl", "callback"})
    public static void loadImage(ImageView imageView, String url, ImageLoaderCallback callback) {

        // make call to the initial loading event
        callback.onImageLoading();
        Log.d("CustomAttribute", "imageUrl:"+url);

        Picasso.with(imageView.getContext())
                .load(url)
                .resize(100,100)
                .placeholder(imageView.getDrawable())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        // after image has been successfully loaded
                        // make call to image ready event
                        callback.onImageReady();
                    }

                    @Override
                    public void onError() {
                        // since an error occured
                        // make call to image load error event
                        callback.onImageLoadError();
                    }
                });
    }

    @BindingAdapter({"imgUrl"})
    public static void loadImage(ImageView imageView, String url) {
        // make call to the initial loading event
        Log.d("CustomAttribute", "imageUrl:"+url);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(3)
                .cornerRadiusDp(60)
                .oval(false)
                .build();

        Picasso.with(imageView.getContext())
                .load(url)
//                .resize(100,100)
                .fit()
                .transform(transformation)
                .placeholder(imageView.getDrawable())
                .into(imageView);
    }


}
