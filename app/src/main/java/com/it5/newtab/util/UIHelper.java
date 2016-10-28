package com.it5.newtab.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

import com.it5.newtab.AppConfig;
import com.it5.newtab.AppContext;

/**
 * 界面帮助类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月10日 下午3:33:36
 * 
 */
public class UIHelper {

    /** 全局web样式 */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";
    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";



    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
//        webView.setWebViewClient(UIHelper.getWebViewClient());
    }



    public static String setHtmlCotentSupportImagePreview(String body) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
                || TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
            // 添加点击图片放大支持
            // 添加点击图片放大支持
            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" onClick=\"showImagePreview('$2')\"");
        } else {
            // 过滤掉 img标签
            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }
        return body;
    }





    /**
     * 打开系统中的浏览器
     * 
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    public static SpannableString parseActiveAction(int objecttype,
                                                    int objectcatalog, String objecttitle) {
        String title = "";
        int start = 0;
        int end = 0;
        if (objecttype == 32 && objectcatalog == 0) {
            title = "加入了开源中国";
        } else if (objecttype == 1 && objectcatalog == 0) {
            title = "添加了开源项目 " + objecttitle;
        } else if (objecttype == 2 && objectcatalog == 1) {
            title = "在讨论区提问：" + objecttitle;
        } else if (objecttype == 2 && objectcatalog == 2) {
            title = "发表了新话题：" + objecttitle;
        } else if (objecttype == 3 && objectcatalog == 0) {
            title = "发表了博客 " + objecttitle;
        } else if (objecttype == 4 && objectcatalog == 0) {
            title = "发表一篇新闻 " + objecttitle;
        } else if (objecttype == 5 && objectcatalog == 0) {
            title = "分享了一段代码 " + objecttitle;
        } else if (objecttype == 6 && objectcatalog == 0) {
            title = "发布了一个职位：" + objecttitle;
        } else if (objecttype == 16 && objectcatalog == 0) {
            title = "在新闻 " + objecttitle + " 发表评论";
        } else if (objecttype == 17 && objectcatalog == 1) {
            title = "回答了问题：" + objecttitle;
        } else if (objecttype == 17 && objectcatalog == 2) {
            title = "回复了话题：" + objecttitle;
        } else if (objecttype == 17 && objectcatalog == 3) {
            title = "在 " + objecttitle + " 对回帖发表评论";
        } else if (objecttype == 18 && objectcatalog == 0) {
            title = "在博客 " + objecttitle + " 发表评论";
        } else if (objecttype == 19 && objectcatalog == 0) {
            title = "在代码 " + objecttitle + " 发表评论";
        } else if (objecttype == 20 && objectcatalog == 0) {
            title = "在职位 " + objecttitle + " 发表评论";
        } else if (objecttype == 101 && objectcatalog == 0) {
            title = "回复了动态：" + objecttitle;
        } else if (objecttype == 100) {
            title = "更新了动态";
        }
        SpannableString sp = new SpannableString(title);
        // 设置标题字体大小、高亮
        if (!StringUtils.isEmpty(objecttitle)) {
            start = title.indexOf(objecttitle);
            if (objecttitle.length() > 0 && start > 0) {
                end = start + objecttitle.length();
                sp.setSpan(new AbsoluteSizeSpan(14, true), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#0e5986")),
                        start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }

    /**
     * 组合动态的回复文本
     * 
     * @param name
     * @param body
     * @return
     */
    public static SpannableStringBuilder parseActiveReply(String name,
                                                          String body) {
        Spanned span = Html.fromHtml(body.trim());
        SpannableStringBuilder sp = new SpannableStringBuilder(name + "：");
        sp.append(span);
        // 设置用户名字体加粗、高亮
        // sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
        // name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0,
                name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sp;
    }



    /**
     * 清除app缓存
     * 
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    //未实现
    public static void showLinkRedirect(Context context, int objType, int objId, String objKey) {

    }

    //未实现
    public static void openBrowser(Context context, String url) {

    }
}
