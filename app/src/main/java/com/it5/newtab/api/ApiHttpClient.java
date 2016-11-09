package com.it5.newtab.api;

import android.util.Log;

import com.it5.newtab.AppContext;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Locale;

import cz.msebera.android.httpclient.client.params.ClientPNames;

import static com.it5.newtab.util.TLog.log;

/**
 * Created by IT5 on 2016/10/27.
 */

public class ApiHttpClient {

    public final static String HOST = "www.oschina.net";
    private static String API_URL = "http://www.oschina.net/%s";
    //    public final static String HOST = "192.168.1.101";
//    private static String API_URL = "http://192.168.1.101/%s";
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";


    public static AsyncHttpClient client;

    public ApiHttpClient() {
    }
    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Host", HOST);
        client.addHeader("Connection", "Keep-Alive");
        client.getHttpClient().getParams()
                .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
    }
    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }


    public static void get(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        Log.d("BASE_CLIENT", "request:" + url);
        return url;
    }


    public static void post(String partUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("POST ").append(partUrl).append("&")
                .append(params).toString());
    }
}
