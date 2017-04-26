package com.wcong.watermask.photo;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by wangcong on 2017/3/17.
 * <p>
 */

public class CallBackActivity extends AppCompatActivity {

  protected static PhotoListener photoListener;
  protected static WaterMask.WaterMaskListener waterMarkListener;

  public static void setPhotoListener(PhotoListener photoListener) {
    CallBackActivity.photoListener = photoListener;
  }

  public static void setWaterListener(WaterMask.WaterMaskListener listener) {
    CallBackActivity.waterMarkListener = listener;
  }
}
