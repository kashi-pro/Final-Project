package com.assignment.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity {

    //XML Component
    ImageView profileIcon;
    RecyclerView cardRecyclerView;
    CardAdapterRCV adapter;

    ArrayList<TableModel> tableList= new ArrayList<>();
    FirebaseDatabase database= FirebaseDatabase.getInstance();;
    DatabaseReference dbReference= database.getReference();;

    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Getting data from firebase
        dbReference.child("Tables").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot tableSnap : snapshot.getChildren()) {
                    tableList.add(tableSnap.getValue(TableModel.class));
                }
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    tempModel.setName(dataSnapshot.child("name").getValue(String.class));
//                    tempModel.setLocation(dataSnapshot.child("location").getValue(String.class));
//                    tempModel.setNumOfGuests(dataSnapshot.child("numOfGuests").getValue(Integer.class));
//                    tempModel.setDescription(dataSnapshot.child("description").getValue(String.class));
//                    tempModel.setImgURL(dataSnapshot.child("imgURL").getValue(String.class));
//
//                    tableList.add(tempModel);
//
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Recycler View Process
        cardRecyclerView= findViewById(R.id.cardRCV);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new CardAdapterRCV(this, tableList);
        cardRecyclerView.setAdapter(adapter);

        if(tableList.size() == 0) {
            Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
        }
        


        //Profile Icon onClick Listener
        profileIcon= findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toProfile= new Intent(HomeScreen.this, ProfileScreen.class);
                startActivity(toProfile);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            startActivity(new Intent(HomeScreen.this, LoginScreen.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}