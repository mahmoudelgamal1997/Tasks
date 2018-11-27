package com.example2017.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addTask extends AppCompatActivity {

    EditText taskDetails;
    SharedPreferences sh;
    Button ok,pickLocation;
    String mTeamTypeArray[] , mDurationArray[];
    String mTeamType=null;
    String mNameSelected;
    String mDurationSelected;
    Spinner mTeamTypeSpinner,mNameSpinner,mDurationSpinner;
    DatabaseReference names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        init();

        mTeamTypeArray = new String[]{getString(R.string.solo), getString(R.string.multi)};
        mDurationArray=new String []{getString(R.string.shortTime),getString(R.string.longTime)};

        mTeamType=mTeamTypeArray[0];
        mDurationSelected=mDurationArray[0];


        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mDurationArray);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mDurationSpinner.setAdapter(adapter2);

        mTeamTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mDurationSelected=mDurationArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mTeamTypeArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mTeamTypeSpinner.setAdapter(adapter);

        mTeamTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mTeamType=mTeamTypeArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        FirebaseListAdapter<ClientItem> firebaseListAdapter  = new FirebaseListAdapter<ClientItem>(
                this,
                ClientItem.class,
                R.layout.textname,
                names

        ) {
            @Override
            protected void populateView(View v, ClientItem model, int position) {

                TextView txt=(TextView)v.findViewById(R.id.name);
                txt.setText(model.getName());
                mNameSelected= model.getName();


            }
        };

        mNameSpinner.setAdapter(firebaseListAdapter);



        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addTask.this,TargetMap.class);
                startActivity(i);

            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String details = taskDetails.getText().toString().trim();
                double v1=Double.parseDouble(sh.getString("pickLat", "111"));
                double v2=Double.parseDouble(sh.getString("pickLon", "111"));

                LatLng targetLocation=new LatLng(v1,v2);

                Mission.Builder mission = new Mission.Builder(sh.getString("MemberKey", "emputy"))
                        .details(details)
                        .getContext(getApplicationContext())
                        .setTeamType(mTeamType)
                        .setName(mNameSelected)
                        .setTargetLocation(targetLocation)
                        .setDuration(mDurationSelected);
                mission.build();


            }
        });


    }



    public void init() {

        sh = getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE);
        names= FirebaseDatabase.getInstance().getReference().child("username");
        taskDetails = (EditText) findViewById(R.id.task_details);
        ok = (Button) findViewById(R.id.addTask);
        mTeamTypeSpinner=(Spinner)findViewById(R.id.task_type);
        mNameSpinner=(Spinner)findViewById(R.id.spinner_name);
        mDurationSpinner=(Spinner)findViewById(R.id.spinner_duration);
        pickLocation=(Button)findViewById(R.id.pickLocation);
    }



}

