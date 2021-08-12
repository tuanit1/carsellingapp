package com.example.autosellingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PickImageAdapter extends RecyclerView.Adapter<PickImageAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Uri> arrayList;

    public PickImageAdapter(ArrayList<Uri> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_image_pick_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_count.setText("Image " + (position + 1));
        Picasso.get()
                .load(arrayList.get(position))
                .placeholder(R.drawable.placeholder_rec)
                .into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_count;
        ImageView iv_img;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_count = itemView.findViewById(R.id.tv_count);
            iv_img = itemView.findViewById(R.id.iv_img);

        }
    }

}
