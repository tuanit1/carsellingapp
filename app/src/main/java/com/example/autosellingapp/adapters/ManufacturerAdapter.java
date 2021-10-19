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
import com.example.autosellingapp.interfaces.InterAdListener;
import com.example.autosellingapp.interfaces.ManuListener;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ManufacturerAdapter extends RecyclerView.Adapter<ManufacturerAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ManufacturerItem> arrayList;
    private ManuListener listener;
    private String type;
    private Methods methods;

    public ManufacturerAdapter(Context context, String type, ArrayList<ManufacturerItem> arrayList ,ManuListener listener){
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        this.type = type;
        this.methods = new Methods(context, interAdListener);
    }

    private InterAdListener interAdListener = new InterAdListener() {
        @Override
        public void onClick(int position) {
            listener.onClick(arrayList.get(position).getManu_id());
        }
    };

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(type.equals("home")){
            view = inflater.inflate(R.layout.item_manufactor_home, parent, false);
        }else {
            view = inflater.inflate(R.layout.item_manu_viewmore, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_manu_name.setText(arrayList.get(position).getManu_name());
        Picasso.get()
                .load(Constant.SERVER_URL + "images/manu_image/" + arrayList.get(position).getManu_thumb())
                .placeholder(R.drawable.placeholder_rec)
                .into(holder.iv_manu_thumb);
        holder.ll_item_manu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methods.showInter(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setAdapterData(ArrayList<ManufacturerItem> arrayList){
        this.arrayList = arrayList;
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
