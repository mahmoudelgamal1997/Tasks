package com.example2017.android.tasks.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example2017.android.tasks.Mission;
import com.example2017.android.tasks.R;
import com.example2017.android.tasks.api.APIInterface;
import com.example2017.android.tasks.api.Reports;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class addTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText taskDetails;
    TextView timeFrom, timeTo, txtName;
    SharedPreferences sh;
    Button ok, pickLocation, startTimer, endTimer,addAttachment;
    String mTeamTypeArray[], mDurationArray[], mProirityArray[];
    String mTeamType = null;
    String mNameSelected;
    String mDurationSelected;
    String mPrioritySelected;
    Spinner mTeamTypeSpinner, mDurationSpinner, mPrioritySpinner;
    DatabaseReference names;
    int yearFinal, monthFinal, dayFinal, hour, minute, hourFinal, minuteFinal = 0;
    String period = "";
    String CollectionTimeFrom, CollectionTimeTo = "";
    // to know which button selected From or To button
    String buttonSelected = "";
    File[] AttachmentFile;

    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        init();

        mTeamTypeArray = new String[]{getString(R.string.solo), getString(R.string.multi)};
        mDurationArray = new String[]{getString(R.string.shortTime), getString(R.string.longTime)};
        mProirityArray = new String[]{getString(R.string.high), getString(R.string.medium), getString(R.string.low)};
        mTeamType = mTeamTypeArray[0];
        mDurationSelected = mDurationArray[0];
        mPrioritySelected = mProirityArray[0];

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDurationArray);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mDurationSpinner.setAdapter(adapter2);

        mDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mDurationSelected = mDurationArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTeamTypeArray);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mTeamTypeSpinner.setAdapter(adapter);

        mTeamTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mTeamType = mTeamTypeArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mProirityArray);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mPrioritySpinner.setAdapter(adapter1);

        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mPrioritySelected = mProirityArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String key = sh.getString("MemberKey", "emputy");
        DatabaseReference name = FirebaseDatabase.getInstance().getReference().child("username").child(key);
        name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);

                mNameSelected = name;
                txtName.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addTask.this, TargetMap.class);
                startActivity(i);

            }
        });


        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        });




        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                buttonSelected = "startTimer";
                DatePickerDialog datePicker = new DatePickerDialog(addTask.this, addTask.this,
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
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                buttonSelected = "endTimer";

                DatePickerDialog datePicker = new DatePickerDialog(addTask.this, addTask.this,
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
                double v1 = Double.parseDouble(sh.getString("pickLat", "111"));
                double v2 = Double.parseDouble(sh.getString("pickLon", "111"));

                LatLng targetLocation = new LatLng(v1, v2);


                addTaskToBackEnd(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        String.valueOf(v1),
                        String.valueOf(v2),
                        mPrioritySelected,
                        details,
                        CollectionTimeFrom,
                        CollectionTimeTo,
                        AttachmentFile
                        );

                /*
                Mission.Builder mission = new Mission.Builder(sh.getString("MemberKey", "emputy"))
                        .details(details)
                        .getContext(getApplicationContext())
                        .setTeamType(mTeamType)
                        .setName(mNameSelected)
                        .setTargetLocation(targetLocation)
                        .setDurationType(mDurationSelected)
                        .setTime(CollectionTimeFrom, CollectionTimeTo)
                        .setPriority(mPrioritySelected);
                mission.build();
                finish();

           */

            }


        });


    }


    public void init() {

        sh = getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE);
        names = FirebaseDatabase.getInstance().getReference().child("username");
        taskDetails = (EditText) findViewById(R.id.task_details);
        ok = (Button) findViewById(R.id.addTask);
        mTeamTypeSpinner = (Spinner) findViewById(R.id.task_type);
        mDurationSpinner = (Spinner) findViewById(R.id.spinner_duration);
        mPrioritySpinner = (Spinner) findViewById(R.id.spinner_prority);
        pickLocation = (Button) findViewById(R.id.pickLocation);
        startTimer = (Button) findViewById(R.id.startTimer);
        endTimer = (Button) findViewById(R.id.endTimer);
        addAttachment = (Button) findViewById(R.id.addAttachment);

        timeFrom = (TextView) findViewById(R.id.timeFrom);
        timeTo = (TextView) findViewById(R.id.timeTo);
        txtName = (TextView) findViewById(R.id.txt_name);

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        TimePickerDialog timePickerDialog = new TimePickerDialog(addTask.this, addTask.this,
                hour,
                minute, false

        );

        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {


        hourFinal = i;
        minuteFinal = i1;

        period = "صباحا";
        if (hourFinal > 12) {
            hourFinal -= 12;
            period = "مساءا";

        } else if (hourFinal == 0) {
            hourFinal = 12;
        }

        if (buttonSelected.equalsIgnoreCase("startTimer")) {
            CollectionTimeFrom = yearFinal + "-" + monthFinal + "-" + dayFinal + "  " + hourFinal + ":" + minuteFinal + " " + period;
            timeFrom.setText(CollectionTimeFrom);

        } else {
            CollectionTimeTo = yearFinal + "-" + monthFinal + "-" + dayFinal + "  " + hourFinal + ":" + minuteFinal + " " + period;
            timeTo.setText(CollectionTimeTo);

        }


    }


    public void addTaskToBackEnd(String userId, String latitude, String longitude, String priority, String notes, String timeFrom, String timeTo, File[] attachments) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.server_url)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        APIInterface service = retrofit.create(APIInterface.class);


        service.SendReqToGetFeedBack("1111","1111","111","1111","1111","8888","solo","111","111",attachments).enqueue(new retrofit2.Callback<Reports>() {
            @Override
            public void onResponse(Call<Reports> call, Response<Reports> response) {
                try {
                    Log.e("success",  response.body().getMessage());
                    Log.e("success",  response.body().getStatus().toString());


                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Reports> call, Throwable t) {
                Log.e("Throwable", t.getMessage().toString());

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                     String filePath = data.getData().getPath();
                     AttachmentFile[0]  = new File(filePath);

                }
        }


    }
}