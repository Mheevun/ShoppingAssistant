package com.mheev.helpthemshop.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mheev on 9/19/2016.
 */
public class ImagePicker {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    public static final String IMG_VIEW_ID = "img_view_id";
    private static final String TAG = ImagePicker.class.getSimpleName();
    private static final String TEMP_IMAGE_NAME = "tempImage";

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final int DEFAULT_MIN_HEIGHT_QUALITY = 400;        // min pixels
    private static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    private static int minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY;



    public static void pickImage(Activity activity, String chooserTitle, ImageView imgView) {
        Intent chooseImageIntent = getPickImageIntent(activity, chooserTitle, imgView);
        activity.startActivityForResult(chooseImageIntent, 234);
    }

    public static Intent getPickImageIntent(Context context, String chooserTitle, ImageView imgView) {
        Intent chooserIntent = null;
        ArrayList intentList = new ArrayList();
        Intent pickIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imgView.getId()!=-1){
            pickIntent.putExtra(IMG_VIEW_ID, imgView.getId());
            takePhotoIntent.putExtra(IMG_VIEW_ID, imgView.getId());
        }
        Log.d(TAG, "getPickImageIntent: "+ pickIntent.getIntExtra(IMG_VIEW_ID,-1));

        List intentList1 = addIntentsToList(context, intentList, pickIntent);
        intentList1 = addIntentsToList(context, intentList1, takePhotoIntent);
        if(intentList1.size() > 0) {
            chooserIntent = Intent.createChooser((Intent)intentList1.remove(intentList1.size() - 1), chooserTitle);
            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[])intentList1.toArray(new Parcelable[intentList1.size()]));
        }
        Log.d(TAG, pickIntent.getIntExtra(IMG_VIEW_ID,-1)+"");
        Log.d(TAG,"chooserIntent:"+pickIntent.getExtras()+"");

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        Log.i(TAG, "Adding intents of type: " + intent.getAction());
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.i(TAG, "App package: " + packageName);
        }
        return list;
    }

    public static Bitmap getImageFromResult(Context context, int requestCode, int resultCode,
                                            Intent imageReturnedIntent) {
        Log.i(TAG, "getImageFromResult() called with: " + "resultCode = [" + resultCode + "]");
        Bitmap bm = null;
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_ID) {
            File imageFile = getTemporalFile(context);
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null
                    || imageReturnedIntent.getData() == null
                    || imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.i(TAG, "selectedImage: " + selectedImage);
            Log.i(TAG, "result image intent: " + imageReturnedIntent.getExtras());
            Log.i(TAG, "result image intent: " + imageReturnedIntent.getIntExtra(IMG_VIEW_ID,-1));

            bm = getImageResized(context, selectedImage);
//            int rotation = ImageRotator.getRotation(context, selectedImage, isCamera);
//            bm = ImageRotator.rotate(bm, rotation);
        }
        return bm;
    }

    private static File getTemporalFile(Context context) {
        return new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
    }

    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            i++;
        } while (bm != null
                && (bm.getWidth() < minWidthQuality || bm.getHeight() < minHeightQuality)
                && i < sampleSizes.length);
        Log.i(TAG, "Final bitmap width = " + (bm != null ? bm.getWidth() : "No final bitmap"));
        return bm;
    }
    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        Bitmap actuallyUsableBitmap = null;
        AssetFileDescriptor fileDescriptor = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fileDescriptor != null) {
            actuallyUsableBitmap = BitmapFactory
                    .decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
            Log.i(TAG, "Trying sample size " + options.inSampleSize + "\t\t"
                    + "Bitmap width: " + actuallyUsableBitmap.getWidth()
                    + "\theight: " + actuallyUsableBitmap.getHeight());
        }

        return actuallyUsableBitmap;
    }
    /*
    GETTERS AND SETTERS
     */

    public static void setMinQuality(int minWidthQuality, int minHeightQuality) {
        ImagePicker.minWidthQuality = minWidthQuality;
        ImagePicker.minHeightQuality = minHeightQuality;
    }
}
