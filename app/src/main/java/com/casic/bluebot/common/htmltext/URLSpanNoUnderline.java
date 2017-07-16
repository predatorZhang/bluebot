package com.casic.bluebot.common.htmltext;

import com.casic.bluebot.http.NetUrl;

/**

 */
public class URLSpanNoUnderline {

    public static final String PATTERN_URL_MESSAGE = "^(?:https://[\\w.]*)?/user/messages/history/([\\w-]+)$";

    public static String createMessageUrl(String globalKey) {
        return NetUrl.HOST + "/user/messages/history/" + globalKey;
    }

}
