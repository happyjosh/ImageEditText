package com.jph.imageedittext.sample;

import com.jph.imageedittext.ILocalPic;

/**
 * Created by jph on 2017/3/17.
 */
public class LocalPic implements ILocalPic {

    private String path;

    public LocalPic(String path) {
        this.path = path;
    }

    @Override
    public String getXPath() {
        return path;
    }
}
