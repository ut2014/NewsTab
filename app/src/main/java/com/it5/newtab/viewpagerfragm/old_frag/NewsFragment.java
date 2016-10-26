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

public class NewsFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView text=new TextView(AppContext.context());
        text.setText("newsfragment");
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
