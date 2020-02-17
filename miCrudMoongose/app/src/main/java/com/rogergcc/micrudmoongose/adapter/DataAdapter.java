package com.rogergcc.micrudmoongose.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.rogergcc.micrudmoongose.R;
import com.rogergcc.micrudmoongose.model.Productos;


import java.util.ArrayList;

/**
 * Created by ROGERGCC on 10/02/2020.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<Productos> productosArrayList;
    private Context context;
    public DataAdapter(Context context, ArrayList<Productos> productosArrayList) {
        this.context = context;
        this.productosArrayList = productosArrayList;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_products, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        Glide.with(context).load(productosArrayList.get(i).getImageUrl()).into(viewHolder.img);
        Productos pro = productosArrayList.get(i);
        String registro = "Nombre : "+pro.getName()+ "\n";
        registro +="precio :" +pro.getPrice();
        viewHolder.txt_producto.setText(registro);
    }
    @Override
    public int getItemCount() {
        return productosArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView img;
        TextView txt_producto;
        public ViewHolder(View view) {
            super(view);
//            img = view.findViewById(R.id.imageView);
            txt_producto = view.findViewById(R.id.txt_producto);
        }
    }

}