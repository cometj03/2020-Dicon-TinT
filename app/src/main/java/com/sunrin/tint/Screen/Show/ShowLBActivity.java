package com.sunrin.tint.Screen.Show;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;

public class ShowLBActivity extends AppCompatActivity {

    // TODO: Complete this activity

    ViewPager2 viewPager2;
    private List<ShowLBFragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lookbook);

        Toolbar toolbar = findViewById(R.id.lb_toolbar);
        toolbar.setTitle("룩북");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> lookBookIDList = getIntent().getStringArrayListExtra("lb_list");
        int currentPosition = getIntent().getIntExtra("click_position", 0);

        fragmentList = new ArrayList<ShowLBFragment>() {
            {
                for (String s : lookBookIDList)
                    add(new ShowLBFragment(s));
            }
        };

        viewPager2 = findViewById(R.id.lookbook_container);
        viewPager2.setPageTransformer(new ZoomAnimation());
        SlideLBAdapter adapter = new SlideLBAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class SlideLBAdapter extends FragmentStateAdapter {

        public SlideLBAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}
