package com.casic.bluebot.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.SensorHubApplication;
import com.casic.bluebot.bean.AccountInfo;
import com.casic.bluebot.bean.RestResponse;
import com.casic.bluebot.bean.UserObject;
import com.casic.bluebot.common.ImageLoadTool;
import com.casic.bluebot.common.LoginBackground;
import com.casic.bluebot.http.ApiClent;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.File;

@EActivity(R.layout.splash)
@NoTitle
@Fullscreen
public class SplashActivity extends BaseActivity {

    @ViewById
    ImageView image;
    @ViewById
    TextView title;
    @ViewById
    View foreMask;
    @ViewById
    View logo;
    @AnimationRes
    Animation entrance;

    Uri background = null;
    boolean mNeedUpdateUser = false;
    private final String TAG = "SplashActivity";


    private ImageLoadTool imageLoadTool = new ImageLoadTool();


    private ApiClent.ClientCallback callback = new ApiClent.ClientCallback()
    {
        @Override
        public void onSuccess(Object data)
        {
            String sData = data.toString();
            RestResponse resp = RestResponse.parseJson(sData);
            if (resp.isSuccess()) {
                mNeedUpdateUser = false;
                String message=resp.getMessage();
                UserObject user = UserObject.parseJson(message);

                AccountInfo.saveAccount(SplashActivity.this, user);
                SensorHubApplication.sUserObject = user;
                //将邮箱与globalKey绑定，邮箱不更新但是更新globalKey
                AccountInfo.saveReloginInfo(SplashActivity.this, user.email, user.global_key);
                next();

            }

        }

        @Override
        public void onFailure(String message) {
            //TODO LIST:
            Log.d(TAG, message);

        }

        @Override
        public void onError(Exception e) {

            Log.d(TAG, e.getMessage());

        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0) {
                foreMask.startAnimation(entrance);
            } else if (msg.what == 1) {
                next();
            }
        }
    };


    @AfterViews
    void init() {

        LoginBackground.PhotoItem photoItem = new LoginBackground(this).getPhoto();

        File file = photoItem.getCacheFile(this);
        imageLoadTool.imageLoader.clearMemoryCache();

        if (file.exists()) {
            background = Uri.fromFile(file);
            image.setImageBitmap(imageLoadTool.imageLoader.loadImageSync("file://" + file.getPath(), ImageLoadTool.enterOptions));
            title.setText(photoItem.getTitle());

        } else {
            ImageSize imageSize = new ImageSize(SensorHubApplication.sWidthPix, SensorHubApplication.sHeightPix);
            image.setImageBitmap(imageLoadTool.imageLoader.loadImageSync("drawable://" + R.drawable.splash, imageSize));
        }

        entrance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    mHandler.sendEmptyMessageDelayed(1, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

     /*   //TODO LIST:判断更新用户的策略是怎么得来的呢？
        // global_key为空的话就需要跳转到Guidance界面去
        if (SensorHubApplication.sUserObject.global_key.isEmpty() && AccountInfo.isLogin(this)) {
            //TODO LIST：去请求用户信息
            ApiClent.updateUser(this, callback);
            mNeedUpdateUser = true;
        }
*/
        mHandler.sendEmptyMessageDelayed(0, 900);

        //TODO LIST:仅仅用户测试
        updateNotifyService();
        pushInXiaomi();

    }

    //TODO LIST:仅仅用于测试
    private void updateNotifyService() {
        boolean needPush = AccountInfo.getNeedPush(this);

        if (needPush) {
            String globalKey = SensorHubApplication.sUserObject.global_key;
            XGPushManager.registerPush(this, "shit");
        } else {
            XGPushManager.registerPush(this, "*");
        }
    }

    // 信鸽文档推荐调用，防止在小米手机上收不到推送
    private void pushInXiaomi() {
        Context context = getApplicationContext();
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
    }

    void next() {
        Intent intent;
        String mGlobalKey = AccountInfo.loadAccount(this).global_key;

        intent = new Intent(this, DeviceListActivity_.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scroll_in, R.anim.scroll_out);

        //TODO LIST:未读取消息
      //  UnreadNotify.update(this);
        finish();
    }

}
