package com.casic.bluebot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.casic.bluebot.R;
import com.casic.bluebot.common.parallaxpager.ParallaxContextWrapper;
import com.casic.bluebot.fragments.ParallaxFragment;



public class GuideActivity extends ActionBarActivity {

    public static final String BROADCAST_GUIDE_ACTIVITY = "BROADCAST_GUIDE_ACTIVITY";
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GuideActivity.this.finish();
        }
    };
    private Uri mUri;

    @Override
    protected void attachBaseContext(Context newBase) {
        //ParallaxPager and Calligraphy don't seem to play nicely together
        //The solution was to add a listener for view creation events so that we can hook up
        // Calligraphy to our view creation calls instead.

           super.attachBaseContext(
                    new ParallaxContextWrapper(newBase, new OpenCalligraphyFactory()));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_parallax);

            IntentFilter filter = new IntentFilter();
            filter.addAction(BROADCAST_GUIDE_ACTIVITY);
            registerReceiver(receiver, filter);

            //TODO LIST:需要加入LoginActivity
            // mUri = getIntent().getParcelableExtra(LoginActivity.EXTRA_BACKGROUND);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, new ParallaxFragment())
                        .commit();
            }

        } catch (Exception e) {
            Log.d("ZHANGFA", e.getMessage());

        }

    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    public Uri getUri() {
        return mUri;
    }
}
