package com.assignment.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();


//        sendDataToDB();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mAuth.getCurrentUser() == null) {
                    Intent toLogin= new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(toLogin);
                    finish();
                }
                else {
                    Intent toHome= new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(toHome);
                    finish();
                }

            }
        }, 2000);

    }

    private void sendDataToDB() {
        ArrayList<TableModel> list= new ArrayList<>();

        list.add(new TableModel("Luxury Indoor Table", "Indoor", 5,
                "This table is placed near the window. Window contains an pleasant view. Book this table to seize the moment and to enjoy the meal with full heart.",
                "https://firebasestorage.googleapis.com/v0/b/table-booking-app-7a1e8.appspot.com/o/tableImages%2Findoor_table_1.jpg?alt=media&token=5175df1e-5d92-40c7-af81-326c8582ff13"));

        list.add(new TableModel("Round Indoor Table", "Indoor", 5,
                "This table is placed in center of the restaurant. You can really enjoy the restaurant's clean environment with your meal",
                "https://firebasestorage.googleapis.com/v0/b/table-booking-app-7a1e8.appspot.com/o/tableImages%2Findoor_table_2.jpg?alt=media&token=c138430b-d6fc-4c42-85be-7c08e15cd5a4"));

        list.add(new TableModel("Wall Side Indoor Table", "Indoor", 4,
                "This is a wall side table. You can sit here if you don't want to engage with restaurant environment. All the services will be exactly same as other tables",
                "https://firebasestorage.googleapis.com/v0/b/table-booking-app-7a1e8.appspot.com/o/tableImages%2Findoor_table_3.jpg?alt=media&token=37197c86-09d1-4a72-a74b-ab58b33fa83c"));

        list.add(new TableModel("Royal Indoor Table", "Indoor", 5,
                "This table is at the most premium location of the restaurant. You will enjoy the service at our best and can also enjoy your meal with soft music",
                "https://firebasestorage.googleapis.com/v0/b/table-booking-app-7a1e8.appspot.com/o/tableImages%2Findoor_table_4.jpg?alt=media&token=16858eb1-6879-491c-ba19-1b0f4a4594ba"));

        DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference().child("Tables");
        for(int i=0; i<list.size(); i++) {
            dbReference.push().setValue(list.get(i));
        }

    }
}