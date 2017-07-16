package com.casic.bluebot.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.casic.bluebot.common.ImageLoadTool;
import com.casic.bluebot.common.widget.CustomDialog;

/**
 * Created by cc191954 on 14-8-11.
 * 封装了图片下载
 * 封装了网络请求
 */
public class BaseFragment extends Fragment  {

    protected LayoutInflater mInflater;
    protected View.OnClickListener mOnClickUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO LIST:打开用户详细信息
           /* String globalKey = (String) v.getTag();

            Intent intent = new Intent(getActivity(), UserDetailActivity_.class);
            intent.putExtra("globalKey", globalKey);
            startActivity(intent);*/
        }
    };
    private ImageLoadTool imageLoadTool = new ImageLoadTool();
    private ProgressDialog mProgressDialog;

    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void setProgressBarProgress() {
        if (mProgressDialog == null) {
            return;
        }

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgress(30);
    }

    protected void showProgressBar(boolean show, String message) {
        if (mProgressDialog == null) {
            return;
        }

        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    public ActionBarActivity getActionBarActivity() {
        return (ActionBarActivity) getActivity();
    }

    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    protected boolean progressBarIsShowing() {
        return mProgressDialog.isShowing();
    }

    protected ImageLoadTool getImageLoad() {
        return imageLoadTool;
    }


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mInflater = LayoutInflater.from(getActivity());

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", clickOk)
                .setNegativeButton("取消", null)
                .show();
        CustomDialog.dialogTitleLineColor(getActivity(), dialog);
    }



    protected void showButtomToast(int messageId) {
        if (!isResumed()) {
            return;
        }

        String message = getString(messageId);
        showButtomToast(message);
    }

    protected void showButtomToast(String msg) {
        if (!isResumed()) {
            return;
        }

        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showMiddleToast(String msg) {
        if (!isResumed()) {
            return;
        }

        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
