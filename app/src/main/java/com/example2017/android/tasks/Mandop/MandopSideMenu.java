package com.example2017.android.tasks.Mandop;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example2017.android.tasks.Admin.AdminMap;
import com.example2017.android.tasks.Admin.GmailWebView;
import com.example2017.android.tasks.Admin.SideMenu;
import com.example2017.android.tasks.Chat.ChatMembers;
import com.example2017.android.tasks.FragmentMembers;
import com.example2017.android.tasks.FragmentTasks;
import com.example2017.android.tasks.MainActivity;
import com.example2017.android.tasks.Mandob_Map;
import com.example2017.android.tasks.PlaceAutocompleteAdapter;
import com.example2017.android.tasks.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MandopSideMenu extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, NavigationView.OnNavigationItemSelectedListener,MainContract.IView {

    public GoogleMap mMap;
    private String userId;
    GoogleApiClient mgoogleclient;
    LocationRequest locationRequest;
    DatabaseReference tasks,teamleader;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationRequest mLocationRequest;

    FusedLocationProviderClient client;
   // private AutoCompleteTextView mAutoCompleteTextView;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,163));

    private List<Polyline> polylines;
    LatLng start,end;
    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};

    String languageKey ;
    SharedPreferences sh;

    private TextView txtName;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandop_side_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sh= getSharedPreferences("plz",MODE_PRIVATE);
        languageKey=sh.getString("LanguageKey","en");
        updateResources(getApplicationContext(),languageKey);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
     //   mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_search);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        polylines = new ArrayList<>();
        client = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();

        teamleader= FirebaseDatabase.getInstance().getReference().child("TeamLeader");

        //admin
        if (userId.equals("Sp17QHHa3vYoPh35JV2nWQ0zjFQ2")){


            Intent ii= new Intent(MandopSideMenu.this,SideMenu.class);
            startActivity(ii);
            finish();
        }else {

            teamleader.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(userId.toString())) {
                        Intent ii= new Intent(MandopSideMenu.this,AdminMap.class);
                        startActivity(ii);
                        DisplayFragmentMember();
                    } else {
                        DisplayFragment();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        init(headerView);

        SetImage(getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mandop_side_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            // Handle the camera action
            Intent i =new Intent(MandopSideMenu.this,ChatMembers.class);
            startActivity(i);

        } else if (id == R.id.nav_home) {


        } else if (id == R.id.nav_manage) {

            if(languageKey.equals("en")){
                languageKey="ar";
                System.out.println("language ar = "+ true);
            }else{

                languageKey="en";
                System.out.println("language en = "+ true);

            }

            SharedPreferences.Editor edit=sh.edit();
            edit.putString("LanguageKey",languageKey);
            edit.commit();

            updateResources(getApplicationContext(),languageKey);
            Toast.makeText(MandopSideMenu.this, "تم تغير اللغه من فضلك اعد تشغيل التطبيق مره اخري ", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent i =new Intent(MandopSideMenu.this,MainActivity.class);
            startActivity(i);
            Toast.makeText(MandopSideMenu.this, "Sgined out ", Toast.LENGTH_SHORT).show();

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera




        buildGoogleApiClients();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);



    }


    protected synchronized void buildGoogleApiClients() {
        mgoogleclient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mgoogleclient.connect();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleclient, locationRequest, this);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng currentlocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));

        start= currentlocation;
        SharedPreferences.Editor edit=sh.edit();
        edit.putString("startJourneylat", String.valueOf(start.latitude));
        edit.putString("startJourneylon", String.valueOf(start.longitude));
        edit.commit();


        FirebaseUser isUser = FirebaseAuth.getInstance().getCurrentUser();
        if (isUser != null) {
            userId = isUser.getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("ClientRequest");
            GeoFire geoFire = new GeoFire(db);
            geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }






    void DisplayFragment(){
        FragmentTasks ft=new FragmentTasks();


        FragmentManager fragmentManager=getFragmentManager() ;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,ft)
                .commit();

    }

    void DisplayFragmentMember(){
        FragmentMembers ft=new FragmentMembers();
        FragmentManager fragmentManager=getFragmentManager() ;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,ft)
                .commit();

    }


/*

    private AdapterView.OnItemClickListener mAutoCompleteClickListener =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideKeyboard();
            final AutocompletePrediction item=mPlaceAutocompleteAdapter.getItem(i);

            final String placeId=item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mgoogleclient,placeId);
            placeResult.setResultCallback(resultCallback);

        }
    };

    private ResultCallback<PlaceBuffer> resultCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if (!places.getStatus().isSuccess()){
                places.release();
                return;
            }
            final Place place= places.get(0) ;

            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(place.getLatLng());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);

            end=place.getLatLng();
            if (start!= null) {
                calculateDirections(start, place.getLatLng());
            }
            //    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),10));
        }
    };


    public void calculateDirections(LatLng start,LatLng end){

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
        Toast.makeText(MandopSideMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MandopSideMenu.this, "Routing start", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        Toast.makeText(MandopSideMenu.this, "Routing success", Toast.LENGTH_SHORT).show();


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
        Toast.makeText(MandopSideMenu.this, "Cancelled", Toast.LENGTH_SHORT).show();

    }

*/
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    public void hideKeyboard()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void locationSetting(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);


    }


    public  void createLocationRequest() {

        locationSetting();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setNeedBle(true);
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        MandopSideMenu.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });


    }






    //change language
    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    private void SetImage(final Context context){

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("username").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String img  =dataSnapshot.child("ProfileImage").getValue(String.class);
                final String name  =dataSnapshot.child("name").getValue(String.class);

                if (name!=null) {
                    txtName.setText(name);
                }
                Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(img).into(imageView);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public void init(View view){

        txtName=(TextView)view.findViewById(R.id.name_mandop);
        imageView=(ImageView) view.findViewById(R.id.imageView_Mandop);
    }


    @Override
    public void onItemClick(Activity v, LatLng start,LatLng end) {

        Toast.makeText(v.getApplicationContext(), "done", Toast.LENGTH_SHORT).show();

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
}
