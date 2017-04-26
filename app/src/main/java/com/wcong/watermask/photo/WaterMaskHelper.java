package com.wcong.watermask.photo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by wangcong on 2017/4/25.
 * <p>
 */

public class WaterMaskHelper {

    private Context context;

    private PhotoListener photoListener;
    private WaterMask.WaterMaskListener waterMarkListener;

    public WaterMaskHelper(Context context, PhotoListener photoListener, WaterMask.WaterMaskListener waterMarkListener) {
        this.context = context;
        this.photoListener = photoListener;
        this.waterMarkListener = waterMarkListener;
    }

    public void startCapture() {
        context.startActivity(new Intent(context, PhotoCaptureActivity.class));
        PhotoCaptureActivity.setWaterListener(waterMarkListener);
        PhotoCaptureActivity.setPhotoListener(photoListener);
    }
}
