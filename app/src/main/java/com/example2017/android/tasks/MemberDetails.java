package com.example2017.android.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example2017.android.tasks.Admin.GmailWebView;
import com.example2017.android.tasks.Admin.addTask;
import com.example2017.android.tasks.Mandop.MandopSideMenu;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MemberDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference memberData;
    ListView listView;
    FloatingActionButton fbtn;
    String languageKey ;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=(ListView)findViewById(R.id.memberListView);
        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
        memberData= FirebaseDatabase.getInstance().getReference().child("Clients").child(sh.getString( "MemberKey","emputy"));
        fbtn=(FloatingActionButton)findViewById(R.id.fab);

        languageKey=sh.getString("LanguageKey","en");
        updateResources(getApplicationContext(),languageKey);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        fbtn.setVisibility(View.INVISIBLE);
        //admin only
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("Sp17QHHa3vYoPh35JV2nWQ0zjFQ2")){

            fbtn.setVisibility(View.VISIBLE);

        }

        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii= new Intent(MemberDetails.this,addTask.class);
                startActivity(ii);

            }
        });
        FirebaseListAdapter<ClientItem> firebaseListAdapter=new FirebaseListAdapter<ClientItem>(
                this,
                ClientItem.class,
               R.layout.tasklistview,
                memberData
        ) {
            @Override
            protected void populateView(View v, ClientItem model, final int position) {

                TextView txt=(TextView)v.findViewById(R.id.taskPostion);
                TextView txt2=(TextView)v.findViewById(R.id.state);
                txt.setText("task "+(position+1));
                txt2.setText(model.getState());

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sh=getApplicationContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
                        SharedPreferences.Editor  mydata=sh.edit();
                        mydata.putString( "MissionKey",getRef(position).getKey().toString() );
                        mydata.commit();

                        Intent i =new Intent(MemberDetails.this,TaskDetailsForMembers.class);
                        startActivity(i);

                    }
                });
            }
        };

listView.setAdapter(firebaseListAdapter);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            Intent i =new Intent(MemberDetails.this,GmailWebView.class);
            startActivity(i);

        } else if (id == R.id.nav_home) {
            Intent i =new Intent(MemberDetails.this,MandopSideMenu.class);
            startActivity(i);
            finish();


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
            Toast.makeText(MemberDetails.this, "تم تغير اللغه من فضلك اعد تشغيل التطبيق مره اخري ", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent i =new Intent(MemberDetails.this,MainActivity.class);
            startActivity(i);
            Toast.makeText(MemberDetails.this, "Sgined out ", Toast.LENGTH_SHORT).show();

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
