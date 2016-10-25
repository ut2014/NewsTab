package com.it5.newtab.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.it5.newtab.AppContext;
import com.it5.newtab.AppManager;
import com.it5.newtab.R;
import com.it5.newtab.interf.BaseViewInterface;
import com.it5.newtab.widget.MyFragmentTabHost;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener,
        BaseViewInterface,View.OnClickListener
{
    @BindView(android.R.id.tabhost)
    MyFragmentTabHost mTabHost;

    private CharSequence mTitle;
    @BindView(R.id.quick_option_iv)
    View mAddBt;

    private Unbinder unbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppContext.getNightModeSwitch()) {
            setTheme(R.style.AppTheme_Night);
        }else {
            setTheme(R.style.AppTheme_Light);
        }
        setContentView(R.layout.activity_main);
        unbind=ButterKnife.bind(this);
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quick_option_iv:
                AppContext.showToast("点了中间按键");
                break;
        }

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public void initView() {
        mTitle=getTitle();
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        //中间按键图片触发
        mAddBt.setOnClickListener(this);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);
        //开启服务 判断是否要接受通知

        //是否第一次开启应
        if (AppContext.isFristStart()) {
            //清理数据缓存

            AppContext.setFristStart(false);
        }
        checkUpdate();
    }



    private void initTabs() {

    }
    private void checkUpdate() {
        AppContext.showToast("检查app是否有更新！");
    }
    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();;
    }
}
