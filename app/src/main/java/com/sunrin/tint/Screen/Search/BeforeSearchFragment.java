package com.sunrin.tint.Screen.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunrin.tint.R;

public class BeforeSearchFragment extends Fragment {



    public BeforeSearchFragment() {
        // Required empty public constructor
    }

    public static BeforeSearchFragment newInstance() {
        BeforeSearchFragment fragment = new BeforeSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before_search, container, false);


        return view;
    }
}