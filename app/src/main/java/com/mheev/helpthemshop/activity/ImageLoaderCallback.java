package com.mheev.helpthemshop.activity;

/**
 * Created by mheev on 9/28/2016.
 */

public interface ImageLoaderCallback {

    // called initially
    void onImageLoading();

    // called when image has successfully loaded
    void onImageReady();

    // called when image loading fails
    void onImageLoadError();

}
