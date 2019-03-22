package com.example2017.android.tasks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example2017.android.tasks.Mandop.MandopSideMenu;
import com.example2017.android.tasks.RecyclerView.Adapter;
import com.example2017.android.tasks.api.APIInterface;
import com.example2017.android.tasks.api.Tasks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M7moud on 11-Nov-18.
 */
public class FragmentTasks extends Fragment  {
    DatabaseReference tasks;
    RecyclerView recyclerView;
    SharedPreferences sh;
    int size=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tasks_fragment,null);

        String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tasks = FirebaseDatabase.getInstance().getReference().child("Clients").child(userId);
        tasks.keepSynced(true);


        Toast.makeText(getActivity(), "Fragment Task", Toast.LENGTH_SHORT).show();


        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView)v. findViewById(R.id.view);
        recyclerView.setLayoutManager(layoutManager);

        getData();


        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
/*
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


}

*/

    public void getData(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIInterface.server_url)
                .addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        APIInterface service = retrofit.create(APIInterface.class);

        service.getTasks().enqueue(new retrofit2.Callback<Tasks>() {
            @Override
            public void onResponse(Call<Tasks> call, Response<Tasks> response) {


                size=response.body().getData().size();

                List<ClientItem> tasks = new ArrayList<>();

                Log.e("number",size+"");
                for (int i = 0; i < (size); i++) {
                    tasks.add(new ClientItem());
                }

                Adapter adapter=new Adapter(tasks,getActivity());
                recyclerView.setAdapter(adapter);

            }


            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {
                Log.e("Fail",t.getMessage());

            }
        });


    }




}