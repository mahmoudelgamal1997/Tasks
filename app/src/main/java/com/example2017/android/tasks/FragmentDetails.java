package com.example2017.android.tasks;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by M7moud on 10-Nov-18.
 */
public class FragmentDetails extends Fragment {

    DatabaseReference details;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.details_fragment,null);

        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        details = FirebaseDatabase.getInstance().getReference().child("Clients").child(userId);

        final TextView taskNumber=(TextView)view.findViewById(R.id.taskNo);
        final TextView taskAdress=(TextView)view.findViewById(R.id.taskAdress);
        final TextView taskDetails=(TextView)view.findViewById(R.id.taskdetails);
        final TextView taskState=(TextView)view.findViewById(R.id.taskState);
        final Button but =(Button)view.findViewById(R.id.but_finish);

      final SharedPreferences sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );


        details.child(sh.getString( "data","emputy"  )).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {


            final String state=dataSnapshot.child("state").getValue(String.class);
            String TaskDetails=dataSnapshot.child("taskDetails").getValue(String.class);
            String adress=dataSnapshot.child("adress").getValue(String.class);
            String number = sh.getString("postion","1");

            taskNumber.setText(getString(R.string.task)+number);
            taskDetails.setText(TaskDetails);
            taskState.setText(state);
            taskAdress.setText(adress);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (state.equalsIgnoreCase("under")){
                        details.child(sh.getString( "data","emputy"  )).child("state").setValue("done");
                    }
                }
            });
        }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

        });

        return view;
    }
}
