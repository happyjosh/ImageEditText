package com.jph.imageedittext.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jph.imageedittext.ExtraSpan;
import com.jph.imageedittext.IExtra;
import com.jph.imageedittext.INetPic;
import com.jph.imageedittext.ImageEditText;
import com.jph.imageedittext.NetPicSpan;

/**
 * Created by jph on 2017/3/18.
 */
public class CustomImageEditText extends ImageEditText {
    public CustomImageEditText(Context context) {
        super(context);
    }

    public CustomImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ISpan createExtraSpan(IExtra extra) {
        return new CustomExtraSpan(extra);
    }

    public class CustomExtraSpan extends ExtraSpan {
        public CustomExtraSpan(IExtra extra) {
            super(extra);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }

        @Override
        protected int getTextSize() {
            return getResources().getDimensionPixelSize(R.dimen.text_size);
        }

        @Override
        protected int getTextColor() {
            return Color.BLUE;
        }
    }

    @Override
    public void loadImage(final NetPicSpan placeImageSpan) {
        final INetPic netPic = placeImageSpan.getNetPic();
        Glide.with(getContext()).load(netPic.getXUrl()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                onNetImageLoaded(resource, placeImageSpan);
//                onNetImageLoaded(drawableToBitmap(resource), placeImageSpan);
            }
        });
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;

    }
}
