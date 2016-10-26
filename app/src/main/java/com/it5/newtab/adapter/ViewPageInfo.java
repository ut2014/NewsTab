package com.it5.newtab.adapter;

import android.os.Bundle;

/**
 * Created by IT5 on 2016/10/26.
 */

public class ViewPageInfo {
    public  String tag;
    public  Class<?> clss;
    public Bundle args;
    public String title;

    public ViewPageInfo(String title,String tag,Class<?> clss,Bundle args) {
        this.args = args;
        this.clss = clss;
        this.tag = tag;
        this.title = title;
    }
}
