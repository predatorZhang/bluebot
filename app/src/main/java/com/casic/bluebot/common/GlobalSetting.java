package com.casic.bluebot.common;


public class GlobalSetting {

    private static GlobalSetting sGlobalSetting;
    private String mNoNotifyGlobalKey = "";

    private GlobalSetting() {
    }

    public static GlobalSetting getInstance() {
        if (sGlobalSetting == null) {
            sGlobalSetting = new GlobalSetting();
        }

        return sGlobalSetting;
    }

    public void setMessageNoNotify(String globalKey) {
        mNoNotifyGlobalKey = globalKey;
    }

    public void removeMessageNoNotify() {
        mNoNotifyGlobalKey = "";
    }

    public String getMessageNotify() {
        return mNoNotifyGlobalKey;
    }
}
