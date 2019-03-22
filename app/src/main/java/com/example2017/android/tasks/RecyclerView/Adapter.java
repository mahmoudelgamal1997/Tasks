package com.example2017.android.tasks.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.app.Fragment;
import android.app.FragmentManager;;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example2017.android.tasks.ClientItem;
import com.example2017.android.tasks.FragmentDetails;
import com.example2017.android.tasks.R;

import java.util.List;

/**
 * Created by M7moud on 21-Mar-19.
 */
public class Adapter  extends RecyclerView.Adapter<Adapter.movieHolder> {
    SharedPreferences sh;
    List<ClientItem> list;
    Activity activity;
    //cnstructr
    public Adapter(List<ClientItem> list ,Activity activity){
        this.list=list;
        this.activity=activity;
    }


    @Override
    public movieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_cardview,parent,false);
        movieHolder hold=new movieHolder(row);




        return hold;
    }

    @Override
    public void onBindViewHolder(final movieHolder holder, final int position)
    {

        //instace from model
        ClientItem model=list.get(position);
        holder.taskNumber.setText("tasks  "+(position +1));


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sh = holder.view.getContext().getSharedPreferences("plz", Context.MODE_PRIVATE );
                SharedPreferences.Editor  mydata=sh.edit();
                mydata.putString( "postion",String.valueOf(position+1));
                mydata.commit();



                FragmentDetails fragment =new FragmentDetails();
                FragmentManager fragmentManager =activity.getFragmentManager() ;
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        //to return all images
        //if zero it won't appeat any thing
        return list.size();
    }

    //holder_class
    class movieHolder extends RecyclerView.ViewHolder
    {

        TextView taskNumber;
        View view;
        public movieHolder(View itemView)
        {
            super(itemView);
             view=itemView;
             taskNumber = (TextView) itemView.findViewById(R.id.taskNumber);




        }

    }




}