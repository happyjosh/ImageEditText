package com.jph.imageedittext;

import android.text.SpannableStringBuilder;

import java.util.List;

/**
 * 组合所有patches内容成一个SpannableStringBuilder的Model,包含了占位的网络图片集合
 * Created by jph on 2017/5/12.
 */
public class PatchesSSB {

    private SpannableStringBuilder ssb;
    private List<NetPicSpan> placeSpanList;

    public PatchesSSB(SpannableStringBuilder ssb, List<NetPicSpan> placeSpanList) {
        this.ssb = ssb;
        this.placeSpanList = placeSpanList;
    }

    public SpannableStringBuilder getSsb() {
        return ssb;
    }

    public List<NetPicSpan> getPlaceSpanList() {
        return placeSpanList;
    }
}
