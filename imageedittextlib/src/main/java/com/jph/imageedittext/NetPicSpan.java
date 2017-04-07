package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by jph on 2017/4/6.
 */
public class NetPicSpan extends ImageSpan implements ImageEditText.ISpan {
    private INetPic mNetPic;

    public NetPicSpan(Drawable d, INetPic netPic) {
        super(d);
        this.mNetPic = netPic;
    }

    public NetPicSpan(Context context, Bitmap b, INetPic netPic) {
        super(context, b);
        this.mNetPic = netPic;
    }

    @Override
    public String getReplaceCode() {
        return "<net_img src=\"" + mNetPic.getXUrl() + "\" />";
    }

    public INetPic getNetPic() {
        return mNetPic;
    }
}
