package com.example2017.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskDetailsForMembers extends AppCompatActivity {

   private DatabaseReference memberData;
   private SharedPreferences sh;
   private TextView adress,finishtime,startTime,state,report,details, TeamType;
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

                final String latitude =dataSnapshot.child("latitude").getValue(String.class);
                final String longitude =dataSnapshot.child("longitude").getValue(String.class);

                adress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(),AdressMap.class);
                        i.putExtra("latit",latitude);
                        i.putExtra("longit",longitude);
                        startActivity(i);
                    }
                });


                startTime.setText(dataSnapshot.child("TimeFrom").getValue(String.class));
                finishtime.setText(dataSnapshot.child("TimeTo").getValue(String.class));
                state.setText(dataSnapshot.child("state").getValue(String.class));
                report.setText(dataSnapshot.child("ReportText").getValue(String.class));
                TeamType.setText(dataSnapshot.child("teamType").getValue(String.class));
                details.setText(dataSnapshot.child("taskDetails").getValue(String.class));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


public void init(){
    adress=(TextView)findViewById(R.id.Taskadress2);
    startTime=(TextView)findViewById(R.id.startTime);
    finishtime=(TextView)findViewById(R.id.finishTime);
    state=(TextView)findViewById(R.id.state);
    report=(TextView)findViewById(R.id.taskReport);
    details=(TextView)findViewById(R.id.details);
    TeamType =(TextView)findViewById(R.id.type);


}
}
