package com.it5.newtab.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.it5.newtab.AppContext;
import com.it5.newtab.AppManager;
import com.it5.newtab.R;
import com.it5.newtab.interf.BaseViewInterface;
import com.it5.newtab.widget.BadgeView;
import com.it5.newtab.widget.MyFragmentTabHost;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener,
        BaseViewInterface,View.OnClickListener
{
    @BindView(android.R.id.tabhost)
    MyFragmentTabHost mTabHost;
    private BadgeView mBvNotice;
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
//        AppContext.showToast(v.getId()+"");
        switch (v.getId()){
            case R.id.quick_option_iv:
                AppContext.showToast("点了中间按键");
                break;
        }

    }

    @Override
    public void onTabChanged(String tabId) {
        final int size=mTabHost.getTabWidget().getTabCount();
        for (int i=0;i<size;i++) {
            View v=mTabHost.getTabWidget().getChildAt(i);
            if (i==mTabHost.getCurrentTab()) {
                v.setSelected(true);
            }else {
                v.setSelected(false);
            }
        }
        if (tabId.equals(getString(MainTab.ME.getResName()))) {
            mBvNotice.setText("");
            mBvNotice.hide();
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public void initView() {
        mTitle=getTitle();
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            //去掉Tab中的坚线
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
        MainTab[] tabs = MainTab.values();
        int size = tabs.length;
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = View.inflate(this, R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            ImageView icon = (ImageView) indicator.findViewById(R.id.iv_icon);

            Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
            icon.setImageDrawable(drawable);
            //title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            if (i == 2) {
                indicator.setVisibility(View.INVISIBLE);
                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);

            if (mainTab.equals(MainTab.ME)) {
                View cn = indicator.findViewById(R.id.tab_mes);
                mBvNotice = new BadgeView(MainActivity.this, cn);
                mBvNotice.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                mBvNotice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                mBvNotice.setBackgroundResource(R.drawable.notification_bg);
                mBvNotice.setGravity(Gravity.CENTER);
            }
//            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }
    private void checkUpdate() {
//        AppContext.showToast("检查app是否有更新！");
    }
    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
        AppManager.getAppManager().removeActivity(this);
    }
}
