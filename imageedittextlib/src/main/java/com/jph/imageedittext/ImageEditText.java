package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 可直接显示图片的EditText
 * Created by jph on 2017/3/13.
 */
public class ImageEditText extends EditText {
    public ImageEditText(Context context) {
        super(context);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setMovementMethod(new ScrollingMovementMethod());
    }

    public void insertLocalImage(ILocalPic pic) {
        Bitmap loadedImage = BitmapFactory.decodeFile(pic.getXPath());
        if (loadedImage == null) {
            return;
        }

        int width = loadedImage.getWidth();

        if (width > getWidth()) {
            //超出了控件宽度需要缩放
            float scaleWidth = ((float) getWidth()) / width;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, loadedImage.getHeight(), matrix, true);
        }

        ISpan imageSpan = new LocalPicSpan(getContext(), loadedImage, pic);
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString(imageSpan.getReplaceCode());
        spannableString.setSpan(imageSpan, 0, imageSpan.getReplaceCode().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable editable = getEditableText();
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        editable.insert(index + spannableString.length(), "\n");
    }

    public void insertExtra(IExtra extra) {
        ISpan extraSpan = new ExtraSpan(extra);
        String replaceCode = extraSpan.getReplaceCode();
        SpannableString spannableString = new SpannableString(replaceCode);
        spannableString.setSpan(extraSpan, 0, replaceCode.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable editable = getEditableText();
        editable.insert(index, "\n");
        index++;
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        editable.insert(index + spannableString.length(), "\n");
    }

    /**
     * 获取附件个数
     *
     * @return
     */
    public int getExtraSpanCount() {
        Editable editable = getText();
        ExtraSpan[] es = editable.getSpans(0, editable.length(), ExtraSpan.class);
        return es.length;
    }

    /**
     * 获取本地图片个数
     *
     * @return
     */
    public int getPicSpanCount() {
        Editable editable = getText();
        LocalPicSpan[] es = editable.getSpans(0, editable.length(), LocalPicSpan.class);
        return es.length;
    }

    /**
     * 得到所有附件
     *
     * @return
     */
    public List<IExtra> getExtras() {
        Editable editable = getText();
        ExtraSpan[] es = editable.getSpans(0, editable.length(), ExtraSpan.class);
        if (es.length == 0) {
            return null;
        }

        List<IExtra> extraList = new ArrayList<>();
        for (ExtraSpan e :
                es) {
            extraList.add(e.getExtra());
        }

        return extraList;
    }

    /**
     * 得到所有本地图片
     *
     * @return
     */
    public List<ILocalPic> getLocalPics() {
        Editable editable = getText();
        LocalPicSpan[] ps = editable.getSpans(0, editable.length(), LocalPicSpan.class);
        if (ps.length == 0) {
            return null;
        }

        List<ILocalPic> extraList = new ArrayList<>();
        for (LocalPicSpan p :
                ps) {
            extraList.add(p.getPic());
        }

        return extraList;
    }

    public interface ISpan {
        /**
         * 获取占位的文字
         *
         * @return
         */
        String getReplaceCode();
    }
}
