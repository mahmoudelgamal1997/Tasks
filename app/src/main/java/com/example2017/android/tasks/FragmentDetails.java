package com.example2017.android.tasks;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by M7moud on 10-Nov-18.
 */
public class FragmentDetails extends Fragment {

    DatabaseReference details;
    PopupWindow mpopup;
    DatabaseReference temp;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

                    //pop up window
                   View view2 = inflater.inflate(R.layout.add_report, null);
                    mpopup = new PopupWindow(view2, ActionBar.LayoutParams.FILL_PARENT,
                            ActionBar.LayoutParams.WRAP_CONTENT, true); // Creation of popup
                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.showAtLocation(view2, Gravity.CENTER, 0, 0); // Displaying popup
                    mpopup.setIgnoreCheekPress();

                    temp=  details.child(sh.getString( "data","emputy"));

                    final EditText mName    = (EditText) view2.findViewById(R.id.user_name);
                    final EditText mReportAdress  = (EditText) view2.findViewById(R.id.report_adress);
                    final EditText mMissionType    = (EditText) view2.findViewById(R.id.mission_type);
                    final EditText mReportText = (EditText) view2.findViewById(R.id.user_report);

                    Button button = (Button) view2.findViewById(R.id.upload);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        temp.child("time").setValue(Time());
                        temp.child("name").setValue(mName.getText().toString());
                        temp.child("ReportAdress").setValue(mReportAdress.getText().toString());
                        temp.child("MissionType").setValue(mMissionType.getText().toString());
                        temp.child("ReportText").setValue(mReportText.getText().toString());
                        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

                        mpopup.dismiss();

                        }




                    });








                }
            });
        }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

        });

        return view;
    }



    public String Time(){
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day =calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        final String CollectionDate=""+year+"-"+month+"-"+day+"  "+hour+":"+minute;
        return CollectionDate;
    }


}
