package com.casic.bluebot.common;

import android.content.Intent;

/**
 * Created by chaochen on 14-10-29.
 */
public interface StartActivity {
    void startActivityForResult(Intent intent, int requestCode);
}
