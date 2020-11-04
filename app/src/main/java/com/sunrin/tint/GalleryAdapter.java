package com.sunrin.tint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.Instant;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.BitSet;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private GalleryActivity galleryActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public MyViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    public GalleryAdapter(GalleryActivity galleryActivity, ArrayList<String> myDataset) {
        mDataset = myDataset;
        this.galleryActivity = galleryActivity;

    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilepath",mDataset.get(holder.getAdapterPosition()));
                galleryActivity.setResult(Activity.RESULT_OK,resultIntent);
                galleryActivity.finish();
            }
        });

        ImageView imageView = cardView.findViewById(R.id.imageView);
        //Glide.with
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

