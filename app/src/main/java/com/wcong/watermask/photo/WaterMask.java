package com.wcong.watermask.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangcong on 2017/3/17.
 * <p>
 */

public class WaterMask {

    /**
     * 绘制水印
     *
     * @param context
     * @param bitmap         原图
     * @param path           保存路径
     * @param waterMaskParam 水印相关参数
     */
    public static void draw(Context context, Bitmap bitmap, String path,
                            WaterMaskParam waterMaskParam) {
        if (waterMaskParam == null || waterMaskParam.txt == null)
            return;
        saveJPGE_After(drawTxt(context, bitmap, waterMaskParam), path);
        context.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
    }

    /**
     * 绘制水印
     *
     * @param context
     * @param bitmap
     * @param waterMaskParam
     * @return
     */
    private static Bitmap drawTxt(Context context, Bitmap bitmap, WaterMaskParam waterMaskParam) {
        int maxHeight = 0;
        List<List<String>> msg = new ArrayList<>();
        for (String str : waterMaskParam.txt) {
            int count = str.length() / waterMaskParam.itemCount;
            if (count == 0) {
                maxHeight++;
                List<String> list = new ArrayList<>();
                list.add(str);
                msg.add(list);
            } else {
                if (str.length() % waterMaskParam.itemCount != 0) {
                    count++;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String s = str.substring(i * waterMaskParam.itemCount,
                            i == count - 1 ? str.length() : (i + 1) * waterMaskParam.itemCount);
                    list.add(s);
                }
                msg.add(list);
                maxHeight += count;
            }
        }
        int txtSize = bitmap.getWidth() / waterMaskParam.itemCount;
        int index = msg.size() - 1;
        bitmap = checkBackground(waterMaskParam.location, bitmap, msg.size() * txtSize * 2.0f);
        for (List<String> strings : msg) {
            for (int i = 0; i < strings.size(); i++) {
                bitmap = checkType(context, bitmap, strings.get(i), txtSize,
                        waterMaskParam, txtSize * (maxHeight--) + index * txtSize / 2);
            }
            index--;
        }
        return bitmap;
    }

    private static Bitmap checkBackground(int location, Bitmap bitmap, float maxHeight) {
        if (location == DefWaterMaskParam.Location.left_bottom || location == DefWaterMaskParam.Location.right_bottom)
            return drawBackground(bitmap, maxHeight);
        return bitmap;
    }

    /**
     * 绘制背景色
     *
     * @param bitmap
     * @param maxHeight 水印最大高度
     * @return
     */
    private static Bitmap drawBackground(Bitmap bitmap, float maxHeight) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint_b = new Paint();
        paint_b.setDither(true);
        paint_b.setFilterBitmap(true);
        paint_b.setColor(Color.BLACK);
        paint_b.setDither(true);
        paint_b.setFilterBitmap(true);
        paint_b.setAlpha(100);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawRect(0, newBitmap.getHeight() - maxHeight, newBitmap.getWidth(),
                newBitmap.getHeight(), paint_b);
        return newBitmap;
    }

    private static Bitmap checkType(Context context, Bitmap bitmap, String str, int txtSize, WaterMaskParam waterMaskParam, int padding) {
        if (waterMaskParam.location == DefWaterMaskParam.Location.left_top)
            return ImageUtil.drawTextToLeftTop(context, bitmap, str, txtSize,
                    waterMaskParam.txtColor, 10, padding);
        else if (waterMaskParam.location == DefWaterMaskParam.Location.right_top)
            return ImageUtil.drawTextToRightTop(context, bitmap, str, txtSize,
                    waterMaskParam.txtColor, 10, padding);
        else if (waterMaskParam.location == DefWaterMaskParam.Location.left_bottom)
            return ImageUtil.drawTextToLeftBottom(context, bitmap, str, txtSize,
                    waterMaskParam.txtColor, 10, padding);
        else if (waterMaskParam.location == DefWaterMaskParam.Location.right_bottom)
            return ImageUtil.drawTextToRightBottom(context, bitmap, str, txtSize,
                    waterMaskParam.txtColor, 10, padding);
        else
            return ImageUtil.drawTextToCenter(context, bitmap, str, txtSize,
                    waterMaskParam.txtColor);
    }

    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class DefWaterMaskParam {
        private static final String TEXT = "";
        private static final int ITEM_COUNT = 20;
        private static final int TEXT_COLOR = Color.WHITE;

        public static final class Location {
            public static int left_top = 0;
            public static int right_top = 1;
            public static int center = 2;
            public static int left_bottom = 3;
            public static int right_bottom = 4;
        }
    }

    public static class WaterMaskParam {
        public List<String> txt = new ArrayList<>(); //水印文字
        public int itemCount = DefWaterMaskParam.ITEM_COUNT; //每行的文字数
        public int txtColor = DefWaterMaskParam.TEXT_COLOR; //文字颜色
        public int location = DefWaterMaskParam.Location.left_bottom; //水印位置

        public WaterMaskParam() {
        }

        public WaterMaskParam(List<String> txt) {
            this.txt = txt;
        }

        public WaterMaskParam(List<String> txt, int itemCount, int txtColor) {
            this.txt = txt;
            this.itemCount = itemCount;
            this.txtColor = txtColor;
        }
    }

    public interface WaterMaskListener {
        WaterMaskParam onDraw();
    }

}
