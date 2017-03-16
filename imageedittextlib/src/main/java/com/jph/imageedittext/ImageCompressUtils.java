package com.jph.imageedittext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片压缩工具类
 * Created by jph on 2017/3/13.
 */
public class ImageCompressUtils {

    /**
     * 压缩图片质量
     *
     * @param bitmap
     * @param quality
     * @return
     */
    public static Bitmap compressToBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 压缩尺寸
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap compressToBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int widthRatio = maxWidth < width ? width / maxWidth : 1;
        int heightRatio = maxHeight < height ? height / maxHeight : 1;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = Math.max(widthRatio, heightRatio);


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        return BitmapFactory.decodeStream(is, null, options);
    }

    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {
            //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 20, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        image.recycle();
        image = null;

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, null);
        if (pixelW >= bitmap.getWidth() && pixelH >= bitmap.getHeight()) {
            //不需要压缩尺寸
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        newOpts.inJustDecodeBounds = false;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
        if (ww > 0 && ww < w) {
            be = (int) (w / ww);
        }
        if (hh > 0 && hh < h) {
            be = Math.min((int) (h / hh), be);
        }

        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        try {
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap newBitmap = BitmapFactory.decodeStream(is, null, newOpts);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        bitmap = null;

        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return newBitmap;
    }
}
