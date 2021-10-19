package com.example.autosellingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.UserListener;
import com.example.autosellingapp.items.RateItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFollowAdapter extends RecyclerView.Adapter<UserFollowAdapter.MyViewHolder>{

    private ArrayList<UserItem> arrayList_user;
    private UserListener listener;
    private Context context;

    public UserFollowAdapter(ArrayList<UserItem> arrayList_user, UserListener listener) {
        this.arrayList_user = arrayList_user;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_user_follow_item, parent, false);
        return new UserFollowAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_name.setText(arrayList_user.get(position).getFullName());

        String image = arrayList_user.get(position).getImage();

        if(!image.isEmpty()){
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.user_ic)
                    .into(holder.iv_user);
        }


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(arrayList_user.get(position).getUid());
        ArrayList<RateItem> arrayList_rating = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList_rating.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RateItem rateItem = dataSnapshot.getValue(RateItem.class);
                    arrayList_rating.add(rateItem);
                }

                int rating_count = arrayList_rating.size();
                float rating_score = 0;


                for(RateItem rate : arrayList_rating){
                    rating_score += rate.getScore();
                }

                float score = rating_score/rating_count;

                holder.tv_rating_score.setText((rating_count == 0)?"0":String.format("%1$,.1f", score));
                holder.tv_rating_count.setText("("+rating_count+")");
                holder.ratingBar.setRating(score);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        holder.cv_user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(arrayList_user.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList_user.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_rating_score, tv_rating_count;
        CircleImageView iv_user;
        RatingBar ratingBar;
        CardView cv_user_item;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rating_score = itemView.findViewById(R.id.tv_rating_score);
            tv_rating_count = itemView.findViewById(R.id.tv_rating_count);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            iv_user = itemView.findViewById(R.id.iv_user);
            cv_user_item = itemView.findViewById(R.id.cv_user_item);
        }
    }
}
