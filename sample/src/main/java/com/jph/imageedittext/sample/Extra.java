package com.jph.imageedittext.sample;

import com.jph.imageedittext.IExtra;

/**
 * Created by jph on 2017/3/17.
 */
public class Extra implements IExtra {

    private int id;
    private int type;
    private String title;

    public Extra(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getXId() {
        return String.valueOf(id);
    }

    @Override
    public String getXType() {
        return String.valueOf(type);
    }

    @Override
    public String getXTitle() {
        return title;
    }
}
