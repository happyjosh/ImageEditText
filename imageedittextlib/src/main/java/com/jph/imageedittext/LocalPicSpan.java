package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;

/**
 * 图片占位
 * Created by jph on 2017/3/17.
 */
public class LocalPicSpan extends ImageSpan implements ImageEditText.ISpan {

    private ILocalPic mPic;

    public LocalPicSpan(Context context, Bitmap b, ILocalPic pic) {
        super(context, b);
        mPic = pic;
    }

    @Override
    public String getReplaceCode() {
        return "<img src=\"" + mPic.getXPath() + "\" />";
    }

    public ILocalPic getPic() {
        return mPic;
    }
}
