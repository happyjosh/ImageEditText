package com.jph.imageedittext;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换出PatchesSSB
 * Created by jph on 2017/6/2.
 */
public class PatchesSSBTransformer {
    private ImageEditText mImageEditText;
    private List list;

    public PatchesSSBTransformer(ImageEditText imageEditText, List list) {
        mImageEditText = imageEditText;
        this.list = list;
    }

    public PatchesSSB getPatchesSSB() {
        if (list == null) {
            return null;
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        final List<NetPicSpan> placeSpanList = new ArrayList<>();
        for (int i = 0, c = list.size(); i < c; i++) {
            Object patch = list.get(i);
            if (patch instanceof INetPic) {
                INetPic netPic = (INetPic) patch;
                Drawable d = new BitmapDrawable(mImageEditText.getResources(),
                        (Bitmap) null);
//                d.setBounds(0, 0, 1, 400);
                NetPicSpan placeImageSpan = new NetPicSpan(d, netPic);//为加载图片先占位
                insertBetweenPatches(i, ssb);
                ssb.append(mImageEditText.createNetPicSpannable(placeImageSpan));
                placeSpanList.add(placeImageSpan);
//                loadImage(placeImageSpan, netPic);
            } else if (patch instanceof ILocalPic) {
                SpannableString localSS = mImageEditText.createLocalPicSpannable((ILocalPic) patch);
                if (localSS == null) {
                    continue;
                }
                insertBetweenPatches(i, ssb);
                ssb.append(localSS);
            } else if (patch instanceof IExtra) {
                insertBetweenPatches(i, ssb);
                ssb.append(mImageEditText.createExtraSpannable((IExtra) patch));
            } else if (patch instanceof String) {
                insertBetweenPatches(i, ssb);
                ssb.append((String) patch);
            }
        }

        return new PatchesSSB(ssb, placeSpanList);
    }

    /**
     * 在两个块之间插入换行
     *
     * @param i
     * @param ssb
     */
    private void insertBetweenPatches(int i, SpannableStringBuilder ssb) {
        if (i > 0) {
            //内容的开始位置不需要换行
            Object previousPatch = list.get(i - 1);
            if (previousPatch instanceof ILocalPic || previousPatch instanceof INetPic) {
                //图片后插入多一个换行
                ssb.append("\n\n");
            } else {
                ssb.append("\n");
            }
        }
    }

}
