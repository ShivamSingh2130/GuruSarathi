package com.example.gurusarthi;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWin extends AppCompatActivity implements ChatIconSelectedListner {
    String reciverimg, reciverUid,reciverName,SenderUID,receiverToken;
    CircleImageView profile;
    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public  static String senderImg,senderName;
    public  static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;
    ImageView chatIcon;

    String senderRoom,reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<MsgModel> messagesArrayList;
    MessagesAdapter mmessagesAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");
        receiverToken =  getIntent().getStringExtra("TOKEN");

        messagesArrayList = new ArrayList<>();

        sendbtn = findViewById(R.id.sendbtnn);
        chatIcon = findViewById(R.id.chatIcon);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messageAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdpter.setLayoutManager(linearLayoutManager);
        mmessagesAdpter = new MessagesAdapter(ChatWin.this,messagesArrayList);
        messageAdpter.setAdapter(mmessagesAdpter);


        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(""+reciverName);

        SenderUID =  firebaseAuth.getUid();

        senderRoom = SenderUID+reciverUid;
        reciverRoom = reciverUid+SenderUID;

        chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatOptionsDialog chat= ChatOptionsDialog.newInstance("chat");
                chat.show(getSupportFragmentManager(), "MyBottomSheetDialogFragment");
            }
        });

        DatabaseReference  reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference  chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MsgModel messages = dataSnapshot.getValue(MsgModel.class);
                    messagesArrayList.add(messages);
                }
                mmessagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    senderImg= snapshot.child("profilepic").getValue().toString();
                    senderName = snapshot.child("userName").getValue().toString();
                    reciverIImg=reciverimg;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg(textmsg.getText().toString());

            }
        });

    }

    private void sendMsg(String message ) {
        if (message.isEmpty()){
            Toast.makeText(ChatWin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        textmsg.setText("");
        Date date = new Date();
        MsgModel messagess = new MsgModel(message,SenderUID,date.getTime());

        database=FirebaseDatabase.getInstance();
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(receiverToken,senderName
                                                ,message,getApplicationContext(),ChatWin.this);
                                        fcmNotificationsSender.SendNotifications();
                                    }
                                });
                    }
                });
    }
    public static String drawableToBase64(Context context, @DrawableRes int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    @Override
    public void onOptionsSelected(ChatAlertOpt selectedOptions,String location) {
       String base64 = drawableToBase64(this,selectedOptions.getIcon());
        sendMsg(location+"\n"+selectedOptions.getTitle()+"#"+base64);
    }
}