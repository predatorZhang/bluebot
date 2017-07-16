package com.casic.bluebot.view;

import android.view.View;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.activity.MessageListActivity;


/**
 * Created by chaochen on 14-10-24.
 */
public class BlankViewDisplay {

    public static final String MY_PROJECT_BLANK = "您还木有项目呢，赶快起来创建吧～";
    public static final String OTHER_PROJECT_BLANK = "这个人很懒，一个项目都木有～";

    public static void setBlank(int itemSize, Object fragment, boolean request, View v, View.OnClickListener onClick) {
        setBlank(itemSize, fragment, request, v, onClick, "");
    }

    public static void setBlank(int itemSize, Object fragment, boolean request, View v, View.OnClickListener onClick, String tipString) {
        // 有些界面不需要显示blank状态
        if (v == null) {
            return;
        }

        View btn = v.findViewById(R.id.btnRetry);
        if (request) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(onClick);
        }

        setBlank(itemSize, fragment, request, v, tipString);
    }

    private static void setBlank(int itemSize, Object fragment, boolean request, View v, String tipString) {
        boolean show = (itemSize == 0);
        if (!show) {
            v.setVisibility(View.GONE);
            return;
        }
        v.setVisibility(View.VISIBLE);

        int iconId = R.drawable.ic_exception_no_network;
        String text = "";

        if (tipString.isEmpty()) {
            if (request) {
                if (fragment instanceof MessageListActivity) {
                    iconId = R.drawable.ic_exception_blank_maopao;
                    text = "无私信\n打个招呼吧~";
                }

            } else {
                iconId = R.drawable.ic_exception_no_network;
                text = "获取数据失败\n请检查下网络是否通畅";
            }
        } else {
            if (request) {
                iconId = R.drawable.ic_exception_blank_task;
            } else {
                iconId = R.drawable.ic_exception_no_network;
            }
            text = tipString;
        }

        v.findViewById(R.id.icon).setBackgroundResource(iconId);
        ((TextView) v.findViewById(R.id.message)).setText(text);
    }

}
