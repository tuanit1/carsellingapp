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
import androidx.viewpager.widget.PagerAdapter;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.MyListener;
import com.example.autosellingapp.utils.Constant;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PickImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> arrayList;
    private MyListener listener;
    private static final int UPLOAD = 489;
    private static final int ATTACH = 484;
    private int TYPE = 0;

    public void setType(boolean type){
        this.TYPE = type ? ATTACH : UPLOAD;
    }

    public PickImageAdapter(ArrayList<String> arrayList, MyListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_car_image_post, container, false);;

        ImageView iv_car_image = view.findViewById(R.id.iv_car_image);
        ImageView iv_delete = view.findViewById(R.id.iv_delete);

        switch (TYPE){
            case UPLOAD:
                iv_delete.setVisibility(View.GONE);
                break;
            case ATTACH:
                iv_delete.setVisibility(View.VISIBLE);
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrayList.remove(position);
                        notifyDataSetChanged();
                        listener.onClick();
                    }
                });
                break;
        }

        Picasso.get()
                .load(arrayList.get(position))
                .placeholder(R.drawable.placeholder_rec)
                .into(iv_car_image);



        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return POSITION_NONE;
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
