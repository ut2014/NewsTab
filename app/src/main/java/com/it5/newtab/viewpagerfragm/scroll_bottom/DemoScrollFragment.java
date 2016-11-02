package com.it5.newtab.viewpagerfragm.scroll_bottom;

import android.view.View;
import android.widget.AdapterView;

import com.it5.newtab.api.OSChinaApi;
import com.it5.newtab.old_been.ListBaseAdapter;
import com.it5.newtab.old_been.NewsList;
import com.it5.newtab.widget.EmptyLayout;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IT5 on 2016/11/2.
 */

public class DemoScrollFragment extends ScrollBottomFragment{
    private static final String CACHE_KEY_PREFIX = "newslist_";

    @Override
    protected ScrollBottomAdapter getListAdapter() {
        return new ScrollBottomAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected String parseList(InputStream is) throws Exception {

        return is.toString();
    }

    @Override
    protected List<String> readList(Serializable seri) {
        for (int i=0;i<15;i++) {
            mDatas.add("demo"+i);
        }
        return mDatas;
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getNewsList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    protected void executeOnLoadDataSuccess(List<String> data) {
        if (mCatalog == NewsList.CATALOG_WEEK
                || mCatalog == NewsList.CATALOG_MONTH) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (mState == STATE_REFRESH)
                mAdapter.clear();
            mAdapter.addData(data);
            mState = STATE_NOMORE;
            mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
            return;
        }
        super.executeOnLoadDataSuccess(data);
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新资讯两小时刷新一次
        if (mCatalog == NewsList.CATALOG_ALL) {

            return 2 * 60 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
