package com.it5.newtab.viewpagerfragm.old_frag;

import android.os.Bundle;

import com.it5.newtab.R;
import com.it5.newtab.adapter.ViewPageFragmentAdapter;
import com.it5.newtab.interf.OnTabReselectListener;
import com.it5.newtab.old_been.BlogList;
import com.it5.newtab.old_been.NewsList;
import com.it5.newtab.viewpagerfragm.scroll_bottom.ScrollBottomFragment;

/**
 * 资讯viewpager页面
 * Created by IT5 on 2016/10/26.
 */

public class NewsViewPagerFragment extends BaseViewPagerFragment implements OnTabReselectListener{

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title=getResources().getStringArray(R.array.news_viewpage_arrays);
        adapter.addTab(title[0],"news",NewsFragment.class,getBundle(NewsList.CATALOG_ALL));
        adapter.addTab(title[1],"news_week",NewsFragment.class,getBundle(NewsList.CATALOG_WEEK));
        adapter.addTab(title[2],"latest_blog",ScrollBottomFragment.class,getBundle(BlogList.CATALOG_LATEST));
        adapter.addTab(title[3],"recommend_blog",BlogFragment.class,getBundle(BlogList.CATALOG_RECOMMEND));

       /* adapter.addTab(title[4],"recommend_blog2",BlogFragment.class,getBundle(BlogList.CATALOG_RECOMMEND));
        adapter.addTab(title[5],"recommend_blog3",BlogFragment.class,getBundle(BlogList.CATALOG_RECOMMEND));*/
    }


    private Bundle getBundle(int newType){
        Bundle bundle=new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG,newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(5);
    }

    /**
     * 基类会根据不同的catalog展示相应的数据
     *
     * @param catalog
     *            要显示的数据类别
     * @return
     */
    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString(BlogFragment.BUNDLE_BLOG_TYPE, catalog);
        return bundle;
    }

    @Override
    public void onTabReselect() {

    }
}
