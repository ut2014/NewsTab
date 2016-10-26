package com.it5.newtab.viewpagerfragm.old_frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.it5.newtab.AppContext;

/**
 * Created by IT5 on 2016/10/26.
 */

public class BlogFragment extends BaseFragment{
    public static final String BUNDLE_BLOG_TYPE = "BUNDLE_BLOG_TYPE";
    protected static final String TAG = BlogFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "bloglist_";
    private String blogType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView text=new TextView(AppContext.context());
        text.setText("blogfragment");
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
