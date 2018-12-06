package com.example2017.android.tasks;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by M7moud on 25-Nov-18.
 */
public  class Mission {
    DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Clients");
    DatabaseReference temp;
    String details;
    String key;
    String teamType;
    Context context;
    String NameSelected;
    String Duration;
    LatLng latLng;
    String TimeFrom,TimeTo;

    private Mission(Builder builder){
        details = builder.details;
        key = builder.key;
        context = builder.context;
        teamType = builder.teamType;
        NameSelected = builder.NameSelected;
        latLng=builder.latLng;
        Duration=builder.Duration;
        TimeFrom=builder.TimeFrom;
        TimeTo=builder.TimeTo;
        if(key != null)
        {
            temp = data.child(key).push();
            temp.child("state").setValue("under");
            temp.child("id").setValue(key);
        }
        if(details != null){ temp.child("taskDetails").setValue(details);}
        if(teamType != null){temp.child("teamType").setValue(teamType);}
        if(NameSelected != null){temp.child("name").setValue(NameSelected);}

        if(latLng != null)
        {
            temp.child("latitude").setValue(String.valueOf(latLng.latitude));
            temp.child("longitude").setValue(String.valueOf(latLng.longitude));
        }

        if (Duration!= null)
        {
            temp.child("TaskDuration").setValue(Duration);
        }

        if (TimeFrom != null && TimeTo != null )
        {
            temp.child("TimeFrom").setValue(TimeTo);
            temp.child("TimeTo").setValue(TimeTo);
        }


        Toast.makeText(context, "Mission Added Successfully", Toast.LENGTH_SHORT).show();
    }

    public static class Builder {

        String details;
        String key;
        Context context;
        String teamType;
        String NameSelected;
        String Duration;
        LatLng latLng;
        String TimeFrom,TimeTo;

        public Builder(String key)
        {

            this.key=key;

        }

        public Builder details(String details){

            this.details=details;

            return this;
        }

        public Builder setName(String nameSelected){

            this.NameSelected=nameSelected;

            return this;
        }



        public Builder getContext(Context context){

            this.context=context;

            return this;
        }


        public Builder setTeamType(String teamType){

            this.teamType=teamType;

            return this;
        }


        public Builder setTargetLocation(LatLng latLng){

            this.latLng=latLng;

            return this;
        }

        public Builder setDurationType(String Duration){

            this.Duration=Duration;

            return this;
        }


        public Builder setTime(String from,String to){

            this.TimeFrom=from;
            this.TimeTo=to;

            return this;
        }

        public Mission build()
        {
            return new Mission(this);
        }

    }
}

