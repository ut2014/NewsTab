package com.it5.newtab.viewpagerfragm.scroll_bottom;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.it5.newtab.AppContext;
import com.it5.newtab.R;
import com.it5.newtab.cache.CacheManager;
import com.it5.newtab.old_been.Entity;
import com.it5.newtab.old_been.ListBaseAdapter;
import com.it5.newtab.old_been.Result;
import com.it5.newtab.old_been.ResultBean;
import com.it5.newtab.util.StringUtils;
import com.it5.newtab.util.TDevice;
import com.it5.newtab.util.ThemeSwitchUtils;
import com.it5.newtab.util.XmlUtils;
import com.it5.newtab.widget.EmptyLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by IT5 on 2016/10/28.
 */

public class ScrollBottomFragment extends Fragment implements OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;

    // 正在下拉但还没有到刷新的状态
    public static final int STATE_PRESSNONE = 4;
    public static int mState = STATE_NONE;
    private int mListViewHeight;


    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EmptyLayout mErrorLayout;
    List<String> mDatas = new ArrayList<>();
    //    ArrayAdapter mAdapter;
    ScrollBottomAdapter mAdapter;

    protected int mStoreEmptyState = -1;
    protected int mCurrentPage = 0;
    protected int mCatalog = 1;

    //错误信息
    protected Result mResult;

    private AsyncTask<String, Void, List<String>> mCacheTask;
    private ParserTask mParserTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pull_refresh_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mErrorLayout.setVisibility(View.GONE);
        initView();
    }


    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                //请求数据
                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
//        mListView.setOnClickListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                mState = STATE_NONE;
                requestData(false);
            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    //获取列表数据
    private void requestData(boolean refresh) {
        String key = getCacheKey();
        if (isReadCacheData(refresh)) {
            readCacheData(key);
        } else {
            //取新的数据
            sendRequestData();
        }

    }

    //判断是否需要读取缓存的数据
    protected boolean isReadCacheData(boolean refresh) {
        String key = getCacheKey();
        if (!TDevice.hasInternet()) {
            return true;
        }
        if (CacheManager.isExistDataCache(getActivity(), key) && !refresh
                && mCurrentPage == 0) {
            return true;
        }
        if (CacheManager.isExistDataCache(getActivity(), key) &&
                !CacheManager.isCacheDataFailure(getActivity(), key)
                && mCurrentPage != 0) {
            return true;
        }
        return false;
    }

    // 是否到时间去刷新数据了
    private boolean onTimeRefresh() {
        String lastRefreshTime = AppContext.getLastRefreshTime(getCacheKey());
        String currTime = StringUtils.getCurTimeStr();
        long diff = StringUtils.calDateDifferent(lastRefreshTime, currTime);
        return needAutoRefresh() && diff > getAutoRefreshTime();
    }

    /***
     * 自动刷新的时间
     * <p>
     * 默认：自动刷新的时间为半天时间
     *
     * @return
     */
    protected long getAutoRefreshTime() {
        return 12 * 60 * 60;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onTimeRefresh()) {
            onRefresh();
        }
    }

    protected void sendRequestData() {
    }

    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, List<String>> {
        private WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(), params[0]);
            if (seri == null) {
                return null;
            } else {
                return readList(seri);
            }

        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            if (list != null) {
                executeOnLoadDataSuccess(list);
            } else {
                excuteOnloadDataError(null);

            }
            executeOnLoadFinish();
        }
    }

    private class saveCacheTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> mContext;
        private Serializable seri;
        private String key;

        private saveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (mCurrentPage == 0 && needAutoRefresh()) {
                AppContext.putToLastRefreshTime(getCacheKey(), StringUtils.getCurTimeStr());
            }
            if (isAdded()) {
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }
                executeParserTask(responseBody);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if (isAdded()) {
                readCacheData(getCacheKey());
            }
        }
    };


    protected void executeOnLoadDataSuccess(List<String> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        if (mResult != null && !mResult.OK()) {
            AppContext.showToast(mResult.getErrorMessage());
            // 注销登陆，密码已经修改，cookie，失效了
            AppContext.getInstance().Logout();
        }

        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (mCurrentPage == 0) {
            mAdapter.clear();
        }

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(mAdapter.getData())) {
                data.remove(i);
                i--;
            }
        }
        int adapterState = ScrollBottomAdapter.STATE_EMPTY_ITEM;
        if ((mAdapter.getCount() + data.size()) == 0) {
            adapterState = ScrollBottomAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || (data.size() < getPageSize() && mCurrentPage == 0)) {
            adapterState = ScrollBottomAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ScrollBottomAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
        mAdapter.addData(data);

        // 判断等于是因为最后有一项是listview的状态
        if (mAdapter.getCount() == 1) {

            if (needShowEmptyNoData()) {
                mErrorLayout.setErrorType(EmptyLayout.NODATA);
            } else {
                mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    //是否需要隐藏listview，显示无数据状态
    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getPageSize() {
        return AppContext.PAGE_SIZE;
    }

    protected void onRefreshNetworkSuccess() {
    }

    protected void excuteOnloadDataError(String error) {
        if (mCurrentPage==0&&!CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }else {
            //在无网络时，滚动到底部时，mCurrentPage先自加了，然而在失败时却
            //没有减回来，如果刻意在无网络的情况下上拉，可以出现漏页问题
            //find by TopJohn
            mCurrentPage--;
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    /** 设置顶部正在加载的状态 */
    protected void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    protected void executeOnLoadFinish(){
        setSwipeRefreshLoadedState();
        mState=STATE_NONE;
    }

    protected ScrollBottomAdapter getListAdapter() {
        return new ScrollBottomAdapter();
    }

    private void executeParserTask(byte[] data){
        cancelParserTask();
        mParserTask=new ParserTask(data);
        mParserTask.execute();

    }
    private void cancelParserTask(){
        if (mParserTask!=null) {
            mParserTask.cancel(true);
            mParserTask=null;
        }
    }

    class ParserTask extends AsyncTask<Void,Void,String>{

        private byte[] reponseData;
        private boolean parserError;
        private List<String> list=new ArrayList<>();
        public ParserTask(byte[] data){
            reponseData=data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                final String data=parseList(new ByteArrayInputStream(
                        reponseData));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new saveCacheTask(getActivity(),data,getCacheKey()).execute();
                    }
                });

                list.add(data);
                if (list==null) {
                    ResultBean resultBean = XmlUtils.toBean(ResultBean.class,
                            reponseData);
                    if (resultBean != null) {
                        mResult = resultBean.getResult();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                parserError=true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (parserError) {
                readCacheData(getCacheKey());

            }else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter==null||mAdapter.getCount()==0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }

        boolean scrollEnd=false;
        try{
            if (view.getPositionForView(mAdapter.getFootView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        }catch (Exception e){
            scrollEnd=false;
        }
        if (mState==STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
                mAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    @Override
    public void onRefresh() {
        if (mState == STATE_REFRESH) {
            return;
        }
        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);

    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }

    private static final String CACHE_KEY_PREFIX = "newslist_";

    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    protected String parseList(InputStream is) throws Exception {
        return null;
    }

    protected List<String> readList(Serializable seri) {
        return null;
    }

    private String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    protected boolean needAutoRefresh() {
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelParserTask();
    }

    /**
     * 保存已读的文章列表
     *
     * @param view
     * @param prefFileName
     * @param key
     */
    protected void saveToReadedList(final View view, final String prefFileName,
                                    final String key) {
        // 放入已读列表
        AppContext.putReadedPostList(prefFileName, key, "true");
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (tvTitle != null) {
            tvTitle.setTextColor(AppContext.getInstance().getResources().getColor(ThemeSwitchUtils.getTitleReadedColor()));
        }
    }
}
