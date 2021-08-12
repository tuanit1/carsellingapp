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
import com.example.autosellingapp.items.ChatItem;
import com.example.autosellingapp.utils.Constant;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<ChatItem> arrayList_chat;
    private Context context;
    private String image;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(ArrayList<ChatItem> arrayList_chat, String image) {
        this.arrayList_chat = arrayList_chat;
        this.image = image;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.MyViewHolder holder, int position) {
        ChatItem chat = arrayList_chat.get(holder.getAdapterPosition());

        switch (chat.getType()){
            case "image":
                Picasso.get()
                        .load(chat.getMessage())
                        .placeholder(R.drawable.placeholder_rec)
                        .into(holder.iv_msg_image);
                holder.iv_msg_image.setVisibility(View.VISIBLE);
                holder.tv_message.setVisibility(View.GONE);
                break;
            default:
                holder.tv_message.setText(chat.getMessage());
                holder.tv_message.setVisibility(View.VISIBLE);
                holder.iv_msg_image.setVisibility(View.GONE);
                break;

        }

        if(holder.iv_user != null){
            Picasso.get()
                    .load(Constant.SERVER_URL + "images/user_image" + image)
                    .placeholder(R.drawable.user_ic)
                    .into(holder.iv_user);
        }

        if(holder.getAdapterPosition() == arrayList_chat.size()-1){
            if(chat.getIsseen().equals("true")){
                holder.tv_seen.setText("Seen");
            }else{
                holder.tv_seen.setText("Delivered");
            }
        }else{
            holder.tv_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList_chat.get(position).getSender_uid().equals(Constant.UID)){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList_chat.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_message, tv_seen;
        ImageView iv_user, iv_msg_image;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_message = itemView.findViewById(R.id.tv_message);

            iv_user = itemView.findViewById(R.id.iv_user);

            iv_msg_image = itemView.findViewById(R.id.iv_msg_image);

            tv_seen = itemView.findViewById(R.id.tv_seen);
        }
    }
}
