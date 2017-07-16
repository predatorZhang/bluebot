package com.casic.bluebot.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casic.bluebot.R;
import com.casic.bluebot.activity.GuideActivity;
import com.casic.bluebot.activity.LoginActivity;
import com.casic.bluebot.activity.LoginActivity_;
import com.casic.bluebot.activity.RegisterActivity_;
import com.casic.bluebot.common.IndicatorView;
import com.casic.bluebot.common.parallaxpager.ParallaxContainer;


public class ParallaxFragment extends Fragment implements ViewPager.OnPageChangeListener {

    IndicatorView mIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parallax, container, false);
        mIndicatorView = (IndicatorView) view.findViewById(R.id.indicatorView);

        ParallaxContainer parallaxContainer =
                (ParallaxContainer) view.findViewById(R.id.parallax_container);

        parallaxContainer.setLooping(false);

        parallaxContainer.setupChildren(inflater,
                R.layout.parallax_view_0,
                R.layout.parallax_view_1,
                R.layout.parallax_view_2,
                R.layout.parallax_view_3,
                R.layout.parallax_view_4,
                R.layout.parallax_view_5,
                R.layout.parallax_view_6
        );

        parallaxContainer.setOnPageChangeListener(this);

        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //TODO LIST:跳转到注册Activity
               RegisterActivity_.intent(ParallaxFragment.this).start();
            }
        });

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity_.class);
                Uri uri = ((GuideActivity) getActivity()).getUri();
                if (uri != null) {
                    //TODO LIST：跳转到登录Activity
                    intent.putExtra(LoginActivity.EXTRA_BACKGROUND, uri);
                }

                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {
//        Log.d("", String.format("off %d, %f, %d", position, offset, offsetPixels));
        if (offset > 0.5) {
            mIndicatorView.setSelect(position + 1);
        } else {
            mIndicatorView.setSelect(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }



}
