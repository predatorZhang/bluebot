package com.casic.bluebot.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;


public class TextWatcherAt implements TextWatcher {

    Context mContext;
    StartActivity mStartActivity;
    int mResult;

    public TextWatcherAt(Context ctx, StartActivity startActivity, int activityResult) {
        this.mContext = ctx;
        this.mStartActivity = startActivity;
        this.mResult = activityResult;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
