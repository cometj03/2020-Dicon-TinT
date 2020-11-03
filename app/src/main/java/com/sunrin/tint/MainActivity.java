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
import com.sunrin.tint.Feed.FeedFragment;
import com.sunrin.tint.Posting.PostingFragment;
import com.sunrin.tint.Profile.ProfileFragment;
import com.sunrin.tint.Search.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 프래그먼트 TabLayout Data
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
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);    // 타이틀 안 보이기

        init(); // 초기 설정

        //***** Fragment (TabLayout) *****//
        // 프래그먼트 객체 ArrayList에 담기
        frag_list.add(new FeedFragment());  // 첫번째 프래그먼트
        frag_list.add(new SearchFragment());  // 두번째 프래그먼트
        frag_list.add(new PostingFragment());  // 세번째 프래그먼트
        frag_list.add(new ProfileFragment());   // 네번째 프래그먼트
        addIconsToList();   // 아이콘 담기

        // 뷰페이저 설정
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        // TabLayout 아이콘 적용
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icon_list.get(i));
        }
        // 선택 안 된 탭 색깔 변경
        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.pink_200);
                tab.getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.gray);
                tab.getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);
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