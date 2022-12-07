package com.assignment.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileScreen extends AppCompatActivity {

    //XML Components
    TextView nameField, emailField, dobField, phoneField, editTxt;
    ImageView profileImageField;
    Button signOutBtn, deleteBtn, mapBtn;

    //Firebase User Object
    FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();

    //Firebase Database Object
    FirebaseDatabase database;
    DatabaseReference dbReference;

    String accoutnRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Binding Layout
        nameField= findViewById(R.id.nameField);
        emailField= findViewById(R.id.emailField);
        dobField= findViewById(R.id.dobField);
        phoneField= findViewById(R.id.phoneField);
        profileImageField= findViewById(R.id.profileImageField);

        //Fetching Customer details from Firebase
        database= FirebaseDatabase.getInstance();
        dbReference= database.getReference().child("Customers");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CustomerModel tempCustomer= dataSnapshot.getValue(CustomerModel.class);
                    if(tempCustomer.getEmail().equals(mUser.getEmail())) {
                        //Setting Data to XML
                        accoutnRef= dataSnapshot.getKey().toString();
                        nameField.setText(tempCustomer.getName());
                        emailField.setText("Email: "+ tempCustomer.getEmail());
                        dobField.setText("Date of Birth: "+ tempCustomer.getDob());
                        phoneField.setText("Phone #: "+ tempCustomer.getPhone());
                        Picasso.with(ProfileScreen.this).load(tempCustomer.getImagePath()).into(profileImageField);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileScreen.this, "*Error: Fetching data failed!", Toast.LENGTH_SHORT).show();
            }
        });

        editTxt= findViewById(R.id.editTxt);
        editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEdit= new Intent(ProfileScreen.this, EditProfileScreen.class);
                toEdit.putExtra("profileRef", accoutnRef);
                startActivity(toEdit);
            }
        });

        signOutBtn= findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileScreen.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                Intent toLogin= new Intent(ProfileScreen.this, LoginScreen.class);
                startActivity(toLogin);
                finish();
            }
        });

        deleteBtn= findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbAccountRef= FirebaseDatabase.getInstance().getReference().child("Customers").child(accoutnRef);
                dbAccountRef.removeValue();
                FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
                mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Intent toLogin= new Intent(ProfileScreen.this, LoginScreen.class);
                            startActivity(toLogin);
                            finish();
                        }
                    }
                });
            }
        });

//        mapBtn= findViewById(R.id.mapBtn);
//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ProfileScreen.this, MapsScreen.class));
//            }
//        });

    }
}