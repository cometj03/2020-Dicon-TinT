package com.sunrin.tint.Screen.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    private ArrayList<PostModel> arrayList;
    private SearchFragment context;

    public SearchAdapter(ArrayList<PostModel> arrayList, SearchFragment context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        for(String s : arrayList.get(position).getImages()) {
            Glide.with(holder.itemView)
                    .load(s)
                    .into(holder.iv_image);
        }

        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_subTitle.setText(arrayList.get(position).getSubTitle());
        holder.tv_content.setText(arrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() { return (null != arrayList ? arrayList.size() : 0); }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        TextView tv_title;
        TextView tv_subTitle;
        TextView tv_content;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subTitle = itemView.findViewById(R.id.tv_subTitle);
            tv_content = itemView.findViewById(R.id.tv_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
