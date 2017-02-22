package com.example.unifood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.support.v7.widget.RecyclerView;

import com.example.unifood.R;
import com.example.unifood.firebase.utils.Utilities;
import com.example.unifood.fragments.RestaurantProductFragment;
import com.example.unifood.fragments.RestaurantProfileFragment;
import com.example.unifood.fragments.RestaurantReviewFragment;
import com.example.unifood.models.Product;
import com.example.unifood.models.Restaurant;
import com.example.unifood.models.Review;
import com.example.unifood.models.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements RestaurantProductFragment.OnListFragmentInteractionListener, RestaurantReviewFragment.OnListFragmentInteractionListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    private TabHost tabHost;
    private TabSpec spec1, spec2, spec3;

    DatabaseReference restaurantRef;
    DatabaseReference reviewsRef;
    DatabaseReference productsRef;

    private String restaurantUId;
    private ArrayList<Review> reviewSet = new ArrayList<>();
    private ArrayList<Product> productSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        setUpFirebase();
        setUpHostBar();

        Intent intentRestaurantSelected = getIntent();
        if (intentRestaurantSelected.hasExtra("REST_ID")) {
            restaurantUId = intentRestaurantSelected.getStringExtra("REST_ID");
            System.out.println("O id ta sendo pego e eh: " + restaurantUId);
        }

        if (restaurantUId != null) {
            restaurantRef = mDatabase.child("restaurants").child(restaurantUId);
            loadProfile();
            loadProducts();
            loadReviews();
        }

    }

    private void loadProfile() {
        restaurantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                AppCompatActivity activity = RestaurantActivity.this;
                boolean deuRuim = (restaurant == null) ? true : false;
                System.out.println("Deu ruim? " + deuRuim);
                RestaurantProfileFragment fragment = (RestaurantProfileFragment) activity.getFragmentManager().findFragmentById(R.id.restaurant_profile);
                //fragment.setRestaurantInfo(restaurant.getName(), restaurant.getCampus(), restaurant.getLocalization());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadProducts() {
        restaurantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                List<Product> productList = restaurant.getProductList();
                AppCompatActivity activity = RestaurantActivity.this;
                RestaurantProductFragment fragment = (RestaurantProductFragment) activity.getFragmentManager().findFragmentById(R.id.restaurant_products);
                fragment.setProductList(productList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private void loadReviews() {
        restaurantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                List<Review> reviewList = restaurant.getReviewList();
                AppCompatActivity activity = RestaurantActivity.this;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void setUpHostBar(){

        tabHost =(TabHost) findViewById(R.id.restaurant_host_bar);
        tabHost.setup();

        spec1 = tabHost.newTabSpec("Informações");
        spec1.setIndicator("Informações");
        spec1.setContent(R.id.tab1);

        spec2 = tabHost.newTabSpec("Cardápio");
        spec2.setIndicator("Cardápio");
        spec2.setContent(R.id.tab2);

        spec3 = tabHost.newTabSpec("Avaliações");
        spec3.setIndicator("Avaliações");
        spec3.setContent(R.id.tab3);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);

    }

    public void setUpFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(Product item) {

    }

    public void onListFragmentInteraction(Review item) {

    }

    public void newReviewFromFragment(float newRate, String newComment) {

        String newData = Util.getInstancia().getCurrentDate();
        Review newReview = new Review(mFirebaseUser.getUid(), restaurantUId, newRate, newComment, newData);

        // TODO: passar esta review pra lista de reviews do restaurantes
    }

}