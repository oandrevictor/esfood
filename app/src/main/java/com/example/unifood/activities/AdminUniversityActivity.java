package com.example.unifood.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.example.unifood.R;
import com.example.unifood.exceptions.CampusException;
import com.example.unifood.fragments.UniversitiesListFragment;
import com.example.unifood.adapters.UniversityListAdapter;
import com.example.unifood.models.Campus;
import com.example.unifood.models.Restaurant;
import com.example.unifood.models.University;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdminUniversityActivity extends AppCompatActivity {
    String universityName;
    String campusName;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private ArrayList<University> dataSet = new ArrayList<>();
    private ArrayList<Restaurant> restaurantSet = new ArrayList<>();

    DatabaseReference ref;
    DatabaseReference restRef;


    @InjectView(R.id.name_text_field) EditText name;
    @InjectView(R.id.new_campus_field) EditText campusname;
    @InjectView(R.id.create_button)    Button createButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_university);
        setUpFirebase();
        ButterKnife.inject(this);

        TabHost tabHost=(TabHost)findViewById(R.id.host_bar);
        tabHost.setup();

        TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Unis");

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Restaurantes");
        spec2.setContent(R.id.tab2);

        TabSpec spec3 = tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Tab 3");
        spec3.setContent(R.id.tab3);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universityName = name.getText().toString();
                campusName = campusname.getText().toString();

                Campus campus = null;
                try {
                    campus = new Campus(campusName);
                } catch (CampusException e) {
                    e.printStackTrace();
                }

                University university = new University(universityName);
                university.addCampus(campus.getId());
                mDatabase.child("campus").child(campus.getId()).setValue(campus);
                mDatabase.child("universities").child(university.getId()).setValue(university);

                Class mainA = MainActivity.class;
                Intent goHome = new Intent(AdminUniversityActivity.this, mainA);
                startActivity(goHome);

            }
        });


        dataSet = new ArrayList<University>();
        ref = mDatabase.child("universities");
        restRef = mDatabase.child("restaurants");

        ref.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                new AdminUniversityActivity.LongOperation(snapshot).execute();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

        restRef.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ///new LoadRestaurants(snapshot, restaurantSet, AdminUniversityActivity.this, R.id.fragment_place2).execute();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });


    }


    /**
     * Gets a reference from the database at Firebase server as well the Auth system and the current user.
     */
    private void setUpFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected class LongOperation extends AsyncTask<String, Void, String> {
        DataSnapshot snapshot;
        public LongOperation(DataSnapshot snapshot){
            this.snapshot = snapshot;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("Count " ,""+snapshot.getChildrenCount());
            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                University uni = postSnapshot.getValue(University.class);
                dataSet.add(uni);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            UniversityListAdapter mAdapter = new UniversityListAdapter(AdminUniversityActivity.this, dataSet);
            UniversitiesListFragment fragment = (UniversitiesListFragment) getFragmentManager().findFragmentById(R.id.fragment_place);
            fragment.updateRecycler(mAdapter);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }




}
