package com.example.autosellingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.EquipmentListener;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.MyItem;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.MyViewHolder> {

    private ArrayList<EquipmentItem> arrayList;
    private Context context;
    private EquipmentListener listener;

    public EquipmentAdapter(ArrayList<EquipmentItem> arrayList, EquipmentListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_equipment, parent, false);
        return new EquipmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_equip_name.setText(arrayList.get(position).getEquip_name());
        holder.ckb_equip.setChecked(arrayList.get(position).isChecked());
        holder.ckb_equip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                arrayList.get(holder.getAdapterPosition()).setChecked(isChecked);
                listener.onClick(arrayList.get(holder.getAdapterPosition()).getEquip_id(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setAdapterData(ArrayList<EquipmentItem> arrayList){
        this.arrayList = arrayList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_equip_name;
        RelativeLayout rl_item_equip;
        CheckBox ckb_equip;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_equip_name = itemView.findViewById(R.id.tv_equip);
            rl_item_equip = itemView.findViewById(R.id.rl_item_equip);
            ckb_equip = itemView.findViewById(R.id.ckb_equip);
        }
    }
}
