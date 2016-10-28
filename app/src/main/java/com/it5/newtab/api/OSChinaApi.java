package com.it5.newtab.api;

import com.it5.newtab.AppContext;
import com.it5.newtab.old_been.NewsList;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by IT5 on 2016/10/27.
 */

public class OSChinaApi {

    /**
     * 获取新闻列表
     *
     * @param catalog
     *            类别 （1，2，3）
     * @param page
     *            第几页
     * @param handler
     */
    public static void getNewsList(int catalog, int page,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
        if (catalog == NewsList.CATALOG_WEEK) {
            params.put("show", "week");
        } else if (catalog == NewsList.CATALOG_MONTH) {
            params.put("show", "month");
        }
        ApiHttpClient.get("action/api/news_list", params, handler);
    }

}
