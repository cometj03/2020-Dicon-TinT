package com.sunrin.tint.Screen;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.R;

public class ShowLBActivity extends AppCompatActivity {

    // TODO: Complete this activity

    LookBookModel data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lookbook);

        data = (LookBookModel) getIntent().getSerializableExtra("lb_item");

        Toolbar toolbar = findViewById(R.id.lb_toolbar);
        toolbar.setTitle("룩북");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
