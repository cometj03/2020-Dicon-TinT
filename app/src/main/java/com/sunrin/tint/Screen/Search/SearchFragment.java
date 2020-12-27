package com.sunrin.tint.Screen.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.ShowPostActivity;
import com.sunrin.tint.Util.FirebaseLoadPost;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PostModel> postAll;
    private ArrayList<PostModel> posters;
    private SearchView searchView;

    private LinearLayout recommendLayout;
    private LinearLayout noneResultLayout;
    private LinearLayout resultLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //사용될 화면
        recommendLayout = (LinearLayout)view.findViewById(R.id.recommendLayout);
        noneResultLayout = (LinearLayout)view.findViewById(R.id.noneResultLayout);
        resultLayout = (LinearLayout)view.findViewById(R.id.resultLayout);

        LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater1.inflate(R.layout.recommend_layout, recommendLayout, true);
        inflater1.inflate(R.layout.none_result_layout, noneResultLayout, true);

        //추천 검색어 onClick
        TextView recommend1 = view.findViewById(R.id.recommend1);
        TextView recommend2 = view.findViewById(R.id.recommend2);
        TextView recommend3 = view.findViewById(R.id.recommend3);
        TextView recommend4 = view.findViewById(R.id.recommend4);
        TextView recommend5 = view.findViewById(R.id.recommend5);
        TextView recommend11 = view.findViewById(R.id.recommend11);
        TextView recommend12 = view.findViewById(R.id.recommend12);
        TextView recommend13 = view.findViewById(R.id.recommend13);
        TextView recommend14 = view.findViewById(R.id.recommend14);
        TextView recommend15 = view.findViewById(R.id.recommend15);

        recommend1.setOnClickListener(this);
        recommend2.setOnClickListener(this);
        recommend3.setOnClickListener(this);
        recommend4.setOnClickListener(this);
        recommend5.setOnClickListener(this);
        recommend11.setOnClickListener(this);
        recommend12.setOnClickListener(this);
        recommend13.setOnClickListener(this);
        recommend14.setOnClickListener(this);
        recommend15.setOnClickListener(this);


        postAll = new ArrayList<>();
        posters = new ArrayList<>();

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화

        Context context = view.getContext();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SearchAdapter(posters, this);
        recyclerView.setAdapter(adapter); //recyclerView에 adapter 연결

        setrecommendLayout();

        getData();
        //SearchView 함수
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //recyclerView로 띄울 리스트 초기화
                if(!posters.isEmpty()) {
                    posters.clear();
                }

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
                    if(adapter != null){
                        adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                    }
                }

                //Fragment화면 조정
                if(posters.isEmpty() && noneResultLayout.getVisibility() == View.GONE){
                    setNoneResultLayout();
                } else if(!posters.isEmpty() && resultLayout.getVisibility() == View.GONE){
                    setResultLayout();
                }

                return true;
            } //검색 버튼을 눌렀을 때

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length() == 0 && recommendLayout.getVisibility() == View.GONE){
                    setrecommendLayout();
                }
                return true;
            } //입력 값이 달라질 때
        });

        //item onClickListener
        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(mContext, ShowPostActivity.class);
                intent.putExtra("item", posters.get(pos));
                startActivity(intent);
            }
        });

        return view;
    }

    private void setrecommendLayout(){ //recommandLayout을 보여줌
        if(noneResultLayout.getVisibility() == View.VISIBLE){
            noneResultLayout.setVisibility(View.GONE);
        }
        if(resultLayout.getVisibility() == View.VISIBLE){
            resultLayout.setVisibility(View.GONE);
        }
        if(recommendLayout.getVisibility() == View.GONE){
            recommendLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setNoneResultLayout(){ //noneResultLayout을 보여줌
        if(recommendLayout.getVisibility() == View.VISIBLE){
            recommendLayout.setVisibility(View.GONE);
        }else{
            resultLayout.setVisibility(View.GONE);
        }
        noneResultLayout.setVisibility(View.VISIBLE);
    }

    private void setResultLayout(){ //ResultLayout을 보여줌
        if(recommendLayout.getVisibility() == View.VISIBLE){
            recommendLayout.setVisibility(View.GONE);
        }else{
            noneResultLayout.setVisibility(View.GONE);
        }
        resultLayout.setVisibility(View.VISIBLE);
    }

    private void getData(){
        //Firebase에서 전체 데이터 가져오기
        FirebaseLoadPost.LoadPosts(
                postModels -> {
                    postAll = (ArrayList<PostModel>) postModels;
                },
                errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.recommend1:
            case R.id.recommend11:
                searchView.setQuery("틴트", true);
                break;
            case R.id.recommend2:
            case R.id.recommend12:
                searchView.setQuery("새해", true);
                break;
            case R.id.recommend3:
            case R.id.recommend13:
                searchView.setQuery("새학기", true);
                break;
            case R.id.recommend4:
            case R.id.recommend14:
                searchView.setQuery("패션", true);
                break;
            case R.id.recommend5:
            case R.id.recommend15:
                searchView.setQuery("학생", true);
        }

        searchView.clearFocus();
    }
}