package com.example2017.android.tasks.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example2017.android.tasks.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatPrivate extends AppCompatActivity {

    DatabaseReference chat;
   private ListView listView;
   FirebaseListAdapter<ChatMessage> firebaseListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_private);

        Intent i=getIntent();
        String RecieverId =  i.getStringExtra("id");
        String SenderId   = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chat= FirebaseDatabase.getInstance().getReference().child("chat").child(RecieverId+""+SenderId);

        firebaseListAdapter=new FirebaseListAdapter<ChatMessage>(
                this,
                ChatMessage.class,
                android.R.layout.simple_list_item_1,
                chat
        ) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView textView=(TextView)v.findViewById(android.R.id.text1);
                textView.setText(model.getMessage().toString());
            }
        };



    }
}
