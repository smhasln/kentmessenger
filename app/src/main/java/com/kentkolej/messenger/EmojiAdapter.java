package com.kentkolej.messenger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

    private ArrayList<Integer> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    EmojiAdapter(Context context, ArrayList<Integer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_emoji_liste, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img.setImageResource(mData.get(position));
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_emoji);
        }

    }

}