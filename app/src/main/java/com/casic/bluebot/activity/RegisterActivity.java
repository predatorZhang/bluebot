package com.casic.bluebot.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.UserObject;
import com.casic.bluebot.common.SimpleTextWatcher;
import com.casic.bluebot.http.ApiClent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BackActivity {

    private final String TAG = "RegisterActivity";
    @ViewById
    EditText editName;
    @ViewById
    EditText editGlobal;
    @ViewById
    ImageView imageValify;
    @ViewById
    EditText editValify;
    @ViewById
    View captchaLayout;
    @ViewById
    View valifyDivide;
    @ViewById
    View loginButton;
    @ViewById
    TextView textClause;

    private ApiClent.ClientCallback registerCallback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()){
                UserObject user = UserObject.parseJson(message);
                //TODO LIST:进行人员登录
                ApiClent.login(RegisterActivity.this, user.email, user.global_key, loginCallback);
                showProgressBar(true);
            }
            else{
                showMiddleToast(message);
                needCaptcha();
                showProgressBar(false);
            }
        }

        @Override
        public void onFailure(String message) {
            //TODO LIST:
            Log.d(TAG, message);

        }

        @Override
        public void onError(Exception e) {

            Log.d(TAG, ""+e);
            showProgressBar(false);
            showMiddleToast("服务器连接失败");

        }
    };

    private ApiClent.ClientCallback loginCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {
                showProgressBar(false);
                UserObject user = UserObject.parseJson(message);
                AccountInfo.saveAccount(RegisterActivity.this, user);
                SensorHubApplication.sUserObject = user;
                AccountInfo.saveReloginInfo(RegisterActivity.this, user.email, user.global_key);
                //TODO LIST:保存全局的cookie
                //Global.syncCookie(this);
                RegisterActivity.this.setResult(RESULT_OK);
                RegisterActivity.this.sendBroadcast(new Intent(GuideActivity.BROADCAST_GUIDE_ACTIVITY));
                RegisterActivity.this.finish();
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, DeviceListActivity_.class));
                Toast.makeText(RegisterActivity.this,
                        "感谢您的支持",
                        Toast.LENGTH_LONG).show();
            }
            else{
                showProgressBar(false);
                showMiddleToast(message);
            }

        }

        @Override
        public void onFailure(String message) {

        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, ""+e);
            showProgressBar(false);
            showMiddleToast("服务器连接失败");

        }
    };

    private ApiClent.ClientCallback updateJcaptchaCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            showValify(true);
            byte[] responseBody = (byte[]) data;
            imageValify.setImageBitmap(BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length));
            editValify.setText("");
        }

        @Override
        public void onFailure(String message) {

            showMiddleToast("获取验证码失败");
        }

        @Override
        public void onError(Exception e) {

            showMiddleToast("获取验证码失败");
        }
    };


    TextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            upateLoginButton();
        }
    };

    @AfterViews
    void init() {
        textClause.setText(Html.fromHtml("点击立即体验，即表示同意<font color=\"#3bbd79\">《SensorHub服务条款》</font>"));
        editName.addTextChangedListener(textWatcher);
        editGlobal.addTextChangedListener(textWatcher);
        editValify.addTextChangedListener(textWatcher);
        upateLoginButton();
        needCaptcha();
        showValify(false);
    }

    private void needCaptcha() {
        ApiClent.updateCaptcha(RegisterActivity.this, updateJcaptchaCallback);
    }

    @Click
    void loginButton() {
        try {
            String name = editName.getText().toString();
            String password = editGlobal.getText().toString();
            String captcha = editValify.getText().toString();

            if (name.isEmpty()) {
                showMiddleToast("邮箱或个性后缀不能为空");
                return;
            }

            if (password.isEmpty()) {
                showMiddleToast("密码不能为空");
                return;
            }

            ApiClent.register(RegisterActivity.this, name, password, captcha, registerCallback);
            showProgressBar(true);

        } catch (Exception e) {

        }
    }

    private void showValify(boolean show) {
        int visable = show ? View.VISIBLE : View.GONE;
        captchaLayout.setVisibility(visable);
        valifyDivide.setVisibility(visable);
    }

    @Click
    void imageValify() {
        editValify.requestFocus();
        downloadValifyPhoto();
    }

    @Click
    void textClause() {
        //TODO LIST:协议条款
     /*   Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);*/
    }

    private void upateLoginButton() {
        if (editName.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        if (editGlobal.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        if (captchaLayout.getVisibility() == View.VISIBLE &&
                editValify.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        loginButton.setEnabled(true);
    }

    private void downloadValifyPhoto() {

        ApiClent.updateCaptcha(RegisterActivity.this, updateJcaptchaCallback);

    }

}
