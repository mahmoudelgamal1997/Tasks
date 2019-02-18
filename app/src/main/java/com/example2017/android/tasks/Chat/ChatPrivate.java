package com.example2017.android.tasks.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example2017.android.tasks.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatPrivate extends AppCompatActivity {

    DatabaseReference chat,temp,RecieveMessage;
    RecyclerView recyclerView;
    EditText Message;
    Button butSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_private);

        define();

        Intent i = getIntent();
        final String RecieverId = i.getStringExtra("id");
        final String SenderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chat = FirebaseDatabase.getInstance().getReference().child("chat");
        RecieveMessage = FirebaseDatabase.getInstance().getReference().child("chat").child(SenderId).child(RecieverId);

        recyclerView = (RecyclerView) findViewById(R.id.ChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(SenderId).child(RecieverId).hasChildren() ) {

                chat=chat.child(SenderId).child(RecieverId);
                    display();
                    Toast.makeText(ChatPrivate.this, "Donee", Toast.LENGTH_SHORT).show();

                }
                if (dataSnapshot.child(RecieverId).child(SenderId).hasChildren() ) {
                    chat=chat.child(RecieverId).child(SenderId);

                    display();
                    Toast.makeText(ChatPrivate.this, "Donee", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });










        butSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                chat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(ChatPrivate.this, dataSnapshot+"", Toast.LENGTH_SHORT).show();
                        if (dataSnapshot.child(SenderId).child(RecieverId).hasChildren()){

                            // check if reference exist or not

                            temp = chat.child(SenderId).child(RecieverId).push();
                            temp.child("message").setValue(Message.getText().toString());
                            temp.child("from").setValue(SenderId);
                            Message.setText("");



                        }else if (dataSnapshot.child(RecieverId).child(SenderId).hasChildren()){

                            //check by reverse
                            temp = chat.child(RecieverId).child(SenderId).push();
                            temp.child("message").setValue(Message.getText().toString());
                            temp.child("from").setValue(RecieverId);
                            Message.setText("");

                        }

                        else{

                            //then database reference isn't exist
                            //So we create it

                            temp = chat.child(SenderId).child(RecieverId).push();
                            temp.child("message").setValue(Message.getText().toString());
                            temp.child("from").setValue(SenderId);
                            Message.setText("");


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });




    }




    public void display(){
        FirebaseRecyclerAdapter<ChatMessage,view_holder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<ChatMessage, view_holder>(
                ChatMessage.class,
                R.layout.message_cardview,
                view_holder.class,
                chat
        ) {
            @Override
            protected void populateViewHolder(final view_holder viewHolder, final ChatMessage model, final int position) {


                Toast.makeText(ChatPrivate.this, model.getfrom()+model.getMessage(), Toast.LENGTH_SHORT).show();
                viewHolder.SetData(model.getfrom(),model.getTime(),model.getMessage());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

   public static class view_holder extends RecyclerView.ViewHolder{

        View view;
        public view_holder(View itemView)
        {
            super(itemView);
            view=itemView;
        }


        public void SetData(String Id ,String Time , String Message){
            final TextView txtName=(TextView)view.findViewById(R.id.textview_username);
            TextView txtTime=(TextView)view.findViewById(R.id.textview_timeSent);
            TextView txtMessage=(TextView)view.findViewById(R.id.textview_Message);


            if (Id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                txtName.setText("You");

            }else {
                DatabaseReference username=FirebaseDatabase.getInstance().getReference().child("username").child(Id);
                username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        txtName.setText(dataSnapshot.child("name").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        //    txtTime.setText(Time);
            txtMessage.setText(Message);


        }


   }


   public void define(){

        recyclerView=(RecyclerView) findViewById(R.id.ChatView);
        Message=(EditText)findViewById(R.id.sendMessage);
        butSend=(Button)findViewById(R.id.but_send);


    }
}
