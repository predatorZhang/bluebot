package com.casic.bluebot.asyntask;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;

/**
 * Created by admin on 2015/4/30.
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    protected String exception;
    private boolean isShowLoading;

    public BaseAsyncTask(final Activity param_0) {
        this(param_0, true);
    }

    public BaseAsyncTask(final Activity param_0, final DialogInterface.OnCancelListener param_1) {
        this(param_0, param_1, true, null);
    }

    public BaseAsyncTask(final Activity param_0, final DialogInterface.OnCancelListener param_1, final boolean param_2, final String param_3) {
        super();
        this.isShowLoading = true;
    }

    public BaseAsyncTask(final Activity param_0, final DialogInterface.OnCancelListener param_1, final boolean param_2, final String param_3, final boolean param_4) {
        super();
        this.isShowLoading = true;
        this.isShowLoading = param_4;
    }

    public BaseAsyncTask(final Activity param_0, final boolean param_1) {
        this(param_0, null, param_1, null);
    }

    public BaseAsyncTask(final Activity param_0, final boolean param_1, final boolean param_2) {
        this(param_0, null, param_1, null, param_2);
    }

    protected Result doInBackground(final Params... param_0) {
        return null;
    }

    protected void onPostExecute(final Result param_0) {
        super.onPostExecute(param_0);
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }
}
