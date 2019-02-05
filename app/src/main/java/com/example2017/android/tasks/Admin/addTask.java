package com.example2017.android.tasks.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example2017.android.tasks.ClientItem;
import com.example2017.android.tasks.Mission;
import com.example2017.android.tasks.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class addTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText taskDetails;
    TextView timeFrom,timeTo;
    SharedPreferences sh;
    Button ok,pickLocation, startTimer,endTimer;
    String mTeamTypeArray[] , mDurationArray[];
    String mTeamType=null;
    String mNameSelected;
    String mDurationSelected;
    Spinner mTeamTypeSpinner,mNameSpinner,mDurationSpinner;
    DatabaseReference names;
    int yearFinal,monthFinal,dayFinal,hour,minute,hourFinal,minuteFinal=0;
    String period="";
    String  CollectionTimeFrom,  CollectionTimeTo="";
    // to know which button selected From or To button
    String buttonSelected="";
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


        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int  month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);

                buttonSelected="startTimer";
                DatePickerDialog datePicker=new DatePickerDialog(addTask.this,addTask.this,
                        year,
                        month,
                        day
                );
                datePicker.show();
            }
        });


        endTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int  month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                buttonSelected="endTimer";

                DatePickerDialog datePicker=new DatePickerDialog(addTask.this,addTask.this,
                        year,
                        month,
                        day
                );
                datePicker.show();
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
                        .setDurationType(mDurationSelected)
                        .setTime(CollectionTimeFrom,CollectionTimeTo);
                mission.build();
                finish();

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
        startTimer=(Button)findViewById(R.id.startTimer);
        endTimer=(Button)findViewById(R.id.endTimer);
        timeFrom=(TextView)findViewById(R.id.timeFrom);
        timeTo=(TextView)findViewById(R.id.timeTo);

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal=i;
        monthFinal=i1+1;
        dayFinal=i2;

        TimePickerDialog timePickerDialog=new TimePickerDialog(addTask.this, addTask.this,
                 hour,
                minute,false

        );

        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {


        hourFinal=i;
        minuteFinal=i1;

        period = "صباحا";
        if (hourFinal>12){
            hourFinal-=12;
            period="مساءا" ;

        }else if (hourFinal==0){
            hourFinal=12;
        }

        if (buttonSelected.equalsIgnoreCase("startTimer")){
              CollectionTimeFrom=yearFinal+"-"+monthFinal+"-"+dayFinal+"  "+hourFinal+":"+minuteFinal+" "+ period;
              timeFrom.setText(CollectionTimeFrom);

        }else {
            CollectionTimeTo=yearFinal+"-"+monthFinal+"-"+dayFinal+"  "+hourFinal+":"+minuteFinal+" "+ period;
            timeTo.setText(CollectionTimeTo);

        }


    }
    }








