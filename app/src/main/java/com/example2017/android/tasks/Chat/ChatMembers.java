package com.example2017.android.tasks.Chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example2017.android.tasks.R;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ChatMembers extends AppCompatActivity {

    DatabaseReference chatMembers;
    ListView listView;
    FirebaseListAdapter<ChatMessage> firebaseListAdapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_members);

        listView=(ListView)findViewById(R.id.chatMember);

        chatMembers=FirebaseDatabase.getInstance().getReference().child("username");

        chatMembers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseListAdapter=new FirebaseListAdapter<ChatMessage>(
                this,
                ChatMessage.class,
                R.layout.chat_member_listview,
                chatMembers
                ) {
            @Override
            protected void populateView(View v, ChatMessage model, final int position) {

                if ( ! model.getName().equals(name)){
                TextView txt=(TextView)v.findViewById(R.id.ChatName);
                SetImage(v,getApplicationContext(),model.getProfileImage());

                txt.setText(model.getName());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i=new Intent(ChatMembers.this,ChatPrivate.class);
                        i.putExtra("id",getRef(position).getKey().toString());
                        startActivity(i);
                    }
                });

            }

            }
        };


        listView.setAdapter(firebaseListAdapter);


    }


    public void SetImage(View v,final Context context, final String uri)
    {

        final ImageView img=(ImageView)v.findViewById(R.id.ChatProfileImage);


        Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

                Picasso.with(context).load(uri).into(img);
            }
        });

    }

}
