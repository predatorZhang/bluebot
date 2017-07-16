package com.casic.bluebot.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.casic.bluebot.R;
import com.casic.bluebot.UpdateService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about)
public class AboutActivity extends UmengActivity {

    @ViewById
    TextView version;

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;

            String versionString = String.format("版本：%s", versionName);
            version.setText(versionString);

        } catch (Exception e) {
/*
            Global.errorLog(e);
*/
        }
    }

    @OptionsItem(android.R.id.home)
    void back() {
        onBackPressed();
    }

    @Click
    void markCoding() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "软件市场里暂时没有找到BlueBot", Toast.LENGTH_SHORT).show();
        }
    }

    @Click
    void checkUpdate() {
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra(UpdateService.EXTRA_BACKGROUND, false);
        startService(intent);
    }

/*
    @Click
    void codingWebsite() {
        WebActivity_.intent(this).url(Global.HOST).start();
    }*/
}
