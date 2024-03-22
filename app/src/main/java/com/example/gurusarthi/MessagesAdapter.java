package com.example.gurusarthi;

import static com.example.gurusarthi.ChatWin.reciverIImg;
import static com.example.gurusarthi.ChatWin.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MsgModel> messagesAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<MsgModel> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MsgModel messages = messagesAdpterArrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        String string ="";
        String image ="";
        if (messages.getMessage().contains("#")){

            String[] m =  messages.getMessage().split("#");
            if (m.length >= 2) {
                string = m[0];
                image = m[1];
            }
        }else {
            string =messages.getMessage();
        }

        if (holder.getClass()==senderVierwHolder.class){
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            if (messages.imageUri!=null){
                viewHolder.msgtxt.setVisibility(View.GONE);
                viewHolder.senderImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.imageUri) .placeholder(R.drawable.photocamera) // Placeholder image resource
                        .error(R.drawable.photocamera) // Error image resource
                 .into(viewHolder.senderImage);

            }else {
                viewHolder.msgtxt.setVisibility(View.VISIBLE);
                viewHolder.senderImage.setVisibility(View.GONE);
                viewHolder.msgtxt.setText(string);
            }

            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
            if(image!=""){ viewHolder.image.setVisibility(View.VISIBLE);} else{
                viewHolder.image.setVisibility( View.GONE);
            }

            viewHolder.image.setImageBitmap(base64ToBitmap(image));

        }else { reciverViewHolder viewHolder = (reciverViewHolder) holder;
            if (messages.imageUri!=null){
                viewHolder.msgtxt.setVisibility(View.GONE);
                viewHolder.receiverImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.imageUri) .placeholder(R.drawable.photocamera) // Placeholder image resource
                        .error(R.drawable.photocamera) // Error image resource
                .into(viewHolder.receiverImage);

            }else {
                viewHolder.msgtxt.setVisibility(View.VISIBLE);
                viewHolder.receiverImage.setVisibility(View.GONE);
                viewHolder.msgtxt.setText(string);
            }

            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);


        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MsgModel messages = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    class  senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView image,senderImage;

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
            image = itemView.findViewById(R.id.image);
            senderImage = itemView.findViewById(R.id.senderImage);

        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        ImageView receiverImage;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
            receiverImage = itemView.findViewById(R.id.receiverImage);
        }
    }
}
