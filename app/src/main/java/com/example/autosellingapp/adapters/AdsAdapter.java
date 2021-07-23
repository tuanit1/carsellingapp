package com.example.autosellingapp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<UserItem> arrayList_user;
    private ArrayList<MyItem> arrayList_city;
    private Methods methods;

    public AdsAdapter(Methods methods, ArrayList<AdsItem> arrayList_ads, ArrayList<CarItem> arrayList_car, ArrayList<UserItem> arrayList_user, ArrayList<MyItem> arrayList_city) {
        this.methods = methods;
        this.arrayList_ads = arrayList_ads;
        this.arrayList_car = arrayList_car;
        this.arrayList_user = arrayList_user;
        this.arrayList_city = arrayList_city;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_ads_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        UserItem user = methods.getUserItemByUsername(arrayList_user, arrayList_ads.get(position).getUsername());
        CarItem car = methods.getCarItemByID(arrayList_car, arrayList_ads.get(position).getCar_id());
        MyItem city = methods.getMyItemByID(arrayList_city, arrayList_ads.get(position).getCity_id());


        holder.tv_seller_name.setText(user.getFullName());
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
        holder.tv_city.setText(city.getName());
        holder.tv_likes.setText(String.valueOf(arrayList_ads.get(position).getAds_likes()));

        Picasso.get()
                .load(Constant.SERVER_URL + "images/" + user.getImage())
                .placeholder(R.drawable.user_ic)
                .into(holder.iv_seller_pic);

        Picasso.get()
                .load(Constant.SERVER_URL + "images/" + car.getCar_imageList().get(1))
                .placeholder(R.drawable.item_manu_placeholder)
                .into(holder.iv_car_pic);


        if(methods.isFavourite(arrayList_user, arrayList_ads.get(position).getAds_id())){
            Picasso.get()
                    .load(R.drawable.heart_check_ic)
                    .into(holder.iv_likes);
        }else{
            Picasso.get()
                    .load(R.drawable.heart_uncheck_ic)
                    .into(holder.iv_likes);
        }

        holder.cv_item_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList_ads.size();
    }

    public void setValue(ArrayList<AdsItem> tempArray) {
        arrayList_ads = tempArray;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_seller_name, tv_post_time, tv_car_name, tv_price, tv_city, tv_likes;
        ImageView iv_seller_pic, iv_car_pic, iv_likes;
        CardView cv_item_ads;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_seller_name = itemView.findViewById(R.id.tv_seller_name);
            tv_post_time = itemView.findViewById(R.id.tv_post_time);
            tv_car_name = itemView.findViewById(R.id.tv_car_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_likes = itemView.findViewById(R.id.tv_likes);

            iv_seller_pic = itemView.findViewById(R.id.iv_seller_pic);
            iv_car_pic = itemView.findViewById(R.id.iv_car_pic);
            iv_likes = itemView.findViewById(R.id.iv_likes);

            cv_item_ads = itemView.findViewById(R.id.cv_item_ads);

        }
    }
}

