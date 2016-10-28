package com.it5.newtab.viewpagerfragm.old_frag;

import android.view.View;
import android.widget.AdapterView;

import com.it5.newtab.adapter.NewsAdapter;
import com.it5.newtab.api.OSChinaApi;
import com.it5.newtab.interf.OnTabReselectListener;
import com.it5.newtab.old_been.ListBaseAdapter;
import com.it5.newtab.old_been.News;
import com.it5.newtab.old_been.NewsList;
import com.it5.newtab.util.XmlUtils;
import com.it5.newtab.widget.EmptyLayout;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IT5 on 2016/10/26.
 */

public class NewsFragment extends BaseListFragment<News> implements
        OnTabReselectListener {

    protected static final String TAG = NewsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "newslist_";

    @Override
    protected NewsAdapter getListAdapter() {
        return new NewsAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatelog;
    }

    @Override
    protected NewsList parseList(InputStream is) throws Exception {

        NewsList list = null;
        try {
            list = XmlUtils.toBean(NewsList.class, is);
        } catch (NullPointerException e) {
            list = new NewsList();
        }
        return list;
    }

    @Override
    protected NewsList readList(Serializable seri) {
        return ((NewsList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getNewsList(mCatelog, mCurrentpage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        News news = mAdapter.getItem(position);
        /*if (news != null) {
            UIHelper.showNewsRedirect(view.getContext(), news);

            // 放入已读列表
            saveToReadedList(view, NewsList.PREF_READED_NEWS_LIST, news.getId()
                    + "");
        }*/
    }

    @Override
    protected void executeOnLoadDataSuccess(List<News> data) {
        if (mCatelog == NewsList.CATALOG_WEEK
                || mCatelog == NewsList.CATALOG_MONTH) {
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
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    protected long getAutoRefreshTime() {
        // 最新资讯两小时刷新一次
        if (mCatelog == NewsList.CATALOG_ALL) {

            return 2 * 60 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
