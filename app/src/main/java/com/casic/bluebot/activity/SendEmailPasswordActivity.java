package com.casic.bluebot.activity;

import android.util.Log;

import com.casic.bluebot.R;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.http.ApiClent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_base_send_email)
public class SendEmailPasswordActivity extends SendEmailBaseActivity {

    private final String TAG = "SendEmailPassword";

    private ApiClent.ClientCallback sendEmailAndPassCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {

                showMiddleToast("激活邮件已经发送，请尽快去邮箱查收");
            }
            else {
                downloadValifyPhoto();
                showMiddleToast(message);
            }

        }

        @Override
        public void onFailure(String message) {

        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "" + e);
            showMiddleToast("该邮箱尚未注册");
        }
    };

    @AfterViews
    protected final void initResendEmail() {
        loginButton.setText("发送重置密码邮件");
    }

    @Click
    protected final void loginButton() {
        if (!isEnterSuccess()) {
            return;
        }



        ApiClent.sendEmailAndPassword(SendEmailPasswordActivity.this, this.getEmail(), this.getValify(), sendEmailAndPassCallback);

    }


}
