package com.casic.bluebot.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.CheckBox;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.activity.AboutActivity_;
import com.casic.bluebot.activity.DeviceListActivity_;
import com.casic.bluebot.activity.GuideActivity;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.common.widget.CustomDialog;
import com.tencent.android.tpush.XGPushManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_setting)
public class SettingFragment extends Fragment {

    @ViewById
    CheckBox allNotify;

    @AfterViews
    void init() {
        boolean mLastNotifySetting = AccountInfo.getNeedPush(getActivity());
        allNotify.setChecked(mLastNotifySetting);
        setHasOptionsMenu(true);
    }

    @Click
    void accountSetting() {
        //TODO LIST���˻�����
/*
        AccountSetting_.intent(this).start();
*/
    }

    @Click
    void pushSetting() {
        allNotify.performClick();
    }

    @Click
    void allNotify() {
        AccountInfo.setNeedPush(getActivity(), allNotify.isChecked());
        Intent intent = new Intent(DeviceListActivity_.BroadcastPushStyle);
        getActivity().sendBroadcast(intent);
    }

    @Click
    void feedback() {
        //TODO LIST:今天晚上完成反馈的任务
/*
        FeedbackActivity_.intent(getActivity()).start();
*/
    }

    @Click
    void aboutCoding() {
        //TODO LIST：关于

        AboutActivity_.intent(SettingFragment.this).start();

    }

    @Click
    void loginOut() {
        showDialog(SensorHubApplication.sUserObject.global_key, "确定要退出？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                XGPushManager.registerPush(getActivity(), "*");
                AccountInfo.loginOut(getActivity());
                startActivity(new Intent(getActivity(), GuideActivity.class));
                getActivity().finish();
            }
        });
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




}
