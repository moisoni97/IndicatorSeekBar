package com.warkiz.indicatorseekbar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.warkiz.indicatorseekbar.fragments.ContinuousFragment;
import com.warkiz.indicatorseekbar.fragments.CustomFragment;
import com.warkiz.indicatorseekbar.fragments.DiscreteFragment;
import com.warkiz.indicatorseekbar.fragments.IndicatorFragment;
import com.warkiz.indicatorseekbar.fragments.JavaBuildFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    private final int tabsNumber;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int tabsNumber) {
        super(fragmentManager, lifecycle);
        this.tabsNumber = tabsNumber;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DiscreteFragment();
            case 2:
                return new CustomFragment();
            case 3:
                return new JavaBuildFragment();
            case 4:
                return new IndicatorFragment();
            default:
                return new ContinuousFragment();
        }
    }

    @Override
    public int getItemCount() {
        return tabsNumber;
    }
}
