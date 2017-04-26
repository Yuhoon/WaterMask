package com.wcong.watermask;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.wcong.watermask.databinding.ActivityMainBinding;
import com.wcong.watermask.photo.PhotoListener;
import com.wcong.watermask.photo.WaterMask;
import com.wcong.watermask.photo.WaterMaskHelper;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPreview;

public class MainActivity extends AppCompatActivity implements WaterMask.WaterMaskListener, PhotoListener {

    ActivityMainBinding binding;

    private WaterMaskHelper waterMaskHelper;

    private ArrayList<String> uris;
    private int maskLocation;
    private View lastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init() {
        binding.setPresenter(new Presenter());
        waterMaskHelper = new WaterMaskHelper(this, this, this);
        lastView = binding.txtCenter;
        lastView.setSelected(true);
        maskLocation = WaterMask.DefWaterMaskParam.Location.center;
    }

    @Override
    public WaterMask.WaterMaskParam onDraw() {
        WaterMask.WaterMaskParam param = new WaterMask.WaterMaskParam();
        param.txt.add("我是一个小标题");
        param.txt.add(binding.edt.getText().toString().trim());
        param.location = maskLocation;
        param.itemCount = 30;
        return param;
    }

    @Override
    public void onChoose(ArrayList<String> photos) {
        uris = photos;
        Glide.with(MainActivity.this).load(photos.get(0)).placeholder(R.mipmap.ic_launcher).centerCrop().error(R.mipmap.ic_launcher)
                .crossFade().into(binding.img);
    }

    public class Presenter {
        public void onCapture() {
            waterMaskHelper.startCapture();
        }

        public void onPreview() {
            if (uris == null)
                return;
            PhotoPreview.builder().setPhotos(uris).setCurrentItem(0).setShowDeleteButton(false)
                    .start(MainActivity.this);

        }

        public void onTypeSelect(View view, int location) {
            if (lastView != view) {
                lastView.setSelected(false);
                lastView = view;
                lastView.setSelected(true);
            }
            maskLocation = location;
        }

    }
}
