package com.sunrin.tint.Screen.Show;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;

public class ShowLBActivity extends AppCompatActivity {

    // TODO: Complete this activity

    ViewPager viewPager;
    SlideLBAdapter adapter;
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

        Toast.makeText(this, "position : " + currentPosition, Toast.LENGTH_SHORT).show();

        fragmentList = new ArrayList<ShowLBFragment>() {
            {
                for (String s : lookBookIDList)
                    add(new ShowLBFragment(s));
            }
        };

        viewPager = findViewById(R.id.lookbook_container);
        adapter = new SlideLBAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setPageTransformer(true, new ZoomAnimation());
        viewPager.setAdapter(adapter);
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
}
