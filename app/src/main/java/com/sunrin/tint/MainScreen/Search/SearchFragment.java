package com.sunrin.tint.MainScreen.Search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.FirebaseLoadPost;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostModel> postAll;
    private ArrayList<PostModel> posters;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        postAll = new ArrayList<>();
        posters = new ArrayList<>();

        SearchView searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화

        Context context = view.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SearchAdapter(posters, this);
        recyclerView.setAdapter(adapter); //recyclerView에 adapter 연결

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            } //검색 버튼을 눌렀을 때

            @Override
            public boolean onQueryTextChange(String s) {

                //recyclerView로 띄울 리스트 초기화
                posters.clear();

                //Firebase에서 전체 데이터 가져오기
                FirebaseLoadPost.LoadPosts(
                        postModels -> {
                            postAll = (ArrayList<PostModel>) postModels;
                        },
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show()
                );


                //입력 값과 FireBase에 있는 데이터의 값을 비교
                //비교해서 입력한 문자열이 있는 데이터만 ArrayList(posters)에 저장해서 SearchAdapter로 보내기

                //데이터의 제목, 소제목, 내용의 replace(" ", "")를 사용해 공백을 없앤후
                //입력한 문자열이 있는지 contains()를 사용해 검사
                s = s.replace(" ", "").toLowerCase();

                for (PostModel post : postAll) {
                    if (post.getTitle().replace(" ", "").toLowerCase().contains(s) ||
                            post.getSubTitle().replace(" ", "").toLowerCase().contains(s) ||
                            post.getContent().replace(" ", "").toLowerCase().contains(s)
                    ) {
                        posters.add(post);
                    }
                    adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                }
                return true;
            } //입력 값이 달라질 때
        });

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}