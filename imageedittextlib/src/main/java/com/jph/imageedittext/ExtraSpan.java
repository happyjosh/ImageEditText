package com.jph.imageedittext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

/**
 * Created by jph on 2017/3/17.
 */
public class ExtraSpan extends ReplacementSpan implements ImageEditText.ISpan {

    protected IExtra mExtra;

    public ExtraSpan(IExtra extra) {
        mExtra = extra;
    }

    @Override
    public String getReplaceCode() {
        return "<extra id=\"" + mExtra.getXId() + "\" type=\"" + mExtra.getXType() + "\" title=\"" + mExtra.getXTitle() + "\" />";
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        if (getTextSize() > 0) {
            paint.setTextSize(getTextSize());
        }
        return (int) paint.measureText(mExtra.getXTitle());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
                     int y, int bottom, Paint paint) {
        if (getTextSize() > 0) {
            paint.setTextSize(getTextSize());
        }
        paint.setColor(getTextColor());
        canvas.drawText(mExtra.getXTitle(), x, y, paint);
    }

    protected int getTextSize() {
        return 0;
    }

    protected int getTextColor() {
        return Color.RED;
    }

    public IExtra getExtra() {
        return mExtra;
    }


}
