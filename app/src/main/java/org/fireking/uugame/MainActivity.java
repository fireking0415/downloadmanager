package org.fireking.uugame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.fireking.uudownload.DownloadRequest;
import org.fireking.uugame.uis.fragments.HomeFragment;
import org.fireking.uugame.uis.fragments.SpeedFragment;
import org.fireking.uugame.uis.fragments.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNavigation;
    private Fragment mCurrentFragment;
    private HomeFragment mHomeFragment;
    private SpeedFragment mSpeedFragment;
    private UserFragment mUserFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        List<BottomNavigation.NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new BottomNavigation.NavigationItem(R.drawable.icon_tabber_game_normal,
                R.drawable.icon_tabber_game_checked, "游戏"));
        navigationItems.add(new BottomNavigation.NavigationItem(R.drawable.icon_tabber_speed_normal,
                R.drawable.icon_tabber_speed_checked, "加速"));
        navigationItems.add(new BottomNavigation.NavigationItem(R.drawable.icon_tabber_user_normal,
                R.drawable.icon_tabber_user_checked, "我的"));

        bottomNavigation.setData(navigationItems, new BottomNavigation.OnBottomNavigationListener() {
            @Override
            public void onBottomNavigation(int position) {
                if (mCurrentFragment != null) {
                    getSupportFragmentManager().beginTransaction().hide(mCurrentFragment).commit();
                }
                if (position == 0) {
                    if (mHomeFragment == null) {
                        mHomeFragment = HomeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().add(R.id.contentPanel,
                                mHomeFragment, HomeFragment.class.getSimpleName()).commit();
                    }
                    getSupportFragmentManager().beginTransaction().show(mHomeFragment).commit();
                    mCurrentFragment = mHomeFragment;
                } else if (position == 1) {
                    if (mSpeedFragment == null) {
                        mSpeedFragment = SpeedFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().add(R.id.contentPanel,
                                mSpeedFragment, SpeedFragment.class.getSimpleName()).commit();
                    }
                    getSupportFragmentManager().beginTransaction().show(mSpeedFragment).commit();
                    mCurrentFragment = mSpeedFragment;
                } else if (position == 2) {
                    if (mUserFragment == null) {
                        mUserFragment = UserFragment.newInstance();
                        getSupportFragmentManager().beginTransaction().add(R.id.contentPanel,
                                mUserFragment, UserFragment.class.getSimpleName()).commit();
                    }
                    getSupportFragmentManager().beginTransaction().show(mUserFragment).commit();
                    mCurrentFragment = mUserFragment;
                }
            }
        });

        //默认在第一个item
        bottomNavigation.performClick(0);

    }
}
