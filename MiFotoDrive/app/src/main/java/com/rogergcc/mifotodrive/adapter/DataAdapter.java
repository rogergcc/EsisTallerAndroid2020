package com.rogergcc.mifotodrive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rogergcc.mifotodrive.R;
import com.rogergcc.mifotodrive.model.ImageUrl;

import java.util.ArrayList;

/**
 * Created by ROGERGCC on 10/02/2020.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ImageUrl> imageUrls;
    private Context context;
    public DataAdapter(Context context, ArrayList<ImageUrl> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);
    }
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
        }
    }
    public void update(ArrayList<ImageUrl> datas){
        imageUrls.clear();
        imageUrls.addAll(datas);
        notifyDataSetChanged();
    }
}