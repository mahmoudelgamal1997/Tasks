package com.example2017.android.tasks.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.EnvironmentalReverb;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example2017.android.tasks.Chat.ChatMembers;
import com.example2017.android.tasks.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by M7moud on 03-Mar-19.
 */
public class Notification extends Service {

    DatabaseReference notify;
    ChildEventListener childEventListener;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getNotification();
        System.out.println("APIInterface started");

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void getNotification(){
        final String id= FirebaseAuth.getInstance().getCurrentUser().getUid();

         notify= FirebaseDatabase.getInstance().getReference().child("Notification").child(id);




         childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                prepareNotification(dataSnapshot.child("from").getValue(String.class));

                if (s != null){
                    notify.child(s).removeValue();
                }

                if(dataSnapshot.getChildrenCount()==1) {
                    notify.removeValue();
                    Toast.makeText(Notification.this,"All notification loaded" , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        notify.addChildEventListener(childEventListener);






    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        notify.removeEventListener(childEventListener);
        Toast.makeText(Notification.this, "Destroyed", Toast.LENGTH_LONG).show();

    }

    private void prepareNotification(String name){
        Random r=new Random();
        int id= r.nextInt(100);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle(name)
                        .setContentText("you have a new message")
                        .setSound((Settings.System.DEFAULT_NOTIFICATION_URI));

        Intent notificationIntent = new Intent(getApplicationContext(), ChatMembers.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),id , notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, builder.build());
    }

}
