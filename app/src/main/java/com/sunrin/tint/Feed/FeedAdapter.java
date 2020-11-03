package com.sunrin.tint.Feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ItemViewHolder> {

    private ArrayList<FeedItem> mData = null;

    FeedAdapter(ArrayList<FeedItem> list) {
        mData = list;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FeedItem item = mData.get(position);

        if (item.getFeed_img() != null)
            holder.feed_img.setImageDrawable(item.getFeed_img());
        if (item.getUserProfile() != null)
            holder.userProfile.setImageDrawable(item.getUserProfile());
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.timeInterval.setText(item.getTimeInterval());
        holder.userName.setText(item.getUserName());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        /*if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(holder, position);
        } else {
            // Perform a partial update
            for (Object payload : payloads) {
                if (payload instanceof TimeFormatPayload) {
                    holder.bindTimePayload((TimeFormatPayload) payload);
                }
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView feed_img;
        ImageView userProfile;
        ImageButton commentBtn, shareBtn;
        TextView title, subTitle, timeInterval, userName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            feed_img = itemView.findViewById(R.id.feed_img);
            userProfile = itemView.findViewById(R.id.feed_userProfile);
            commentBtn = itemView.findViewById(R.id.feed_comment);
            shareBtn = itemView.findViewById(R.id.feed_share);
            title = itemView.findViewById(R.id.feed_title);
            subTitle = itemView.findViewById(R.id.feed_subTitle);
            timeInterval = itemView.findViewById(R.id.feed_timeInterval);
            userName = itemView.findViewById(R.id.feed_userName);
        }
    }
}
