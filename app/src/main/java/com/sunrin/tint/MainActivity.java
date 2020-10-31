package com.sunrin.tint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> frag_list = new ArrayList<>();
    private ArrayList<Integer> icon_list = new ArrayList<>();

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 대신 툴바 사용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 타이틀 안 보이기

        tabLayout = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);

        // 프래그먼트 객체 ArrayList에 담기
        for (int i = 0; i < 4; i++) {
            frag_list.add(new FeedFragment());
        }
        addIconsToList();   // 아이콘 담기

        // 뷰페이저 설정
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icon_list.get(i));
        }
    }

    private void addIconsToList() {
        icon_list.add(R.drawable.icon_feed);
        icon_list.add(R.drawable.icon_search);
        icon_list.add(R.drawable.icon_write);
        icon_list.add(R.drawable.icon_profile);
    }

    // 뷰페이저 구성을 위해 어댑터 생성
    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) { super(fm); }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return frag_list.get(position);
        }

        @Override
        public int getCount() {
            return frag_list.size();
        }
    }
}