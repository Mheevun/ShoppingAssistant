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
import java.io.FileOutputStream;
import java.io.IOException;
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



    /**
     * Launch a dialog to pick an image from camera/gallery apps.
     *
     * @param activity     which will launch the dialog.
     * @param chooserTitle will appear on the picker dialog.
     */
    public static void pickImage(Activity activity, String chooserTitle) {
        Intent chooseImageIntent = getPickImageIntent(activity, chooserTitle);
        activity.startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    /**
     * Get an Intent which will launch a dialog to pick an image from camera/gallery apps.
     *
     * @param context      context.
     * @param chooserTitle will appear on the picker dialog.
     * @return intent launcher.
     */
    public static Intent getPickImageIntent(Context context, String chooserTitle) {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTemporalFile(context)));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toArray(new Parcelable[intentList.size()]));
        }

        return chooserIntent;
    }

    public static void pickImage(Activity activity, String chooserTitle, ImageView imgView) {
        Intent chooseImageIntent = getPickImageIntent(activity, chooserTitle, imgView);
        activity.startActivityForResult(chooseImageIntent, 234);
    }

    public static Intent getPickImageIntent(Context context, String chooserTitle, ImageView imgView) {
        Intent chooserIntent = null;
        List intentList = new ArrayList();
        Intent pickIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imgView.getId()!= -1){
            pickIntent.putExtra(IMG_VIEW_ID, imgView.getId());
            takePhotoIntent.putExtra(IMG_VIEW_ID, imgView.getId());
        }
        Log.d(TAG, "getPickImageIntent: "+ pickIntent.getIntExtra(IMG_VIEW_ID,-1));

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);
        if(intentList.size() > 0) {
            chooserIntent = Intent.createChooser((Intent)intentList.remove(intentList.size() - 1), chooserTitle);
            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[])intentList.toArray(new Parcelable[intentList.size()]));
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
            Log.d(TAG, "isCamera: "+isCamera);
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.i(TAG, "context exist: " + context);
            Log.i(TAG, "file exist: " + imageFile.exists());
            Log.i(TAG, "selectedImage: " + selectedImage);


            bm = getImageResized(context, selectedImage);
        }
        return bm;
    }

    public static File createFileFromBitmap(Context context, Bitmap bitmap) {

        File newFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(newFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return newFile;

    }

    private static File getTemporalFile(Context context) {
        return new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
//        File file = null;
//        try {
//            file = File.createTempFile(TEMP_IMAGE_NAME, null, context.getCacheDir());
//        } catch (IOException e) {
//            // Error while creating file
//            e.printStackTrace();
//        }
//        return file;
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
//        return bm;
        return Bitmap.createScaledBitmap(bm, 100, 100, true);
    }
    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        Bitmap actuallyUsableBitmap = null;
        AssetFileDescriptor fileDescriptor = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            Log.d(TAG, "uri: "+ theUri);
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
