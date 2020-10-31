package com.sunrin.tint;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class FeedFragment extends Fragment {

    Context mContext;
    private int[] filterNames = {
            R.string.filter_name1, R.string.filter_name2, R.string.filter_name3, R.string.filter_name4, R.string.filter_name5 };

    private ChipGroup chipGroup;
    private HorizontalScrollView scrollView;
    private ImageButton filterToggle;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        chipGroup = view.findViewById(R.id.chipGroup);
        scrollView = view.findViewById(R.id.scrollView);
        filterToggle = view.findViewById(R.id.filterToggle);

        spawnFilterChip();
        filterToggle.setOnClickListener(v -> {
            if (scrollView.getVisibility() == View.VISIBLE)
                scrollView.setVisibility(View.INVISIBLE);
            else
                scrollView.setVisibility(View.VISIBLE);
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void spawnFilterChip() {
        for (int i = 0; i < 5; i++) {
            Chip chip = new Chip(mContext);
            chip.setText(getString(filterNames[i]));
            chip.setCheckable(true);

            // chip.setCheckedIconVisible(false);
            chipGroup.addView(chip);
        }
    }
}
