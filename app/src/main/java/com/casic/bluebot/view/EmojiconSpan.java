package com.casic.bluebot.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import com.casic.bluebot.common.DrawableTool;
import com.casic.bluebot.common.MyImageGetter;
import com.casic.bluebot.fragments.EmojiFragment;


class EmojiconSpan extends DynamicDrawableSpan {
    private final Context mContext;
    private final int mResourceId;
    private Drawable mDrawable;
    private boolean mIsMonkey;

    public EmojiconSpan(Context context, String iconName) {
        super();
        mContext = context;

        String name = EmojiFragment.textToMonkdyMap.get(iconName);

        if (name == null) {
            name = iconName;
            mIsMonkey = false;
        } else {
            mIsMonkey = true;
        }

        mResourceId = MyImageGetter.getResourceId(name);
    }

    @Override
    public Drawable getDrawable() {
        if (mDrawable == null) {
            try {
                mDrawable = mContext.getResources().getDrawable(mResourceId);
                DrawableTool.zoomDrwable(mDrawable, mIsMonkey);
            } catch (Exception e) {
            }
        }
        return mDrawable;
    }
}