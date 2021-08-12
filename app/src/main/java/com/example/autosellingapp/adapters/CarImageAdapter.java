package com.example.autosellingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.autosellingapp.R;
import com.example.autosellingapp.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CarImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> arrayList_image;

    public CarImageAdapter(Context context, ArrayList<String> arrayList_image) {
        this.context = context;
        this.arrayList_image = arrayList_image;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_car_image, container, false);
        ImageView iv_car_image = view.findViewById(R.id.iv_car_image);
        TextView tv_count_image = view.findViewById(R.id.tv_count_image);

        String count = (position + 1) + "/" + arrayList_image.size();
        tv_count_image.setText(count);

        Picasso.get()
                .load(Constant.SERVER_URL + "images/car_image/" + arrayList_image.get(position))
                .placeholder(R.drawable.placeholder_rec)
                .into(iv_car_image);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(arrayList_image != null){
            return arrayList_image.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }
}
