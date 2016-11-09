package com.it5.newtab;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.it5.newtab.api.ApiHttpClient;
import com.it5.newtab.base.BaseApplication;
import com.it5.newtab.old_been.User;
import com.it5.newtab.util.StringUtils;
import com.loopj.android.http.AsyncHttpClient;

import java.util.Properties;
import java.util.UUID;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * Created by IT5 on 2016/10/25.
 */
public class AppContext extends BaseApplication {
    private static AppContext instance;
    public static int PAGE_SIZE=20;

    public static AppContext getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        init();

    }
    private void init(){
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        ApiHttpClient.setHttpClient(client);
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

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }


    public void clearAppCache() {

    }

    public void Logout() {

    }

    public boolean isLogin() {
        return false;
    }

    private int loginUid;
    private boolean login;
    private void initLogin() {
        User user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = user.getId();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setPortrait(getProperty("user.face"));
        user.setAccount(getProperty("user.account"));
        user.setLocation(getProperty("user.location"));
        user.setFollowers(StringUtils.toInt(getProperty("user.followers"), 0));
        user.setFans(StringUtils.toInt(getProperty("user.fans"), 0));
        user.setScore(StringUtils.toInt(getProperty("user.score"), 0));
        user.setFavoritecount(StringUtils.toInt(
                getProperty("user.favoritecount"), 0));
        user.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        user.setGender(getProperty("user.gender"));
        return user;
    }
    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }
    public int getLoginUid() {
        return loginUid;
    }
}
