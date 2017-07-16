package com.casic.bluebot.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.casic.bluebot.http.ApiClent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EActivity
public class SendEmailBaseActivity extends BackActivity {

    @ViewById
    protected EditText editValify;

    @ViewById
    protected ImageView imageValify;

    @ViewById
    protected EditText editName;

    @ViewById
    protected Button loginButton;

    private ApiClent.ClientCallback updateJcaptchaCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

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

    public static boolean isValifyEmail(Context context, String email) {
        String match = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            return true;
        }

        Toast.makeText(context, "邮箱格式错误", Toast.LENGTH_SHORT).show();
        return false;
    }

    @AfterViews
    protected final void initBaseResend() {
        downloadValifyPhoto();
    }

    @Click
    protected final void imageValify() {
        editValify.requestFocus();
        downloadValifyPhoto();
    }

    protected void downloadValifyPhoto() {


        ApiClent.updateCaptcha(SendEmailBaseActivity.this, updateJcaptchaCallback);

    }

    protected String getEmail() {
        return editName.getText().toString();
    }

    protected String getValify() {
        return editValify.getText().toString();
    }

    protected boolean isEnterSuccess() {
        return isValifyEmail(this, getEmail())
                && isEnterValifyCode();
    }

    private boolean isEnterValifyCode() {
        if (getValify().isEmpty()) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
