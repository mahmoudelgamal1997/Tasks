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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        View v = inflater.inflate(R.layout.tasks_fragment, null);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        teamleader = FirebaseDatabase.getInstance().getReference().child("TeamLeader").child(userId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.view);
        recyclerView.setLayoutManager(layoutManager);

        display();

        return v;
    }

    public void display() {


        FirebaseRecyclerAdapter<ClientItem, Post_viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientItem, Post_viewholder>(
                ClientItem.class,
                R.layout.orders_cardview,
                Post_viewholder.class,
                teamleader


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ClientItem model, final int position) {

                viewHolder.SetData(model.getName());


                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




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


            TextView leaderName = (TextView) view.findViewById(R.id.taskNumber);
            leaderName.setText(name);

        }


    }
}