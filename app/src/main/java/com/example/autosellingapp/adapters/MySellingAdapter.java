package com.example.autosellingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.EditDeleteSellingListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MySellingAdapter extends RecyclerView.Adapter<MySellingAdapter.MyViewHolder> {

    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private Context context;
    private Methods methods;
    private EditDeleteSellingListener listener;

    public MySellingAdapter(Methods methods, ArrayList<AdsItem> arrayList_ads, ArrayList<CarItem> arrayList_car, EditDeleteSellingListener listener) {
        this.arrayList_ads = arrayList_ads;
        this.arrayList_car = arrayList_car;
        this.methods = methods;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_myselling_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        CarItem car = methods.getCarItemByID(arrayList_car, arrayList_ads.get(position).getCar_id());

        Date currentDate = new Date();
        Date postDate = arrayList_ads.get(position).getAds_posttime();

        long diffInTime = currentDate.getTime() - postDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
        long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
        long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime)/365l;
        long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime)/30l;
        long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);

        if(diffInYear < 1){
            if(diffInMonth < 1){
                if(diffInDay < 1){
                    if(diffInHour < 1){
                        if(diffInMinutes < 1){
                            holder.tv_post_time.setText("Just now");
                        }else {
                            holder.tv_post_time.setText(diffInMinutes + " minutes ago");
                        }
                    }else {
                        holder.tv_post_time.setText(diffInHour + " hours ago");
                    }
                }else{
                    holder.tv_post_time.setText(diffInDay + " days ago");
                }
            }else {
                holder.tv_post_time.setText(diffInMonth + " months ago");
            }
        }else {
            holder.tv_post_time.setText(diffInYear + " years ago");
        }

        holder.tv_car_name.setText(car.getCar_name());
        holder.tv_price.setText("$ "+String.format("%1$,.0f", arrayList_ads.get(position).getAds_price()));
        holder.tv_likes.setText(String.valueOf(arrayList_ads.get(position).getAds_likes()));
        holder.tv_condition.setText((car.isNew())?"New":"Used");

        if(car.getCar_imageList().isEmpty()){
            Picasso.get()
                    .load(car.getCar_imagelist_link().get(0))
                    .placeholder(R.drawable.placeholder_rec)
                    .into(holder.iv_car_pic);
        }else {
            Picasso.get()
                    .load(Constant.SERVER_URL + "images/car_image/" + car.getCar_imageList().get(0))
                    .placeholder(R.drawable.placeholder_rec)
                    .into(holder.iv_car_pic);
        }


        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(arrayList_ads.get(position));
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(arrayList_ads.get(position));
            }
        });
        holder.btn_mark_as_sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMark(arrayList_ads.get(position));
            }
        });

        if(arrayList_ads.get(position).isAds_isAvailable()){
            holder.btn_mark_as_sold.setText("MARK AS SOLD");
            holder.ll_sold.setVisibility(View.GONE);
        }else{
            holder.btn_mark_as_sold.setText("MARK AS AVAILABLE");
            holder.ll_sold.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList_ads.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cv_item_ads;
        TextView tv_car_name, tv_price, tv_condition, tv_likes, tv_post_time;
        ImageView iv_car_pic;
        Button btn_delete, btn_edit, btn_mark_as_sold;
        RelativeLayout rl_ads_item;
        LinearLayout ll_sold;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cv_item_ads = itemView.findViewById(R.id.cv_item_ads);
            tv_car_name = itemView.findViewById(R.id.tv_car_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_condition = itemView.findViewById(R.id.tv_condition);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            tv_post_time = itemView.findViewById(R.id.tv_post_time);
            iv_car_pic = itemView.findViewById(R.id.iv_car_pic);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_mark_as_sold = itemView.findViewById(R.id.btn_mark_as_sold);
            rl_ads_item = itemView.findViewById(R.id.rl_ads_item);
            ll_sold = itemView.findViewById(R.id.ll_sold);
        }
    }
}
