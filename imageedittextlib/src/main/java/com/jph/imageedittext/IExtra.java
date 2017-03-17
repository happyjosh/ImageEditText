package com.jph.imageedittext;

/**
 * 附件model需要实现的接口，接口方法命名加上X前缀，避免和实际实现Model的get方法冲突
 * Created by jph on 2017/3/17.
 */
public interface IExtra {

    String getXId();

    String getXType();

    String getXTitle();

}
