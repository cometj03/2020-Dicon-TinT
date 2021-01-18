package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Models.PostModel;
import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ItemViewHolder> implements Filterable {

    private List<PostModel> mData, mDataFiltered;
    Context mContext;
    ViewGroup emptyView;

    public PostGridAdapter(ViewGroup emptyView) {
        this.emptyView = emptyView;
        this.mData = new ArrayList<>();
        this.mDataFiltered = new ArrayList<>();
    }

    public void setList(List<PostModel> list) {
        this.mData = list;
        if(mDataFiltered == null || mDataFiltered.isEmpty())
            mDataFiltered = mData;

        if (emptyView != null)
            if (getItemCount() <= 0)
                emptyView.setVisibility(View.VISIBLE);
            else
                emptyView.setVisibility(View.GONE);
    }

    public List<PostModel> getList() {
        return mDataFiltered;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.post_item_grid, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        PostModel item = mDataFiltered.get(position);

        if(!item.getId().isEmpty() && !item.getImages().isEmpty()) {
            // 정사각형으로 잘라서 보여줌
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(holder.thumbNail)
                    .load((item.getImages().get(0)))
                    .apply(requestOptions)
                    .into(holder.thumbNail);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataFiltered == null)
            return 0;
        return mDataFiltered.size();
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                Filter filter = Filter.valueOf(constraint.toString());

                Log.e(TAG, "performFiltering: asdfasdf : " + constraint.toString());

                if (constraint.toString().equals("ALL")) {
                    FilterResults results = new FilterResults();
                    results.values = mData;
                    return results;
                } else {
                    // 필터로 찾은 후 반환
                    mDataFiltered = new ArrayList<PostModel>() {
                        {
                            for (PostModel p : mData) {
                                if (p.getFilters() != null && p.getFilters().contains(filter))
                                    add(p);
                            }
                        }
                    };
                }

                // results에 결과 담고 return
                FilterResults results = new FilterResults();
                results.values = mDataFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // results : return 값
                mDataFiltered = (List<PostModel>) results.values;
                notifyDataSetChanged();

                if (emptyView != null)
                    if (getItemCount() <= 0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);
            }
        };
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, View cover, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.itemClickListener = mListener;
    }

    private OnItemClickListener itemClickListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbNail;
        View cover;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbNail = itemView.findViewById(R.id.post_thumb_nail);
            cover = itemView.findViewById(R.id.cover);

            thumbNail.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, cover, pos);
            });
        }
    }
}
