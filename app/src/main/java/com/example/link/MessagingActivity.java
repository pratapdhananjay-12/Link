package com.example.link;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.link.databinding.ActivityMessagingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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

        String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.msgActivityUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user2).into(binding.profileImage);

    }
}