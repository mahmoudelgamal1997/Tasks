package com.example2017.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberDetails extends AppCompatActivity {

    DatabaseReference memberData;
    SharedPreferences sh;
    ListView listView;
    FloatingActionButton fbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        listView=(ListView)findViewById(R.id.memberListView);
        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
        memberData= FirebaseDatabase.getInstance().getReference().child("Clients").child(sh.getString( "MemberKey","emputy"));
        fbtn=(FloatingActionButton)findViewById(R.id.fab);

        fbtn.setVisibility(View.INVISIBLE);
        //admin only
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Sp17QHHa3vYoPh35JV2nWQ0zjFQ2")){

            fbtn.setVisibility(View.VISIBLE);

        }

        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii= new Intent(MemberDetails.this,addTask.class);
                startActivity(ii);

            }
        });
        FirebaseListAdapter<ClientItem> firebaseListAdapter=new FirebaseListAdapter<ClientItem>(
                this,
                ClientItem.class,
               R.layout.tasklistview,
                memberData
        ) {
            @Override
            protected void populateView(View v, ClientItem model, final int position) {

                TextView txt=(TextView)v.findViewById(R.id.taskPostion);
                TextView txt2=(TextView)v.findViewById(R.id.state);
                txt.setText("task "+(position+1));
                txt2.setText(model.getState());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
                        SharedPreferences.Editor  mydata=sh.edit();
                        mydata.putString( "MissionKey",getRef(position).getKey().toString() );
                        mydata.commit();

                        Intent i =new Intent(MemberDetails.this,TaskDetailsForMembers.class);
                        startActivity(i);

                    }
                });
            }
        };

listView.setAdapter(firebaseListAdapter);
    }
}
