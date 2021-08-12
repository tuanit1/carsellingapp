package com.example.autosellingapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.UserListener;
import com.example.autosellingapp.items.ChatItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private ArrayList<UserItem> arrayList_user;
    private UserListener listener;
    private Context context;
    private String theLastMessage;

    public UserAdapter(ArrayList<UserItem> arrayList_user, UserListener listener) {
        this.arrayList_user = arrayList_user;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_user_item, parent, false);
        return new UserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_name.setText(arrayList_user.get(position).getFullName());
        holder.ll_user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(arrayList_user.get(position));
            }
        });
        Picasso.get()
                .load(Constant.SERVER_URL + "images/user_image/" + arrayList_user.get(position).getImage())
                .placeholder(R.drawable.user_ic)
                .into(holder.iv_user);
        if(arrayList_user.get(position).getStatus().equals("online")){
            holder.iv_on.setVisibility(View.VISIBLE);
            holder.iv_off.setVisibility(View.GONE);
        }else{
            holder.iv_on.setVisibility(View.GONE);
            holder.iv_off.setVisibility(View.VISIBLE);
        }

        holder.iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpMenu(v, arrayList_user.get(position).getUid());
            }
        });

        setTheLastMessage(arrayList_user.get(position), holder.tv_last_message);
    }

    @Override
    public int getItemCount() {
        return arrayList_user.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name, tv_last_message;
        CircleImageView iv_user, iv_on, iv_off;
        LinearLayout ll_user_item;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_last_message = itemView.findViewById(R.id.tv_message);

            iv_user = itemView.findViewById(R.id.iv_user);
            iv_on = itemView.findViewById(R.id.iv_on);
            iv_off =itemView.findViewById(R.id.iv_off);
            ll_user_item = itemView.findViewById(R.id.ll_user_item);
        }
    }

    private void setTheLastMessage(UserItem user, TextView last_msg){
        theLastMessage = "default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatItem chat = dataSnapshot.getValue(ChatItem.class);
                    if(chat.getReceiver_uid().equals(Constant.UID) && chat.getSender_uid().equals(user.getUid()) ||
                            chat.getReceiver_uid().equals(user.getUid()) && chat.getSender_uid().equals(Constant.UID)){
                        switch (chat.getType()){
                            case "message":
                                if(chat.getSender_uid().equals(Constant.UID)){
                                    theLastMessage = "You: " + chat.getMessage();
                                }else {
                                    theLastMessage = chat.getMessage();
                                }
                                break;
                            case "image":
                                if(chat.getSender_uid().equals(Constant.UID)){
                                    theLastMessage = "You sent a image";
                                }else {
                                    theLastMessage = user.getFullName() + " sent you a image";
                                }
                                break;
                        }

                        if(chat.getIsseen().equals("false") && chat.getSender_uid().equals(user.getUid())){
                            last_msg.setTextColor(context.getResources().getColor(R.color.text_black));
                        }else{
                            last_msg.setTextColor(context.getResources().getColor(R.color.default_text));
                        }
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        last_msg.setTextColor(context.getResources().getColor(R.color.default_text));
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void openPopUpMenu(View v, String uid){
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_user, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.popup_user_profile:
                        listener.onLongClick(uid);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
