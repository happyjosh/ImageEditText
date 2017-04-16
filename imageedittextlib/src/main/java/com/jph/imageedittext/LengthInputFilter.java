package com.jph.imageedittext;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;

import java.util.Arrays;

/**
 * 因为默认计算的是span占位的字符的长度，此类将span算作一个字符来计算
 * Created by jph on 2017/4/16.
 */
public class LengthInputFilter implements InputFilter {

    private final int mMax;

    public LengthInputFilter(int max) {
        mMax = max;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                               int dstart, int dend) {
        int max = getRealMax(source, dest);
        int keep = max - (dest.length() - (dend - dstart));
        int sourceMinus = getSpansMinus(source);
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start - sourceMinus) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
//            return source.subSequence(start, keep);
            CharSequence c = subSource(source, start, keep);
//            CharSequence c = source.subSequence(start, keep);
            return c;
        }
    }

    /**
     * @return the maximum length enforced by this input filter
     */
    public int getMax() {
        return mMax;
    }

    private int getRealMax(CharSequence source, Spanned dest) {

        int minus = 0;
//        minus += getSpansMinus(source);
        minus += getSpansMinus(dest);

        return mMax + minus;
    }

    /**
     * 得到字符串中的所有span
     *
     * @param s
     * @return
     */
    private ImageEditText.ISpan[] getSpans(CharSequence s) {
        if (s instanceof Spannable) {
            return ((Spannable) s).getSpans(0, s.length(),
                    ImageEditText.ISpan.class);
        }

        return null;
    }

    /**
     * 得到字符串中所有Span的length差值
     *
     * @param s
     * @return
     */
    private int getSpansMinus(CharSequence s) {
        if (s == null) {
            return 0;
        }
        return getSpansMinus(getSpans(s));
    }

    /**
     * 得到所有Span的length差值
     *
     * @param spans
     * @return
     */
    private int getSpansMinus(ImageEditText.ISpan[] spans) {
        if (spans == null) {
            return 0;
        }

        int minus = 0;
        for (ImageEditText.ISpan span :
                spans) {
            minus += span.getReplaceCode().length() - 1;
        }

        return minus;
    }

    /**
     * 截取插入的字符串，处理span的问题。可能截取到span对应的字符串中间部分
     *
     * @param source
     * @param start
     * @param keep
     */
    private CharSequence subSource(CharSequence source, int start, int keep) {
        if (source == null) {
            return null;
        }

        if (source instanceof Spannable) {
            return subSource((Spannable) source, start, keep);
        }
        return source.subSequence(start, keep);
    }

    private CharSequence subSource(Spannable source, int start, int keep) {
        ImageEditText.ISpan[] spans = getSpans(source);
        Arrays.sort(spans, new SpanComparator(source));
        if (spans == null) {
            return source.subSequence(start, keep);
        }
        int realKeep = keep;
        for (ImageEditText.ISpan span :
                spans) {
            int beginIndex = source.getSpanStart(span);
            int endIndex = source.getSpanEnd(span);
            if (beginIndex < realKeep) {
                //可截取内容包含了该span或者span的部分内容
                realKeep += span.getReplaceCode().length() - 1;
                if (endIndex > realKeep) {
                    //结束的位置超出了限制，代表span从中间被截取了
                    source.removeSpan(span);
                    return source.subSequence(start, beginIndex);
                }
            }
        }
        return source.subSequence(start, realKeep);
    }
}
