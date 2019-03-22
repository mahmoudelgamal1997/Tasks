package com.example2017.android.tasks;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example2017.android.tasks.Mandop.MainContract;
import com.example2017.android.tasks.Mandop.MandopSideMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by M7moud on 10-Nov-18.
 */
public class FragmentDetails extends Fragment implements RoutingListener {

    DatabaseReference details;
    PopupWindow mpopup;
    DatabaseReference temp;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,163));

    private List<Polyline> polylines;
    LatLng start,end;
    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};

    MainContract.IView view;

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
        final TextView txt_taskDuration=(TextView)view.findViewById(R.id.taskDuration);
        final TextView txt_taskFrom=(TextView)view.findViewById(R.id.taskStartTime);
        final TextView txt_taskTo=(TextView)view.findViewById(R.id.taskFinishTime);
        final TextView txt_teamType=(TextView)view.findViewById(R.id.teamType);


        final Button but =(Button)view.findViewById(R.id.but_finish);

      final SharedPreferences sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );







        details.child(sh.getString( "data","emputy"  )).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {


            final String state=dataSnapshot.child("state").getValue(String.class);
            String TaskDetails=dataSnapshot.child("taskDetails").getValue(String.class);
           // String adress=dataSnapshot.child("ReportAdress").getValue(String.class);
            String teamType=dataSnapshot.child("teamType").getValue(String.class);
            String name=dataSnapshot.child("name").getValue(String.class);
            final String latitude =dataSnapshot.child("latitude").getValue(String.class);
            final String longitude =dataSnapshot.child("longitude").getValue(String.class);
            String TaskDuration=dataSnapshot.child("TaskDuration").getValue(String.class);
            String TimeFrom=dataSnapshot.child("TimeFrom").getValue(String.class);
            String TimeTo=dataSnapshot.child("TimeTo").getValue(String.class);




            start=new LatLng(Double.parseDouble(sh.getString("startJourneylat","30")),Double.parseDouble(sh.getString("startJourneylon","30")));
            end = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));


        //    MandopSideMenu m=new MandopSideMenu();

          //  m.onItemClick(getActivity(),start,end);
         //   calculateDirections(start,end);

            String number = sh.getString("postion","1");

            taskNumber.setText(getString(R.string.task)+number);
            taskDetails.setText(TaskDetails);
            taskState.setText(state);
           // taskAdress.setText(adress);
            txt_taskDuration.setText(TaskDuration);
            txt_taskFrom.setText(TimeFrom);
            txt_taskTo.setText(TimeTo);
            txt_teamType.setText(teamType);


            taskAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getActivity(),AdressMap.class);
                    i.putExtra("latit",latitude);
                    i.putExtra("longit",longitude);
                    startActivity(i);
                }
            });


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

                         if (dataSnapshot.child("state").getValue(String.class).equalsIgnoreCase("under")){
                             temp.child("state").setValue("done");
                         }

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





    public void calculateDirections(LatLng start, LatLng end){

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(start, end)
                .key("AIzaSyAT7Zm5TRyycR00208WPyAwad62mciY4dE")
                .build();
        routing.execute();







    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getActivity(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getActivity(), "Routing start", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        Toast.makeText(getActivity(), "Routing success", Toast.LENGTH_SHORT).show();




        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        MandopSideMenu m=new MandopSideMenu();
        m.mMap.moveCamera(center);

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int ii = 0; ii <route.size(); ii++) {

            //In case of more than 5 alternative routes
            int colorIndex = ii % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = m.mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getActivity(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        m.mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        m.mMap.addMarker(options);


    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();

    }






}
