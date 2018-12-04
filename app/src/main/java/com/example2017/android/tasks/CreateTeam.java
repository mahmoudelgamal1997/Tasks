package com.example2017.android.tasks;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateTeam extends AppCompatActivity {

    Spinner spinner;
    ListView listView;
   private String nameSelected,id,ProfileImage="";
   private String TeamLeaderName , TeamLeaderId , TeamLeaderProfileImage;
   private   DatabaseReference names,Team,temp;
    ArrayList<CustomArrayList> TeamNames;
    int position=0;
    Button CreateTeam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);


        spinner = (Spinner) findViewById(R.id.Spinner_member);
        listView = (ListView) findViewById(R.id.List_member);
        names = FirebaseDatabase.getInstance().getReference().child("username");
        Team = FirebaseDatabase.getInstance().getReference().child("TeamLeader");
        CreateTeam=(Button)findViewById(R.id.but_CreateTeam);

        TeamNames = new ArrayList<>();

        final CustomAdaper customAdaper=new CustomAdaper(TeamNames);

        FirebaseListAdapter<ClientItem> firebaseListAdapter = new FirebaseListAdapter<ClientItem>(
                this,
                ClientItem.class,
                R.layout.textname,
                names
        ) {
            @Override
            protected void populateView(View v, ClientItem model, int position) {


                TextView txt = (TextView) v.findViewById(R.id.name);
                txt.setText(model.getName());
                nameSelected = model.getName();
                id=getRef(position).getKey().toString();
                ProfileImage=model.getProfileImage();



            }


        };

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                TeamNames.add(new CustomArrayList(id,nameSelected,ProfileImage));
                customAdaper.notifyDataSetChanged();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(firebaseListAdapter);




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int ii, long l) {

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("هل تريد تعينه القائد");
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        TeamLeaderName=TeamNames.get(ii).getName();
                        TeamLeaderId=TeamNames.get(ii).getId();
                        TeamLeaderProfileImage=TeamNames.get(ii).getProfileImage();

                    }
                });

                AlertDialog alert=builder.create() ;
                alert.show();


                return true;
            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                TeamNames.remove(i);
                customAdaper.notifyDataSetChanged();



            }
        });

        listView.setAdapter(customAdaper);



        CreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TeamLeaderId != null){
                Team.child(TeamLeaderId).child("ProfileImage").setValue(TeamLeaderProfileImage);
                Team.child(TeamLeaderId).child("name").setValue(TeamLeaderName);

                for(int i =0 ; i<customAdaper.arrayLists.size();i++)
                {

                    //member id
                    String id=TeamNames.get(i).getId();
                    temp=Team.child(TeamLeaderId).child("members").child(id);
                    temp.child("name").setValue(TeamNames.get(i).getName());
                    temp.child("ProfileImage").setValue(TeamNames.get(i).getProfileImage());

                }
                Toast.makeText(CreateTeam.this,getString(R.string.CreateTeamToast), Toast.LENGTH_SHORT).show();

            }else {
                    Toast.makeText(CreateTeam.this,getString(R.string.SelectLeader), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    class  CustomAdaper extends BaseAdapter{

    ArrayList<CustomArrayList> arrayLists=new ArrayList<>();

    public CustomAdaper(ArrayList<CustomArrayList > arrayList){
       this. arrayLists=arrayList;
    }

    @Override
    public int getCount() {
        return arrayLists.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayLists.get(i).name;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater linflater =getLayoutInflater();
        View view1 =linflater.inflate(R.layout.textname,null);
        TextView txtname=(TextView)view1.findViewById(R.id.name);
        txtname.setText(arrayLists.get(i).getName());

        return view1;
    }
}


}
