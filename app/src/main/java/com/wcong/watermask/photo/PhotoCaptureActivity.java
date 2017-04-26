package com.wcong.watermask.photo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PhotoCaptureActivity extends CallBackActivity {

    private final int REQUEST_CODE = 0;

    private File imgUri;

    private String fileName = "demo";

    private String[] perms = {
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    @AfterPermissionGranted(1)
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(PhotoCaptureActivity.this, perms)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imgUri = createImageFile();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgUri));
            startActivityForResult(takePictureIntent, REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "", 1, perms);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Log.i("---success---", imgUri + "");
            ArrayList<String> strings = new ArrayList<>();
            strings.add(imgUri.toString());
            if (photoListener != null)
                photoListener.onChoose(strings);
            if (waterMarkListener != null) {
                WaterMask.WaterMaskParam maskParam = waterMarkListener.onDraw();
                Bitmap bitmap = ImageUtil.getBitmap(String.valueOf((imgUri)));
//        WaterMask.saveJPGE_After(rotateBitmapByDegree(bitmap, readPictureDegree(String.valueOf(imgUri))),String.valueOf(imgUri));
                WaterMask.draw(this, bitmap, String.valueOf((imgUri)), maskParam);
            }
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imgUri)));
        }
        finish();
    }

    private File createImageFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                fileName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("----", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = fileName + timeStamp;
        String suffix = ".jpg";
        File image = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        return image;
    }
}
