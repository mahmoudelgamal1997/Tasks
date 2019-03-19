package com.example2017.android.tasks.Admin;

import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
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
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example2017.android.tasks.BooVariable;
import com.example2017.android.tasks.Chat.ChatMembers;
import com.example2017.android.tasks.FragmentTeamLeaders;
import com.example2017.android.tasks.MainActivity;
import com.example2017.android.tasks.R;
import com.example2017.android.tasks.api.APIInterface;
import com.example2017.android.tasks.Service.Notification;
import com.example2017.android.tasks.api.Reports;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SideMenu extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private LatLng postion;
    LatLng currentPostion = new LatLng(29.975051, 31.287913);
    String name;
    private Map<String, Marker> markers;
    FusedLocationProviderClient client;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationRequest mLocationRequest;
    private TextView txtName;
    private ImageView imageView;
    String languageKey;
    SharedPreferences sh;
    private String userId;
    boolean enable=false;
    Context context;

   private BooVariable bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        sh= getSharedPreferences("plz",MODE_PRIVATE);
        //get last value
        enable=sh.getBoolean("switch",true);







        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //start service for Notification
        startService(new Intent(getBaseContext(), Notification.class));


        DatabaseReference image=FirebaseDatabase.getInstance().getReference();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SetImage(getApplicationContext(),image);
                //to set language
                sh = getSharedPreferences("plz", MODE_PRIVATE);
        languageKey = sh.getString("LanguageKey", "en");
        updateResources(getApplicationContext(), languageKey);
        System.out.println("language = " + languageKey);
        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        client = LocationServices.getFusedLocationProviderClient(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        init(headerView);

        SetImage(getApplicationContext());

        // setup markers
        this.markers = new HashMap<String, Marker>();
        client = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        DisplayFragmentLeader();






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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            Toast.makeText(SideMenu.this, "messages", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SideMenu.this, ChatMembers.class);
            startActivity(i);


        } else if (id == R.id.nav_ShareForApp) {


            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle("Chooser title")
                    .setText("http://play.google.com/store/apps/details?id=" + this.getPackageName())
                    .startChooser();


        } else if (id == R.id.nav_Vote) {

            launchMarket();


            // Handle the camera action
        } else if (id == R.id.nav_manage) {

            //to change languge
            if (languageKey.equals("en")) {
                languageKey = "ar";
                System.out.println("language ar = " + true);
            } else {

                languageKey = "en";
                System.out.println("language en = " + true);

            }

            SharedPreferences.Editor edit = sh.edit();
            edit.putString("LanguageKey", languageKey);
            edit.commit();

            updateResources(getApplicationContext(), languageKey);
            Toast.makeText(SideMenu.this, "تم تغير اللغه من فضلك اعد تشغيل التطبيق مره اخري ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(SideMenu.this, MainActivity.class);
            startActivity(i);
            Toast.makeText(SideMenu.this, "Sgined out ", Toast.LENGTH_SHORT).show();

            finish();

        } else if (id == R.id.nav_create) {
            Intent i = new Intent(SideMenu.this, CreateTeam.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item, menu);


        MenuItem menuItem = menu.findItem(R.id.switch_item);
        View view = MenuItemCompat.getActionView(menuItem);

        Switch s=(Switch)view.findViewById(R.id.switchForActionBar);
        s.setChecked(enable);
        final SharedPreferences.Editor editor=sh.edit();
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b==true){
                    editor.putBoolean("switch",true);
                    enable=true;
                    Toast.makeText(SideMenu.this, "you are online", Toast.LENGTH_SHORT).show();
                    editor.apply();

                    bv.setEnable(true);


                }else{
                    editor.putBoolean("switch",false);
                    enable=false;
                    Toast.makeText(SideMenu.this, "You are offline", Toast.LENGTH_SHORT).show();
                    editor.apply();

                    bv.setEnable(false);
                }
            }
        });


        ImageView message=(ImageView)view.findViewById(R.id.Messages);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SideMenu.this, ChatMembers.class));
            }
        });


        ImageView notification=(ImageView)view.findViewById(R.id.Notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SideMenu.this, "Notification", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        return super.onOptionsItemSelected(item);
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
        if (client.getLastLocation() != null) {
            client.getLastLocation().addOnCompleteListener(SideMenu.this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {

                        //     currentPostion = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());

                    } else {
                        Toast.makeText(SideMenu.this, "failed to get loction", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("ClientRequest");
            final DatabaseReference Users = FirebaseDatabase.getInstance().getReference().child("username");
            GeoFire geoFire = new GeoFire(db);

            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(currentPostion.latitude, currentPostion.longitude), 1000);

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {


                    Users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            name = dataSnapshot.child(key).child("name").getValue(String.class);


                            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
                            marker.setTitle(name);
                            markers.put(key, marker);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    // Move the marker
                    Marker marker = markers.get(key);
                    if (marker != null) {

                        postion = new LatLng(location.latitude, location.longitude);

                        animateMarker(marker, postion, false);
                    }


                }


                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });
        }
    }


    void DisplayFragmentLeader() {
        FragmentTeamLeaders ft = new FragmentTeamLeaders();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, ft)
                .commit();

    }


    @Override
    public void onLocationChanged(Location location) {

        currentPostion = new LatLng(location.getLatitude(), location.getLongitude());


    }

    // Animation handler for old APIs without animation support
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {

                        marker.setVisible(false);
                    } else {

                        marker.setVisible(true);
                    }
                }
            }
        });
    }


    public boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public void locationSetting() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
    }


    public void createLocationRequest() {

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
                                        SideMenu.this,
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


    public void init(View view) {

        txtName = (TextView) view.findViewById(R.id.nameHeader);
        imageView = (ImageView) view.findViewById(R.id.imageViewHeader);
        bv = new BooVariable();

    }

    private void SetImage(final Context context) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("username").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String img = dataSnapshot.child("ProfileImage").getValue(String.class);
                final String name = dataSnapshot.child("name").getValue(String.class);

                txtName.setText(name);
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

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }


    public void SetImage(final Context context, DatabaseReference databaseReference) {

        final ImageView img = (ImageView) findViewById(R.id.profile_image);

        databaseReference.child("username").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String uri = dataSnapshot.child("ProfileImage").getValue(String.class);


                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(uri).into(img);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    @Override
    protected void onResume() {
        super.onResume();

        if (enable){
            //start service for Notification
            startService(new Intent(getBaseContext(), Notification.class));

            Toast.makeText(SideMenu.this, "You are online", Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(SideMenu.this, "You are offline", Toast.LENGTH_SHORT).show();

        }



        bv.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {

                if (bv.isEnable()){
                    startService(new Intent(getBaseContext(), Notification.class));

                }else {
                    stopService(new Intent(getBaseContext(), Notification.class));

                }

            }
        });

    }






}