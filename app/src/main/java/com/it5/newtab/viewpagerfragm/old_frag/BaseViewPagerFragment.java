package com.it5.newtab.viewpagerfragm.old_frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it5.newtab.R;
import com.it5.newtab.adapter.ViewPageFragmentAdapter;
import com.it5.newtab.widget.EmptyLayout;
import com.it5.newtab.widget.PagerSlidingTabStrip;

/**
 * 带导航条的基类
 * 添加导航条
 * Creaed by IT5 on 2016/10/26.
 */

public abstract class BaseViewPagerFragment extends BaseFragment {
    //带top导航标题栏
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;//出错提示

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_viewpage_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTabStrip=(PagerSlidingTabStrip) view
                .findViewById(R.id.pager_tabstrip);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
    }

    protected void setScreenPageLimit() {
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);

}
