package com.example.gurusarthi;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    ImageView chatIcon,cumbut;

    String senderRoom,reciverRoom;
    RecyclerView messageAdpter;
    ArrayList<MsgModel> messagesArrayList;
    MessagesAdapter mmessagesAdpter;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
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
        cumbut = findViewById(R.id.camBut);
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


        cumbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });
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
                sendMsg(textmsg.getText().toString(),null);

            }
        });

    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            openCamera();
        }
    }

    private void sendMsg(String message,Uri imageUri) {
        if (message.isEmpty() && imageUri == null) {
            Toast.makeText(ChatWin.this, "Enter a message or capture an image", Toast.LENGTH_SHORT).show();
            return;
        }
        textmsg.setText("");
        Date date = new Date();
        if (imageUri != null) {
            // Upload image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("chat_images").child("image/" + System.currentTimeMillis() + ".webp");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Create message object
                            MsgModel messageModel = new MsgModel("", SenderUID, date.getTime(), imageUrl);
                            sendMessageToDatabase(messageModel);
                        }).addOnFailureListener(e -> {
                            // Handle image upload failure
                            Toast.makeText(ChatWin.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle image upload failure
                        Toast.makeText(ChatWin.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Create message object for text message
            MsgModel messageModel = new MsgModel(message, SenderUID, date.getTime(), null);
            sendMessageToDatabase(messageModel);
        }
    }

    // Method to send message object to Firebase Realtime Database
    private void sendMessageToDatabase(MsgModel messageModel) {
        // Push message to sender's and receiver's chat rooms in Firebase Realtime Database
        DatabaseReference senderRef = database.getReference().child("chats").child(senderRoom).child("messages").push();
        senderRef.setValue(messageModel).addOnCompleteListener(senderTask -> {
            if (senderTask.isSuccessful()) {
                DatabaseReference receiverRef = database.getReference().child("chats").child(reciverRoom).child("messages").push();
                receiverRef.setValue(messageModel).addOnCompleteListener(receiverTask -> {
                    if (receiverTask.isSuccessful()) {
                        // Send push notification to the receiver
                        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(receiverToken, senderName, messageModel.getMessage(), getApplicationContext(), ChatWin.this);
                        fcmNotificationsSender.SendNotifications();
                    } else {
                        // Handle failure to send message to the receiver
                    }
                });
            } else {
                // Handle failure to send message to the sender
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
        sendMsg(location+"\n"+selectedOptions.getTitle()+"#"+base64,null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                openCamera();
            } else {
                // Camera permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    Uri imageUri;
    private void openCamera() {
        // Code to open camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 10);
        }

    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imageUri = BitmapUtils.bitmapToUri(this, imageBitmap);
                sendMsg("image",imageUri);
            }else {
                Toast.makeText(this, "Failed to retrieve image URI", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Image Discarded", Toast.LENGTH_SHORT).show();
        }
    }

}