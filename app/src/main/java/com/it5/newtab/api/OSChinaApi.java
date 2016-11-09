package com.it5.newtab.api;

import com.it5.newtab.AppContext;
import com.it5.newtab.old_been.NewsList;
import com.it5.newtab.old_been.Report;
import com.it5.newtab.util.StringUtils;
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

    /**
     * 获取新闻明细
     */
    public static void getNewsDetail(int id, AsyncHttpResponseHandler handler) {
        RequestParams params=new RequestParams("id", id);
        ApiHttpClient.get("action/api/news_detail", params, handler);
    }


    /**
     * 用户添加收藏
     *
     * @param uid
     *            用户UID
     * @param objid
     *            比如是新闻ID 或者问答ID 或者动弹ID
     * @param type
     *            1:软件 2:话题 3:博客 4:新闻 5:代码
     */
    public static void addFavorite(int uid, int objid, int type,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("objid", objid);
        params.put("type", type);
        ApiHttpClient.post("action/api/favorite_add", params, handler);
    }

    public static void delFavorite(int uid, int objid, int type,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("objid", objid);
        params.put("type", type);
        ApiHttpClient.post("action/api/favorite_delete", params, handler);
    }

    /**
     * 举报
     *
     * @param report
     * @param handler
     */
    public static void report(Report report, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("obj_id", report.getObjId());
        params.put("url", report.getUrl());
        params.put("obj_type", report.getObjType());
        params.put("reason", report.getReason());
        if (report.getOtherReason() != null
                && !StringUtils.isEmpty(report.getOtherReason())) {
            params.put("memo", report.getOtherReason());
        }
        ApiHttpClient.post("action/communityManage/report", params, handler);
    }


    /**
     * 用户针对某个新闻，帖子，动弹，消息发表评论的接口，参数使用POST方式提交
     *
     * @param catalog
     *            　　 1新闻　　2 帖子　　３　动弹　　４消息中心
     * @param id
     *            被评论的某条新闻，帖子，动弹或者某条消息的id
     * @param uid
     *            当天登陆用户的UID
     * @param content
     *            发表的评论内容
     * @param isPostToMyZone
     *            是否转发到我的空间，０不转发　　１转发到我的空间（注意该功能之对某条动弹进行评论是有效，其他情况下服务器借口可以忽略该参数）
     * @param handler
     */
    public static void publicComment(int catalog, int id, int uid,
                                     String content, int isPostToMyZone, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("id", id);
        params.put("uid", uid);
        params.put("content", content);
        params.put("isPostToMyZone", isPostToMyZone);
        ApiHttpClient.post("action/api/comment_pub", params, handler);
    }
}
