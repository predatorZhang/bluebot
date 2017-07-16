package com.casic.bluebot.common;

import android.graphics.drawable.Drawable;

import com.casic.bluebot.SensorHubApplication;


/**
 * Created by chaochen on 14-11-12.
 */
public class DrawableTool {
    public static void zoomDrwable(Drawable drawable, boolean isMonkey) {
        int width = isMonkey ? SensorHubApplication.sEmojiMonkey : SensorHubApplication.sEmojiNormal;
        drawable.setBounds(0, 0, width, width);
    }
}
