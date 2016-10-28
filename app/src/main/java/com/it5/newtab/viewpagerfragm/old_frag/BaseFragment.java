package com.it5.newtab.viewpagerfragm.old_frag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it5.newtab.AppContext;
import com.it5.newtab.R;
import com.it5.newtab.interf.BaseFragmentInterface;
import com.it5.newtab.ui.dialog.DialogControl;

/**
 * 碎片基类 old
 * 旧版的 basefragmentinterface 与baseviewinterface是一样的
 *
 * 功能--是定义下拉刷新 常量  与 加载时的进度对话框
 * Created by IT5 on 2016/10/26.
 */

public class BaseFragment extends Fragment implements View.OnClickListener,BaseFragmentInterface {

    public static final int STATE_NONE=0;
    public static final int STATE_REFRESH=1;
    public static final int STATE_LOADMORE=2;
    public static final int STATE_NOMORE=3;

    // 正在下拉但还没有到刷新的状态
    public static final int STATE_PRESSNONE=4;
    public static int mState=STATE_NONE;

    protected LayoutInflater mInflater;
    public AppContext getApplication(){
        return (AppContext) getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater=inflater;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected int getLayoutId(){
        return 0;
    }
    protected View inflateView(int resId){
        return mInflater.inflate(resId,null);
    }

    public boolean onBackPressed(){
        return false;
    }

    protected void hideWaitDialog(){
        FragmentActivity activity=getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl)activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog(){
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }
}
