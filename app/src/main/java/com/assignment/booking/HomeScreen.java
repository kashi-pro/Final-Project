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

    //XML Components
    ImageView profileIcon;
    RecyclerView cardRCV;

    //Firebase Objects to Fetch Data from Database
    FirebaseDatabase database;
    DatabaseReference dbReference;

    //FirebaseUser object to check user
    FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();

    //ArrayList of type TableModel
    ArrayList<TableModel> tableList;

    //Firebase Recycler option
//    FirebaseRecyclerOptions<TableModel> options;
//    FirebaseRecyclerAdapter<TableModel, CardViewHolder>adapter;

    //Adapter Object to show list
    CardAdapterRCV cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Binding XML with Java
        profileIcon= findViewById(R.id.profileIcon);
        cardRCV= findViewById(R.id.cardRCV);
        cardRCV.setLayoutManager(new LinearLayoutManager(HomeScreen.this));
        cardRCV.hasFixedSize();

        //OnClick Listener on profileIcon
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, ProfileScreen.class));
            }
        });

        //Initializing ArrayList
        tableList= new ArrayList<>();

//        loadTableData();

//        //Setting Recycler view with adapter
//        cardAdapter= new CardAdapterRCV(this, tableList);
//        cardRCV.setAdapter(cardAdapter);
//
//        //Fetching all tables from Database
        database= FirebaseDatabase.getInstance();
        dbReference= database.getReference().child("Tables");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TableModel> list= new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TableModel tempTable= dataSnapshot.getValue(TableModel.class);
                    list.add(tempTable);
                }
                //Setting Recycler view with adapter
                cardAdapter= new CardAdapterRCV(HomeScreen.this, list);
                cardRCV.setAdapter(cardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void loadTableData() {
//        options= new FirebaseRecyclerOptions.Builder<TableModel>().setQuery(dbReference, TableModel.class).build();
//        adapter= new FirebaseRecyclerAdapter<TableModel, CardViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull TableModel model) {
//                holder.tableNamePlace.setText(model.getName());
//                holder.locationPlace.setText(model.getLocation());
//                holder.numGuestPlace.setText(model.getNumOfGuests());
//                Picasso.get().load(model.getImgURL()).into(holder.tablePicPlace);
//            }
//
//            @NonNull
//            @Override
//            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_layout_template, parent, false);
//                return new CardViewHolder(view);
//            }
//        };
////        adapter.startListening();
//        cardRCV.setAdapter(adapter);
//
//    }

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