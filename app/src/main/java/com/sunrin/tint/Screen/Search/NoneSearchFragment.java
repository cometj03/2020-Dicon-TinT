package com.sunrin.tint.Screen.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunrin.tint.R;

public class NoneSearchFragment extends Fragment {

    public NoneSearchFragment() {
        // Required empty public constructor
    }

    public static NoneSearchFragment newInstance() {
        NoneSearchFragment fragment = new NoneSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_none_search, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}