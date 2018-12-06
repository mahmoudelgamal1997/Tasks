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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by M7moud on 11-Nov-18.
 */
public class FragmentTasks extends Fragment {
    DatabaseReference tasks;
    RecyclerView recyclerView;
    SharedPreferences sh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tasks_fragment,null);

        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tasks = FirebaseDatabase.getInstance().getReference().child("Clients").child(userId);
        tasks.keepSynced(true);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView)v. findViewById(R.id.view);
        recyclerView.setLayoutManager(layoutManager);
        display();

        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void display() {


        FirebaseRecyclerAdapter<ClientItem, Post_viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ClientItem, Post_viewholder>(
                ClientItem.class,
                R.layout.orders_cardview,
                Post_viewholder.class,
                tasks


        ) {
            @Override
            protected void populateViewHolder(final Post_viewholder viewHolder, final ClientItem model, final int position) {

                viewHolder.SetData("task "+(position+1));

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sh=getActivity().getSharedPreferences("plz", Context.MODE_PRIVATE );
                        SharedPreferences.Editor  mydata=sh.edit();
                        mydata.putString( "data",getRef(position).getKey().toString() );
                        mydata.putString( "postion",String.valueOf(position+1));
                        mydata.commit();

                        FragmentDetails fragment =new FragmentDetails();
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

    public void SetData(String number) {


        TextView taskNumber = (TextView) view.findViewById(R.id.taskNumber);
        taskNumber.setText(number);

    }


}}