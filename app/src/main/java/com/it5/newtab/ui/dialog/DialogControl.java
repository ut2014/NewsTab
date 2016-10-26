package com.it5.newtab.ui.dialog;

import android.app.ProgressDialog;

/**
 * Created by IT5 on 2016/10/26.
 */

public interface DialogControl {
    public abstract void hideWaitDialog();

    public abstract ProgressDialog showWaitDialog();

    public abstract ProgressDialog showWaitDialog(int resId);
    public abstract ProgressDialog showWaitDialog(String text);
}
