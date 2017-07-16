package com.casic.bluebot.common;

import android.content.Context;

import com.casic.bluebot.R;


/*
 */
public class Dimens {

    public static float PROJECT_ICON_ROUND = 2;

    public static void initValue(Context context) {
        PROJECT_ICON_ROUND = context.getResources().getDimension(R.dimen.project_icon_circle);
    }
}
