package com.sunrin.tint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sunrin.tint.MainScreen.Feed.FeedFragment;
import com.sunrin.tint.MainScreen.Posting.PostingFragment;
import com.sunrin.tint.MainScreen.Profile.ProfileFragment;
import com.sunrin.tint.MainScreen.Search.SearchFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 프래그먼트 TabLayout Data
    private List<Fragment> frag_list = new ArrayList<>();
    private List<Integer> icon_list = new ArrayList<>();

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toast.makeText(this, TimeAgo.getTimeAgo("2019-06-08 10:30:23"), Toast.LENGTH_SHORT).show();


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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setIcon(icon_list.get(position));
        }).attach();

        // TabLayout 아이콘 적용
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icon_list.get(i));
            if (i != 0) {
                // 2, 3, 4 번째 탭 색깔 바꿈
                int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.gray);
                tabLayout.getTabAt(i).getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedColor = ContextCompat.getColor(getApplicationContext(), R.color.pink_200);
                tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int unSelectedColor = ContextCompat.getColor(getApplicationContext(), R.color.gray);
                tab.getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Tab 다시 선택됨
                switch (tab.getPosition()) {
                    case 0:
                        // TODO: recyclerView goes to top
                        break;
                }
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewPager2_container);
        // pager = findViewById(R.id.pager);
    }

    private void addIconsToList() {
        icon_list.add(R.drawable.icon_feed);
        icon_list.add(R.drawable.icon_search);
        icon_list.add(R.drawable.icon_write);
        icon_list.add(R.drawable.icon_profile);
    }

    // 뷰페이저 구성을 위해 어댑터 생성
    class ViewPagerAdapter extends FragmentStateAdapter {


        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return frag_list.get(position);
        }

        @Override
        public int getItemCount() {
            return frag_list.size();
        }
    }
}