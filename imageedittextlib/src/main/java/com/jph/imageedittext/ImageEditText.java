package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 可直接显示图片的EditText
 * Created by jph on 2017/3/13.
 */
public class ImageEditText extends EditText {
    public ImageEditText(Context context) {
        super(context);
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void insertLocalImage(String path) {
        // 根据Bitmap对象创建ImageSpan对象
        // 计算缩放比例
        Bitmap loadedImage = BitmapFactory.decodeFile(path);
        if (loadedImage == null) {
            return;
        }

        Bitmap scaleBitmap = ImageCompressUtils.ratio(loadedImage, getWidth(), loadedImage.getHeight());
        loadedImage.recycle();
        loadedImage = null;

        int width = scaleBitmap.getWidth();

        Bitmap showBitmap = null;
        if (width > getWidth()) {
            int height = scaleBitmap.getHeight();

            float scaleWidth = ((float) getWidth()) / width;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            showBitmap = Bitmap.createBitmap(scaleBitmap, 0, 0, width, height, matrix, true);
            scaleBitmap.recycle();
            scaleBitmap = null;
        } else {
            showBitmap = scaleBitmap;
        }

        ImageSpan imageSpan = new ImageSpan(getContext(), showBitmap);
//        showBitmap.recycle();
        showBitmap = null;
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        String tempUrl = "<img src=\"" + path + "\" />";
        SpannableString spannableString = new SpannableString(tempUrl);
        // 用ImageSpan对象替换你指定的字符串
        spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable edit_text = getEditableText();
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
        edit_text.insert(index + spannableString.length(), "\n");

//        System.gc();
    }

    @Override
    public boolean bringPointIntoView(int offset) {
//        if ("onPreDraw".equals(new Throwable().getStackTrace()[0].getMethodName())) {
//            return true;
//        }
        return super.bringPointIntoView(offset);
    }

    @Override
    public boolean onPreDraw() {
        return super.onPreDraw();
    }
}
