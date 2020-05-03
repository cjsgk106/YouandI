package com.example.andorid.youandi.album;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andorid.youandi.R;
import com.example.andorid.youandi.model.ImageAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class albumAdapter extends RecyclerView.Adapter<albumAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public albumAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textView.setText(uploadCurrent.getName());
//        Log.w("kkkkkk", uploadCurrent.getImageUrl());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public  ImageViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }

    }
}