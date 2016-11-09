package com.it5.newtab.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.it5.newtab.AppContext;
import com.it5.newtab.AppManager;
import com.it5.newtab.R;
import com.it5.newtab.interf.BaseViewInterface;
import com.it5.newtab.ui.dialog.CommonToast;
import com.it5.newtab.ui.dialog.DialogControl;
import com.it5.newtab.util.DialogHelp;
import com.it5.newtab.util.StringUtils;
import com.it5.newtab.util.TDevice;

import butterknife.ButterKnife;

/**
 * Created by IT5 on 2016/11/3.
 */

public class BaseActivity extends AppCompatActivity implements DialogControl,View.OnClickListener, BaseViewInterface {

    public static final String INTENT_ACTION_EXIT_APP = "INTENT_ACTION_EXIT_APP";

    private boolean _isVisible;
    private ProgressDialog _waitDialog;
    protected LayoutInflater mInflater;

    protected ActionBar mActionBar;
    private TextView mTvActionTitle;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TDevice.hideSoftKeyboard(getCurrentFocus());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppContext.getNightModeSwitch()) {
            setTheme(R.style.AppTheme_Night);

        } else {
            setTheme(R.style.AppTheme_Light);
        }

        AppManager.getAppManager().addActivity(this);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        onBeforeSetContentLayout();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        mActionBar = getSupportActionBar();
        mInflater = getLayoutInflater();
        initActionBar(mActionBar);

        //通过注解绑定控件
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initData();
        _isVisible = true;
    }

    protected void onBeforeSetContentLayout() {
    }

    protected boolean hasActionBar() {
        return true;
    }

    protected int getLayoutId() {
        return 0;
    }

    protected int getActionBarTitle() {
        return R.string.actionbar_title_news;
    }

    protected View inflateView(int resId) {
        return mInflater.inflate(resId, null);
    }

    protected boolean hasBackButton() {
        return false;
    }

    protected void init(Bundle savedInstanceState) {
    }

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null) {
            return;
        }
        if (hasBackButton()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        } else {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayUseLogoEnabled(false);
            int titleRes = getActionBarTitle();
            if (titleRes != 0) {
                actionBar.setTitle(titleRes);
            }

        }

    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }
    public void setActionBarTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }
        if (hasActionBar() && mActionBar != null) {
            if (mTvActionTitle != null) {
                mTvActionTitle.setText(title);
            }
            mActionBar.setTitle(title);
        }
    }


    public void showToast(int msgResid, int icon, int gravity) {
        showToast(getString(msgResid), icon, gravity);
    }

    public void showToast(String message, int icon, int gravity) {
        CommonToast toast = new CommonToast(this);
        toast.setMessage(message);
        toast.setMessageIc(icon);
        toast.setLayoutGravity(gravity);
        toast.show();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog!=null) {
            try{
                _waitDialog.dismiss();
                _waitDialog=null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public ProgressDialog showWaitDialog(int resId) {
        return showWaitDialog(getString(resId));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog==null) {
                _waitDialog = DialogHelp.getWaitDialog(this, message);
            }
            if (_waitDialog!=null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }
}
