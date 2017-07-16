package com.casic.bluebot.activity;

import com.casic.bluebot.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;


@EActivity(R.layout.activity_base_annotation)
public class BackActivity extends BaseActivity {

    @AfterViews
    protected final void annotationDispayHomeAsUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OptionsItem(android.R.id.home)
    protected final void annotaionClose() {
        onBackPressed();
    }
}
