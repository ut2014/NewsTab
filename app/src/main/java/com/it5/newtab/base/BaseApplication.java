package com.it5.newtab.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it5.newtab.R;
import com.it5.newtab.util.StringUtils;

/**
 * Created by IT5 on 2016/10/25.
 * 操作SharedPreferences,及自定义Toast
 */

public class BaseApplication extends Application {
    private static String PREF_NAME="creativelocker.pref";
    private static String LAST_REFRESH_TIME="last_refresh_time.pref";

    static Context _context;
    static Resources _resource;

    private static String lastToast="";
    private static long lastToastTime;
    private static boolean isAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD) {
            isAtLeastGB=true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context=getApplicationContext();
        _resource=_context.getResources();
    }

    public static synchronized BaseApplication context(){
        return (BaseApplication)_context;
    }

    public static Resources resources(){
        return _resource;
    }


    //放入已读文章列表中
    public static void putReadedPostList(String prefileName,String key,String value){
        SharedPreferences pre=getPreferences(prefileName);
        int size=pre.getAll().size();
        Editor edit=pre.edit();
        if (size>=100) {
            edit.clear();
        }
        edit.putString(key,value);
        apply(edit);
    }

    //读取是否是已读的文章列表
     public static boolean isOnReadedPostList(String prefFileName,String key){
        return getPreferences(prefFileName).contains(key);
     }


    /**
     * 记录列表上次刷新时间
     */
    public static void putToLastRefreshTime(String key,String value){
        SharedPreferences pre=getPreferences(LAST_REFRESH_TIME);
        Editor editor=pre.edit();
        editor.putString(key,value);
        apply(editor);
    }

    /**
     *获取列表的上次刷新时间
     */
    public static String getLastRefreshTime(String key){
        return getPreferences(LAST_REFRESH_TIME).getString(key, StringUtils.getCurTimeStr());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor){
        if (isAtLeastGB) {
            editor.apply();
        }else {
            editor.commit();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(){
        SharedPreferences pre=context().getSharedPreferences(PREF_NAME,MODE_MULTI_PROCESS);
        return pre;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String preName){
        SharedPreferences pre=context().getSharedPreferences(preName,MODE_MULTI_PROCESS);
        return pre;
    }
    public static void set(String key, int value) {
        Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, boolean value) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }


    public static String string(int id) {
        return _resource.getString(id);
    }

    public static String string(int id, Object... args) {
        return _resource.getString(id, args);
    }

    public static void showToast(int message) {
        showToast(message, Toast.LENGTH_LONG, 0);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void showToast(int message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon);
    }

    public static void showToast(String message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon, Gravity.BOTTOM);
    }

    public static void showToastShort(int message) {
        showToast(message, Toast.LENGTH_SHORT, 0);
    }

    public static void showToastShort(String message) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    public static void showToastShort(int message, Object... args) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
    }

    public static void showToast(int message, int duration, int icon) {
        showToast(message, duration, icon, Gravity.BOTTOM);
    }

    public static void showToast(int message, int duration, int icon,
                                 int gravity) {
        showToast(context().getString(message), duration, icon, gravity);
    }

    public static void showToast(int message, int duration, int icon,
                                 int gravity, Object... args) {
        showToast(context().getString(message, args), duration, icon, gravity);
    }

    public static void showToast(String message, int duration, int icon,
                                 int gravity) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast)
                    || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(context()).inflate(
                        R.layout.view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setImageResource(icon);
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(context());
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }

                toast.setDuration(duration);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }
}
