package com.example2017.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberDetails extends AppCompatActivity {

    DatabaseReference memberData;
    SharedPreferences sh;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        listView=(ListView)findViewById(R.id.memberListView);
        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
        memberData= FirebaseDatabase.getInstance().getReference().child("Clients").child(sh.getString( "MemberKey","emputy"));

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
