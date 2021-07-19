package com.example.autosellingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.items.ManufacturerItem;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManufacturerAdapter extends RecyclerView.Adapter<ManufacturerAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ManufacturerItem> arrayList;

    public ManufacturerAdapter(ArrayList<ManufacturerItem> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_manufactor_home, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_manu_name.setText(arrayList.get(position).getManu_name());
        Picasso.get()
                .load(arrayList.get(position).getManu_thumb())
                .placeholder(R.drawable.item_manu_placeholder)
                .into(holder.iv_manu_thumb);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_manu_name;
        ImageView iv_manu_thumb;
        LinearLayout ll_item_manu;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_manu_name = itemView.findViewById(R.id.tv_manu_name);
            iv_manu_thumb = itemView.findViewById(R.id.iv_manu_thumb);
            ll_item_manu = itemView.findViewById(R.id.ll_item_manu);
        }
    }
}
