package com.jph.imageedittext.sample;

import com.jph.imageedittext.INetPic;

/**
 * Created by jph on 2017/4/6.
 */
public class NetPic implements INetPic {
    private String url;

    public NetPic(String url) {
        this.url = url;
    }

    @Override
    public String getXUrl() {
        return url;
    }
}
