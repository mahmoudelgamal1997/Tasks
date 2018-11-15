package com.example2017.android.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskDetailsForMembers extends AppCompatActivity {

   private DatabaseReference memberData;
   private SharedPreferences sh;
   private TextView adress,finishtime,state,report,details,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_for_members);

        init();

        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
        memberData= FirebaseDatabase.getInstance().getReference().child("Clients").child(sh.getString( "MemberKey","emputy")).child(sh.getString( "MissionKey","emputy"));

        memberData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adress.setText(dataSnapshot.child("MissionAdress").getValue(String.class));
                finishtime.setText(dataSnapshot.child("time").getValue(String.class));
                state.setText(dataSnapshot.child("state").getValue(String.class));
                report.setText(dataSnapshot.child("ReportText").getValue(String.class));
                type.setText(dataSnapshot.child("MissionType").getValue(String.class));
                details.setText(dataSnapshot.child("taskDetails").getValue(String.class));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


public void init(){
    adress=(TextView)findViewById(R.id.adress);
    finishtime=(TextView)findViewById(R.id.finishTime);
    state=(TextView)findViewById(R.id.state);
    report=(TextView)findViewById(R.id.taskReport);
    details=(TextView)findViewById(R.id.details);
    type=(TextView)findViewById(R.id.type);


}
}
