package com.example2017.android.tasks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by M7moud on 12-Nov-18.
 */
public class FragmentMembers extends Fragment {
    DatabaseReference teammembers;
    RecyclerView recyclerView;
    SharedPreferences sh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.members_fragment,null);

        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );

        //admin ID
        if (userId.equals("Sp17QHHa3vYoPh35JV2nWQ0zjFQ2")) {

            teammembers = FirebaseDatabase.getInstance().getReference().child("TeamLeader").child(sh.getString("key",userId)).child("members");

        }else {
            //normal teamleader
            teammembers = FirebaseDatabase.getInstance().getReference().child("TeamLeader").child(userId).child("members");
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)v. findViewById(R.id.view3);
        recyclerView.setLayoutManager(layoutManager);

        display();

        return  v;
    }

    public void display() {


        FirebaseRecyclerAdapter<ClientItem, Post_viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientItem, Post_viewholder>(
                ClientItem.class,
                R.layout.leader_image,
                Post_viewholder.class,
                teammembers


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ClientItem model, final int position) {

                viewHolder.SetData(model.getName());

                viewHolder.SetImage(model.getProfileImage(),getActivity());
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );
                        SharedPreferences.Editor  mydata=sh.edit();
                        mydata.putString( "MemberKey",getRef(position).getKey().toString() );
                        mydata.commit();

                        Intent i =new Intent(view.getContext(),MemberDetails.class);
                        startActivity(i);


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

        public void SetData(String number) {


            TextView taskNumber = (TextView) view.findViewById(R.id.leaderName);
            taskNumber.setText(number);

        }


        public void SetImage(final String img , final Context context){

            final ImageView profileImage=(ImageView)view.findViewById(R.id.user_profile_image);


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