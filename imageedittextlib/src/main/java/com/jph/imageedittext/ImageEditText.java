package com.jph.imageedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 可直接显示图片和附件的EditText
 * 因EditText滑动无弹性,使用时需在外层嵌套ScrollView,可使用fillViewPort设置铺满
 * Created by jph on 2017/3/13.
 */
public abstract class ImageEditText extends EditText {
    public ImageEditText(Context context) {
        super(context);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        setMovementMethod(new ScrollingMovementMethod());
    }

    public void insertNetImage(INetPic netPic) {
        NetPicSpan placeImageSpan = new NetPicSpan(new BitmapDrawable(getResources(),
                (Bitmap) null), netPic);//为加载图片先占位
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = createNetPicSpannable(placeImageSpan);

        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable editable = getEditableText();

        if (!selectionStartInLine()) {
            editable.insert(index, "\n");
            index++;
        }
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        editable.insert(index + spannableString.length(), "\n");

        loadImage(placeImageSpan);//异步加载图片
    }

    /**
     * 复写调用异步加载图片,加载成功后调用onNetImageLoaded替换占位的span
     *
     * @param placeImageSpan
     */
    public abstract void loadImage(final NetPicSpan placeImageSpan);

    /**
     * 图片异步加载成功后手动调用
     *
     * @param drawable
     * @param placeImageSpan
     */
    public void onNetImageLoaded(Drawable drawable, NetPicSpan placeImageSpan) {
        NetPicSpan imageSpan = new NetPicSpan(compressDrawable(drawable),
                placeImageSpan.getNetPic());

        onNetImageLoaded(imageSpan, placeImageSpan);
    }

    /**
     * 图片异步加载成功后手动调用
     *
     * @param b
     * @param placeImageSpan
     */
    public void onNetImageLoaded(Bitmap b, NetPicSpan placeImageSpan) {
        NetPicSpan imageSpan = new NetPicSpan(getContext(), compressBitmap(b), placeImageSpan.getNetPic());

        onNetImageLoaded(imageSpan, placeImageSpan);
    }

    /**
     * 加载出来的图片Span替换掉占位的Span
     *
     * @param newImageSpan
     * @param placeImageSpan
     */
    private void onNetImageLoaded(NetPicSpan newImageSpan, NetPicSpan placeImageSpan) {
        SpannableString spannableString = new SpannableString(newImageSpan.getReplaceCode());
        spannableString.setSpan(newImageSpan, 0, newImageSpan.getReplaceCode().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Editable editable = getEditableText();
        int start = editable.getSpanStart(placeImageSpan);
        int end = editable.getSpanEnd(placeImageSpan);
        getEditableText().replace(start, end, spannableString);
        editable.removeSpan(placeImageSpan);
    }


    public void insertLocalImage(ILocalPic pic) {
        SpannableString spannableString = createLocalPicSpannable(pic);
        if (spannableString == null) {
            return;
        }

        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable editable = getEditableText();

        if (!selectionStartInLine()) {
            editable.insert(index, "\n");
            index++;
        }
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        editable.insert(index + spannableString.length(), "\n");
    }

    public void insertExtra(IExtra extra) {
        SpannableString spannableString = createExtraSpannable(extra);

        // 将选择的图片追加到EditText中光标所在位置
        int index = getSelectionStart(); // 获取光标所在位置
        Editable editable = getEditableText();

        if (!selectionStartInLine()) {
            editable.insert(index, "\n");
            index++;
        }
        if (index < 0 || index >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(index, spannableString);
        }
        editable.insert(index + spannableString.length(), "\n");
    }

    private SpannableString createNetPicSpannable(ISpan placeImageSpan) {
        SpannableString spannableString = new SpannableString(placeImageSpan.getReplaceCode());
        spannableString.setSpan(placeImageSpan, 0, placeImageSpan.getReplaceCode().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString createLocalPicSpannable(ILocalPic pic) {
        Bitmap loadedImage = BitmapFactory.decodeFile(pic.getXPath());
        if (loadedImage == null) {
            return null;
        }

        loadedImage = compressBitmap(loadedImage);


        ISpan imageSpan = new LocalPicSpan(getContext(), loadedImage, pic);
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString(imageSpan.getReplaceCode());
        spannableString.setSpan(imageSpan, 0, imageSpan.getReplaceCode().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString createExtraSpannable(IExtra extra) {
        ISpan extraSpan = createExtraSpan(extra);
        String replaceCode = extraSpan.getReplaceCode();
        SpannableString spannableString = new SpannableString(replaceCode);
        spannableString.setSpan(extraSpan, 0, replaceCode.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 可复写该方法自定义ExtraSpan来控制附件的样式
     *
     * @param extra
     * @return
     */
    protected ISpan createExtraSpan(IExtra extra) {
        return new ExtraSpan(extra);
    }

    /**
     * 设置分散的内容
     *
     * @param list
     */
    public void setPatches(List list) {
        if (list == null) {
            return;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        final List<NetPicSpan> placeSpanList = new ArrayList<>();
        for (Object patch :
                list) {
            if (patch instanceof INetPic) {
                INetPic netPic = (INetPic) patch;
                Drawable d = new BitmapDrawable(getResources(),
                        (Bitmap) null);
//                d.setBounds(0, 0, 1, 400);
                NetPicSpan placeImageSpan = new NetPicSpan(d, netPic);//为加载图片先占位
                ssb.append("\n");
                ssb.append(createNetPicSpannable(placeImageSpan));
                placeSpanList.add(placeImageSpan);
//                loadImage(placeImageSpan, netPic);
            } else if (patch instanceof ILocalPic) {
                SpannableString localSS = createLocalPicSpannable((ILocalPic) patch);
                if (localSS == null) {
                    continue;
                }
                ssb.append("\n");
                ssb.append(localSS);
            } else if (patch instanceof IExtra) {
                ssb.append("\n");
                ssb.append(createExtraSpannable((IExtra) patch));
            } else if (patch instanceof String) {
                ssb.append("\n");
                ssb.append((String) patch);
            }
        }

        setText(ssb);

        //内容追加到输入框内后才开始加载图片
        for (NetPicSpan placeSpan :
                placeSpanList) {
            loadImage(placeSpan);
        }

    }

    /**
     * 得到所有小块(文字，附件，本地图片)的集合
     *
     * @return 返回集合中的数据类型可能会有String, ILocalPic, IExtra
     */
    public List getPatches() {
        Editable editable = getText();
        //得到所有不是普通文字的数据
        ISpan[] ss = editable.getSpans(0, editable.length(), ISpan.class);

        if (editable.length() == 0) {
            //无内容
            return null;
        }
        List list = new ArrayList<>();
        if (ss.length == 0) {
            //只有普通文字内容
            list.add(editable.toString());
            return list;
        }

        int previousEnd = -1;//上一个碎片的结束位置
        for (int i = 0; i < ss.length; i++) {
            ISpan s = ss[i];
            int start = editable.getSpanStart(s);
            int end = editable.getSpanEnd(s);
            if (i == 0 && start > 0) {
                //第一个碎片&&它前面有文字
                //会为非位子碎片加上换行符
                String patch = trimEndEnter(editable.subSequence(0, start).toString());
                if (!TextUtils.isEmpty(patch)) {
                    //非文字碎片和碎片之间的换行去除换行符会为""
                    list.add(patch);
                }
            } else if (i != 0 && start > previousEnd) {
                //不是第一个碎片&&且和上一个碎片之间有普通文字
                String patch = trimEnter(editable.subSequence(previousEnd, start).toString());
                if (!TextUtils.isEmpty(patch)) {
                    list.add(patch);
                }
            }

            addSpanContent2List(s, list);

            if (i == ss.length - 1 && end < editable.length()) {
                //最后一个碎片&&它后面有文字
                String patch = trimStartEnter(editable.subSequence(end, editable.length()).toString());
                if (!TextUtils.isEmpty(patch)) {
                    list.add(patch);
                }
            }
            previousEnd = end;
        }

        return list;
    }

    /**
     * 将span的内容加入list
     *
     * @param span
     * @param list
     */
    private void addSpanContent2List(ISpan span, List list) {
        if (span instanceof NetPicSpan) {
            list.add(((NetPicSpan) span).getNetPic());
        } else if (span instanceof LocalPicSpan) {
            list.add(((LocalPicSpan) span).getLocalPic());
        } else if (span instanceof ExtraSpan) {
            list.add(((ExtraSpan) span).getExtra());
        }
    }

    /**
     * 去除开始和结尾的换行符
     *
     * @param str
     * @return
     */
    private String trimEnter(String str) {
        return trimEndEnter(trimStartEnter(str));
    }

    /**
     * 去除开始的换行符
     *
     * @param str
     * @return
     */
    private String trimStartEnter(String str) {
        if (str.startsWith("\n")) {
            return str.substring(1, str.length());
        }

        return str;
    }

    /**
     * 去除结尾的换行符
     *
     * @param str
     * @return
     */
    private String trimEndEnter(String str) {
        if (str.endsWith("\n")) {
            return str.substring(0, str.length() - 1);
        }

        return str;
    }

    /**
     * 限制图片尺寸小于屏幕宽度
     *
     * @param loadedImage
     * @return
     */
    private Bitmap compressBitmap(Bitmap loadedImage) {
        int width = loadedImage.getWidth();

        if (width > getWidth()) {
            //超出了控件宽度需要缩放
            float scaleWidth = ((float) getWidth()) / width;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, width, loadedImage.getHeight(), matrix, true);
        }

        return loadedImage;
    }

    private Drawable compressDrawable(Drawable d) {
        int width = d.getIntrinsicWidth();
        int height = d.getIntrinsicHeight();
        if (width > getWidth()) {
            float ratio = ((float) height) / width;
            //超出了控件宽度需要缩放
            width = getWidth();
            height = (int) (getWidth() * ratio);
        }
        d.setBounds(0, 0, width, height);
        return d;
    }

    /**
     * 光标是否在行的第一位
     *
     * @return
     */
    protected boolean selectionStartInLine() {
        if (length() <= 0) {
            return true;
        }

        int selectionStart = getSelectionStart();
        if (selectionStart <= 0) {
            return true;
        }
        return "\n".equals(getText().subSequence(selectionStart - 1, selectionStart).toString());
    }

    public interface ISpan {
        /**
         * 获取占位的文字
         *
         * @return
         */
        String getReplaceCode();
    }
}
