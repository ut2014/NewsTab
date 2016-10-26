package com.it5.newtab;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by IT5 on 2016/10/25.
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;
    //单例
    public static AppManager getAppManager() {
        if (instance==null) {
            instance=new AppManager();
        }
        if (activityStack==null) {
            activityStack=new Stack<>();
        }
        return instance;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }
    //移去当前activity
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }
}
