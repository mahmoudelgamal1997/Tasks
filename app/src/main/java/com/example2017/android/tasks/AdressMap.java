package com.example2017.android.tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example2017.android.tasks.Mandop.MandopSideMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class AdressMap extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private String StartLat,StartLon,EndLat, EndLon;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,163));

    private List<Polyline> polylines;
    LatLng start,end;
    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};

    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylines = new ArrayList<>();

        sh= getSharedPreferences("plz",MODE_PRIVATE);

        StartLat=sh.getString("startJourneylat","222");
        StartLon=sh.getString("startJourneylat","222");
        Intent i =getIntent();
        EndLat=i.getStringExtra("latit");
        EndLon=i.getStringExtra("longit");


        start=new LatLng(Double.parseDouble(StartLat),Double.parseDouble(StartLon));
        end=new LatLng(Double.parseDouble(EndLat),Double.parseDouble(EndLon));


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       // LatLng Target = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
       // mMap.addMarker(new MarkerOptions().position(Target).title("Target Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Target,12));

        if (start !=null &end!=null){
            calculateDirections(start,end);
        }

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
        Toast.makeText(getApplicationContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getApplicationContext(), "Routing start", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        Toast.makeText(getApplicationContext(), "Routing success", Toast.LENGTH_SHORT).show();




        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        mMap.moveCamera(center);

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
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(options);


    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
