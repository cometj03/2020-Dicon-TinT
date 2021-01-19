package com.sunrin.tint.Screen.Show;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class SlideLBAdapter extends FragmentStatePagerAdapter {

    List<ShowLBFragment> fragmentList;

    public SlideLBAdapter(FragmentManager fm, List<ShowLBFragment> list) {
        super(fm);
        this.fragmentList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
