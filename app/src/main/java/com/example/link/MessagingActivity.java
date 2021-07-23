package com.example.link;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.link.adapters.ChatAdapter;
import com.example.link.databinding.ActivityMessagingBinding;
import com.example.link.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class MessagingActivity extends AppCompatActivity {
    ActivityMessagingBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.msgActivityUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user2).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // this is where showing messages is set
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter= new ChatAdapter(messageModels,this);
        binding.msgActivityRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.msgActivityRecyclerView.setLayoutManager(layoutManager);

        //making sender and receiver node
        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        //showing messages
        database.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            messageModels.add(model);
                        }
                        //recycler view uptade in real time
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //sending text
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String message =  binding.etTypeMessage.getText().toString();
               final MessageModel model = new MessageModel(senderId,message);
               model.setTimeStamp(new Date().getTime());
               binding.etTypeMessage.setText("");
               //saving a message
               database.getReference().child("Chats").child(senderRoom).push()
                       .setValue(model)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                        database.getReference().child("Chats").child(receiverRoom).push()
                                .setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                   }
               });
            }
        });

    }
}