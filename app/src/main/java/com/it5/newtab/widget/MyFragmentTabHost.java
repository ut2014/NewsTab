package com.it5.newtab.widget;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * Created by IT5 on 2016/10/25.
 */

public class MyFragmentTabHost extends FragmentTabHost{
    private String mCurrentTag;
    private String mNoTabChangedTag;

    public MyFragmentTabHost(Context context) {
        super(context);
    }

    public MyFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(mNoTabChangedTag)) {
            setCurrentTabByTag(mCurrentTag);
        }else {
            super.onTabChanged(tabId);
            mCurrentTag=tabId;
        }
    }

    //设置没有切换的TAB页
    public void setNoTabChangedTag(String tab){
        this.mNoTabChangedTag = tab;
    }
}
