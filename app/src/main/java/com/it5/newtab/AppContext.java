package com.it5.newtab;

import com.it5.newtab.base.BaseApplication;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * Created by IT5 on 2016/10/25.
 */
public class AppContext extends BaseApplication {
    private static AppContext instance;

    public static AppContext getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return false;
    }

    public static boolean isFristStart(){
        return getPreferences().getBoolean(AppConfig.KEY_FRITST_START,true);
    }
    public static void setFristStart(boolean frist){
        set(AppConfig.KEY_FRITST_START,frist);
    }

}
