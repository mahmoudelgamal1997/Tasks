package com.example2017.android.tasks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by M7moud on 12-Nov-18.
 */
public class FragmentTeamLeaders extends Fragment {


    DatabaseReference tasks,teamleader;
    RecyclerView recyclerView;
    SharedPreferences sh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.team_leader, null);

        Firebase.setAndroidContext(getActivity());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        teamleader = FirebaseDatabase.getInstance().getReference().child("TeamLeader");
        teamleader.keepSynced(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.view2);
        recyclerView.setLayoutManager(layoutManager);

        display();

        return v;
    }

    public void display() {


        FirebaseRecyclerAdapter<ClientItem, Post_viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientItem, Post_viewholder>(
                ClientItem.class,
                R.layout.leader_image,
                Post_viewholder.class,
                teamleader


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ClientItem model, final int position) {

                viewHolder.SetData(model.getName());
                Toast.makeText(getActivity(), model.getProfileImage(), Toast.LENGTH_SHORT).show();

                 viewHolder.SetImage(model.getProfileImage(),getActivity());





                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );
                        SharedPreferences.Editor  mydata=sh.edit();
                        mydata.putString( "key",getRef(position).getKey().toString() );
                        mydata.commit();

                        FragmentMembers fragment =new FragmentMembers();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.fragment, fragment)
                                .commit();


                    }
                });



            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class Post_viewholder extends RecyclerView.ViewHolder {

        View view;

        public Post_viewholder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void SetData(String name) {


            TextView leaderName = (TextView) view.findViewById(R.id.leaderName);
            leaderName.setText(name);

        }

        public void SetImage(final String img , final Context context){

            final ImageView profileImage=(ImageView)view.findViewById(R.id.leaderProfileImage);

          //  Glide.with(context).load(img).dontAnimate().into(profileImage).onLoadFailed();

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(context).load(img).into(profileImage);
                }
            });


        }

    }
}