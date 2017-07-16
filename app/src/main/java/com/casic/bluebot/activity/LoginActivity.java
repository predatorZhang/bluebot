package com.casic.bluebot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.UserObject;
import com.casic.bluebot.common.FastBlur;
import com.casic.bluebot.common.Global;
import com.casic.bluebot.common.LoginBackground;
import com.casic.bluebot.common.SimpleTextWatcher;
import com.casic.bluebot.common.widget.LoginAutoCompleteEdit;
import com.casic.bluebot.http.ApiClent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.android.tpush.XGPushManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;


@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private final String TAG = "LoginActivity";


    public static final String EXTRA_BACKGROUND = "background";

    final private int RESULT_CLOSE = 100;
    final double scaleFactor = 16;
    final float radius = 8;


    @Extra
    Uri background;
    @ViewById
    ImageView userIcon;
    @ViewById
    ImageView backgroundImage;
    @ViewById
    View layoutRoot;
    @ViewById
    LoginAutoCompleteEdit editName;
    @ViewById
    EditText editPassword;
    @ViewById
    ImageView imageValify;
    @ViewById
    EditText editValify;
/*    @ViewById
    EditText edit2FA;*/
    @ViewById
    View captchaLayout;
    @ViewById
    View loginButton;
    @ViewById
    View  loginLayout;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.icon_user_monkey)
            .showImageOnFail(R.drawable.icon_user_monkey)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();
    View androidContent;
    TextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            upateLoginButton();
        }
    };
    TextWatcher textWatcherName = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            userIcon.setImageResource(R.drawable.icon_user_monkey);
        }
    };
    private String globalKey = "";

    private ApiClent.ClientCallback loginCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            String message=resp.getMessage();

            if (resp.isSuccess()) {

                showProgressBar(false);
                UserObject user = UserObject.parseJson(message);
                AccountInfo.saveAccount(LoginActivity.this, user);
                SensorHubApplication.sUserObject = user;
                AccountInfo.saveReloginInfo(LoginActivity.this, user.email, user.global_key);
                //TODO LIST:保存全局的cookie
                LoginActivity.this.sendBroadcast(new Intent(GuideActivity.BROADCAST_GUIDE_ACTIVITY));
                LoginActivity.this.finish();
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, DeviceListActivity_.class));

            }
            else{
                showProgressBar(false);
                needCaptcha();
                showMiddleToast(message);
            }

        }

        @Override
        public void onFailure(String message) {

        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "" + e);
            showProgressBar(false);
            showMiddleToast("服务器连接失败");

        }
    };

    private ApiClent.ClientCallback updateJcaptchaCallback = new ApiClent.ClientCallback() {
        @Override
        public void onSuccess(Object data) {

            captchaLayout.setVisibility(View.VISIBLE);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 调用下，防止收到上次登陆账号的通知
        XGPushManager.registerPush(this, "*");
    }

    @AfterViews
    void init() {
        if (background == null) {
            LoginBackground.PhotoItem photoItem = new LoginBackground(this).getPhoto();
            File file = photoItem.getCacheFile(this);
            if (file.exists()) {
                background = Uri.fromFile(file);
            }
        }

        try {
            BitmapDrawable bitmapDrawable;
            if (background == null) {
                bitmapDrawable = createBlur();
            } else {
                bitmapDrawable = createBlur(background);
            }
            backgroundImage.setImageDrawable(bitmapDrawable);
        } catch (Exception e) {
            //Global.errorLog(e);
        }

/*
        needCaptcha();
*/

        editName.addTextChangedListener(textWatcher);
        editPassword.addTextChangedListener(textWatcher);
        editValify.addTextChangedListener(textWatcher);
        upateLoginButton();

        editName.addTextChangedListener(textWatcherName);

        androidContent = findViewById(android.R.id.content);
        androidContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = androidContent.getHeight();
                if (height > 0) {
                    ViewGroup.LayoutParams lp = layoutRoot.getLayoutParams();
                    lp.height = height;
                    layoutRoot.setLayoutParams(lp);
                    androidContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        String lastLoginName = AccountInfo.loadLastLoginName(this);
        if (!lastLoginName.isEmpty()) {
            editName.setDisableAuto(true);
            editName.setText(lastLoginName);
            editName.setDisableAuto(false);
            editPassword.requestFocus();
/*
            editName(false);
*/
        }
    }

    private BitmapDrawable createBlur() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.entrance1, options);
        int height = options.outHeight;
        int width = options.outWidth;

        options.outHeight = (int) (height / scaleFactor);
        options.outWidth = (int) (width / scaleFactor);
        options.inSampleSize = (int) (scaleFactor + 0.5);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.entrance1, options);
        Bitmap blurBitmap = FastBlur.doBlur(bitmap, (int) radius, true);

        return new BitmapDrawable(getResources(), blurBitmap);
    }

    private BitmapDrawable createBlur(Uri uri) {
        String path = Global.getPath(this, uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;

        options.outHeight = (int) (height / scaleFactor);
        options.outWidth = (int) (width / scaleFactor);
        options.inSampleSize = (int) (scaleFactor + 0.5);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        Bitmap blurBitmap = FastBlur.doBlur(bitmap, (int) radius, true);

        return new BitmapDrawable(getResources(), blurBitmap);
    }

    @Click
    void imageValify() {
        editValify.requestFocus();
        needCaptcha();
    }

    @Click
    void register() {
        RegisterActivity_.intent(this).startForResult(RESULT_CLOSE);
    }

    @OnActivityResult(RESULT_CLOSE)
    void resultRegiter(int result) {
        if (result == Activity.RESULT_OK) {
            sendBroadcast(new Intent(GuideActivity.BROADCAST_GUIDE_ACTIVITY));
            finish();
        }
    }

    private void needCaptcha() {
        //TODO LIST：网络请求下载验证图片
        ApiClent.updateCaptcha(LoginActivity.this, updateJcaptchaCallback);

    }


    @Click
    protected final void loginButton() {
        login();
    }

    private void login() {
        try {
            String name = editName.getText().toString();
            String password = editPassword.getText().toString();
            String captcha = editValify.getText().toString();

            if (name.isEmpty()) {
                showMiddleToast("邮箱或个性后缀不能为空");
                return;
            }

            if (password.isEmpty()) {
                showMiddleToast("密码不能为空");
                return;
            }

            //TODO LIST:调用登录接口，进行用户登录
            showProgressBar(true, R.string.logining);
            ApiClent.loginWithCaptcha(LoginActivity.this, name, password, captcha, loginCallback);


        } catch (Exception e) {
/*
            Global.errorLog(e);
*/
        }
    }


    @Click
    protected final void loginFail() {
        String[] listTitles = getResources().getStringArray(R.array.dialog_login_fail_help);
        new AlertDialog.Builder(this).setItems(listTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //TODO LIST:找回密码activity
                    SendEmailPasswordActivity_
                            .intent(LoginActivity.this)
                            .start();
                } else if (which == 1) {
                    //TODO LIST：邮件激活账户Activity
                /*    SendEmailActiveActivity_
                            .intent(LoginActivity.this)
                            .start();*/
                }
            }
        }).show();
    }




    @Override
    public void onBackPressed() {
            finish();
    }


    private void upateLoginButton() {
        if (editName.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        if (editPassword.getText().length() == 0) {
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
}