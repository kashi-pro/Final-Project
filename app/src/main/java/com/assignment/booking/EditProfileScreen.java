package com.assignment.booking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfileScreen extends AppCompatActivity {

    //XML Component Objects
    EditText nameField, phoneField, dobField;
    ProgressBar pgBar;
    Button editBtn;
    Uri imagePath;

    //User values Variables
    String name, phone, dob;

    //for profile reference
    String accountRef;

    //Firebase Database Objects
    FirebaseDatabase database;
    DatabaseReference dbReference;

    //Customer Object to Store data
    CustomerModel customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        accountRef= getIntent().getStringExtra("profileRef");

        nameField= findViewById(R.id.nameField);
        dobField= findViewById(R.id.dobField);
        phoneField= findViewById(R.id.phoneField);
        pgBar= findViewById(R.id.pgBar);

        getProfileData();

        editBtn= findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name= nameField.getText().toString();
                phone= phoneField.getText().toString();
                dob= dobField.getText().toString();

                if(name.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
                    Toast.makeText(EditProfileScreen.this, "*Error: Fill all field!", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadEditProfile();
                }

            }
        });
    }

    private void getProfileData() {
        database= FirebaseDatabase.getInstance();
        dbReference= database.getReference().child("Customers");
        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    customer= dataSnapshot.getValue(CustomerModel.class);
                    if(customer.getEmail().equals(mUser.getEmail())) {
                        //Setting Data to XML
                        accountRef= dataSnapshot.getKey().toString();
                        nameField.setText(customer.getName());
                        dobField.setText(customer.getDob());
                        phoneField.setText(customer.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileScreen.this, "*Error: Fetching data failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void uploadEditProfile() {
        pgBar.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.GONE);
        customer.setName(name);
        customer.setDob(dob);
        customer.setPhone(phone);

        dbReference.child(accountRef).setValue(customer)
                .addOnSuccessListener(EditProfileScreen.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileScreen.this, "Edit Sucessfully", Toast.LENGTH_SHORT).show();
                        Intent toLogin= new Intent(EditProfileScreen.this, ProfileScreen.class);
                        startActivity(toLogin);
                        finish();
                    }
                });
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()) {
//                    Toast.makeText(EditProfileScreen.this, "Profile Edit Successfully", Toast.LENGTH_SHORT).show();
//                    pgBar.setVisibility(View.GONE);
//                    editBtn.setVisibility(View.VISIBLE);
//                    startActivity(new Intent(EditProfileScreen.this, ProfileScreen.class));
//                    finish();
//                }
//                else {
//                    pgBar.setVisibility(View.GONE);
//                    editBtn.setVisibility(View.VISIBLE);
//                    Toast.makeText(EditProfileScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}