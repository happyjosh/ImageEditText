package com.jph.imageedittext;

import android.text.Spannable;

import java.util.Comparator;

/**
 * 对Span按在文本中的顺序进行排序，默认取出来是按插入的先后顺序
 * Created by jph on 2017/4/17.
 */
public class SpanComparator implements Comparator<ImageEditText.ISpan> {
    private Spannable spannable;

    public SpanComparator(Spannable spannable) {
        this.spannable = spannable;
    }

    @Override
    public int compare(ImageEditText.ISpan o1, ImageEditText.ISpan o2) {
        return spannable.getSpanStart(o1) - spannable.getSpanStart(o2);
    }
}
