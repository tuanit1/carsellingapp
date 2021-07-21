package com.example.autosellingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.ColorListener;
import com.example.autosellingapp.items.ColorItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ColorItem> arrayList;
    private ColorListener listener;

    public ColorListAdapter(ArrayList<ColorItem> arrayList, ColorListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public ColorListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_color_item, parent, false);
        return new ColorListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ColorListAdapter.MyViewHolder holder, int position) {
        if(arrayList.get(position).getColor_id() != -1){
            holder.iv_color.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor_code()));
        }
        holder.tv_color_name.setText(arrayList.get(position).getColor_name());
        holder.ll_color_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(arrayList.get(position).getColor_id(), arrayList.get(position).getColor_name());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_color_name;
        RelativeLayout ll_color_item;
        ImageView iv_color;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_color_name = itemView.findViewById(R.id.tv_color_name);
            iv_color = itemView.findViewById(R.id.iv_color);
            ll_color_item = itemView.findViewById(R.id.rl_item_color);
        }
    }
}
