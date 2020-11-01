package com.sunrin.tint;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ItemViewHolder> {

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView[] feed_img;
        ImageView userProfile;
        ImageButton commentBtn, shareBtn;
        TextView title, subTitle, timeInterval, userName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            feed_img[0] = itemView.findViewById(R.id.feed_img);
            userProfile = itemView.findViewById(R.id.feed_userProfile);
            commentBtn = itemView.findViewById(R.id.feed_comment);
            shareBtn = itemView.findViewById(R.id.feed_share);
            title = itemView.findViewById(R.id.feed_title);
            subTitle = itemView.findViewById(R.id.feed_subTitle);
            timeInterval = itemView.findViewById(R.id.feed_timeInterval);
            userName = itemView.findViewById(R.id.feed_userName);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
