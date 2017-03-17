package com.jph.imageedittext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * Created by jph on 2017/3/17.
 */
public class ExtraSpan extends ReplacementSpan implements ImageEditText.ISpan {

    IExtra mExtra;

    public ExtraSpan(IExtra extra) {
        mExtra = extra;
    }

    @Override
    public String getReplaceCode() {
        return "<post id=\"" + mExtra.getXId() + "\" type=\"" + mExtra.getXType() + "\" />";
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        return (int) paint.measureText(mExtra.getXTitle());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
                     int y, int bottom, Paint paint) {
        paint.setColor(Color.RED);
        canvas.drawText(mExtra.getXTitle(), x, y, paint);
    }

    public IExtra getExtra() {
        return mExtra;
    }
}
