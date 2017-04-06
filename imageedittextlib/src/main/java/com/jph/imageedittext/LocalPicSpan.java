package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;

/**
 * 图片占位
 * Created by jph on 2017/3/17.
 */
public class LocalPicSpan extends ImageSpan implements ImageEditText.ISpan {

    private ILocalPic mLocalPic;

    public LocalPicSpan(Context context, Bitmap b, ILocalPic localPic) {
        super(context, b);
        mLocalPic = localPic;
    }

    @Override
    public String getReplaceCode() {
        return "<local_img src=\"" + mLocalPic.getXPath() + "\" />";
    }

    public ILocalPic getLocalPic() {
        return mLocalPic;
    }
}
