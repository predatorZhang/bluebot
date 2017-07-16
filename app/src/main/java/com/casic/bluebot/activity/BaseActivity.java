package com.casic.bluebot.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.casic.bluebot.common.DialogUtil;
import com.casic.bluebot.common.FootUpdate;
import com.casic.bluebot.common.widget.CustomDialog;


public class BaseActivity extends UmengActivity {

    protected LayoutInflater mInflater;

    private ProgressDialog mProgressDialog;

    protected FootUpdate mFootUpdate = new FootUpdate();



    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void showProgressBar(boolean show, String message) {
        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    protected void showProgressBar(boolean show, int message) {
        String s = getString(message);
        showProgressBar(show, s);
    }

    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mInflater = getLayoutInflater();


    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        super.onDestroy();
    }


    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        showDialog(title, msg, clickOk, null);
    }

    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk,
                              DialogInterface.OnClickListener clickCancel) {
        showDialog(title, msg, clickOk, clickCancel, "ȷ��", "ȡ��");
    }

    public final void dialogTitleLineColor(Dialog dialog) {
        CustomDialog.dialogTitleLineColor(this, dialog);
    }


    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk,
                              DialogInterface.OnClickListener clickCancel,
                              String okButton,
                              String cannelButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(okButton, clickOk)
                .setNegativeButton(cannelButton, clickCancel)
                .show();
        dialogTitleLineColor(dialog);
    }

    protected void showButtomToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showButtomToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }

    protected void showMiddleToast(int id) {
        String message = getString(id);
        showMiddleToast(message);
    }

    protected void showMiddleToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private DialogUtil.LoadingPopupWindow mDialogProgressPopWindow = null;

    public void initDialogLoading() {
        if (mDialogProgressPopWindow == null) {
            PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    hideProgressDialog();
                }
            };

            mDialogProgressPopWindow = DialogUtil.initProgressDialog(this, onDismissListener);
        }
    }

    public void showDialogLoading(String title) {
        initDialogLoading();
        DialogUtil.showProgressDialog(this, mDialogProgressPopWindow, title);
    }

    public void showDialogLoading() {
        showDialogLoading("");
    }


    public void hideProgressDialog() {
        if (mDialogProgressPopWindow != null) {
            DialogUtil.hideDialog(mDialogProgressPopWindow);
        }
    }



}
