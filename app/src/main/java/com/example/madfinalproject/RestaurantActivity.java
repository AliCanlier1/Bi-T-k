package com.example.madfinalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RestaurantActivity extends AppCompatActivity implements IRestaurantInterface{
    RecyclerView recyclerView;
    ArrayList<Restaurant> restaurantArrayList;
    RestaurantAdapter restaurantAdapter;
    FirebaseFirestore db;
    String reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db =FirebaseFirestore.getInstance();
        restaurantArrayList = new ArrayList<Restaurant>();
        restaurantAdapter = new RestaurantAdapter(RestaurantActivity.this, restaurantArrayList,this);
        EventChangeListener();
        recyclerView.setAdapter(restaurantAdapter);
    }
    private void EventChangeListener() {
        db.collection("Restaurants").orderBy("distance")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc :value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                restaurantArrayList.add(dc.getDocument().toObject(Restaurant.class));
                            }
                            restaurantAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(RestaurantActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}