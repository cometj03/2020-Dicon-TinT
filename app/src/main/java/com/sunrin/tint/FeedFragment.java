package com.sunrin.tint;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FeedFragment extends Fragment {

    Context mContext;
    private final int[] filterNames = {
            R.string.filter_name1, R.string.filter_name2, R.string.filter_name3, R.string.filter_name4, R.string.filter_name5 };
    private ArrayList<Chip> chips = new ArrayList<>();
    private ArrayList<Boolean> chipsBooleans = new ArrayList<>();


    private ChipGroup chipGroup;
    private HorizontalScrollView scrollView;
    private ImageButton filterToggle;

    // chip 클릭 리스너 생성
    private CompoundButton.OnCheckedChangeListener chipChangeListener = (CompoundButton buttonView, boolean isChecked) -> {
        int tag = (int) buttonView.getTag();
        chipsBooleans.set(tag, isChecked);
    };


    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        init(view);

        filterToggle.setOnClickListener(v -> {
            if (scrollView.getVisibility() == View.VISIBLE)
                scrollView.setVisibility(View.INVISIBLE);
            else
                scrollView.setVisibility(View.VISIBLE);
        });

        // chip을 클릭함
        chipGroup.setOnCheckedChangeListener((ChipGroup group, int checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            Toast.makeText(mContext, chip.getTag() + "번째", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void init(View view) {
        chipGroup = view.findViewById(R.id.chipGroup);
        scrollView = view.findViewById(R.id.scrollView);
        filterToggle = view.findViewById(R.id.filterToggle);

        chips.add(view.findViewById(R.id.chip1));
        chips.add(view.findViewById(R.id.chip2));
        chips.add(view.findViewById(R.id.chip3));
        chips.add(view.findViewById(R.id.chip4));
        chips.add(view.findViewById(R.id.chip5));
        for (int i = 0; i < 5; i++) {
            chipsBooleans.add(false);
            chips.get(i).setTag(i);
            chips.get(i).setOnCheckedChangeListener(chipChangeListener);
        }
    }

//    private void spawnFilterChip() {
//        for (int i = 0; i < 5; i++) {
//            Chip chip = new Chip(mContext);
//            chip.setText(getString(filterNames[i]));
//            chip.setCheckable(true);
//
//            // chip.setCheckedIconVisible(false);
//            chipGroup.addView(chip);
//        }
//    }
}
