package com.casic.bluebot.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.casic.bluebot.R;
import com.casic.bluebot.bean.UserInfo;
import com.readystatesoftware.viewbadger.BadgeView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */

@EFragment(R.layout.fragment_navigation_drawer0)
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
/*    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;*/


    //TODO LIST:append  by zhangfan
    final int radioIds[] = {
            R.id.radio0,
            R.id.radio1,
            R.id.radio2
    };

    @ViewById
    TextView name;
    @ViewById
    TextView sign;
    @ViewById
    TextView follows;
    @ViewById
    TextView fans;
    @ViewById
    CircleImageView circleIcon;

    BadgeView badgeDevice;
    BadgeView badgeMessage;
    RadioButton radios[] = new RadioButton[radioIds.length];


    View.OnClickListener clickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < radios.length; ++i) {
                if (v.equals(radios[i])) {
                    selectItem(i);
                } else {
                    radios[i].setChecked(false);
                }
            }
        }
    };

    private boolean mFirstDisplay = true;

    public NavigationDrawerFragment() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @AfterViews
    public void init() {

        //TODO LIST:加载用户信息
        UserInfo mockUser = new UserInfo();
        mockUser.setNickName("predator");
        mockUser.setUsername("zhangfan");
        setControlContent(mockUser);

        for (int i = 0; i < radioIds.length; ++i) {
            radios[i] = (RadioButton) getView().findViewById(radioIds[i]);
            radios[i].setOnClickListener(clickItem);
        }

        radios[0].setChecked(true);

        badgeDevice = (BadgeView) getView().findViewById(R.id.badge0);
        badgeDevice.setText("3");

        badgeMessage = (BadgeView) getView().findViewById(R.id.badge1);
        badgeMessage.setText("10");

        if (mFirstDisplay) {
            //TODO LIST:第一次打开要更新用户信息

        }

    }

    private void setControlContent(UserInfo user) {
        name.setText(user.getUsername());
        sign.setText(user.getNickName());
        follows.setText(String.valueOf("20"));
        fans.setText(String.valueOf("100"));
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void showDrawer(boolean show) {
        if (show) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        } else {
            mDrawerLayout.closeDrawers();
        }
    }

    @Click
    void userInfo() {
      //TODO LIST：点击可查看用户信息
    }


    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBarActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.empty,  /* "open drawer" description for accessibility */
                R.string.empty  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (mCallbacks != null) {
                    mCallbacks.onNavigationDrawerItemSelected(mCurrentSelectedPosition);
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                //TODO LIST：可以在这里去请求服务器的数据，改变左侧菜单栏的信息
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public ActionBarActivity getActionBarActivity() {
        return (ActionBarActivity) getActivity();
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }

        //TODO LIST：此处可将该fragment注册为监听全局事件的观察者
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        //TODO LIST：此处可取消该fragment注册为监听全局事件的观察者

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {

        ActionBar actionBar = getActionBarActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public  interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
