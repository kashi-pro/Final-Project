package com.assignment.booking;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SignupScreen extends AppCompatActivity {

    //XML Component Objects
    EditText nameField, emailField, phoneField, dobField, passField, cpassField;
    ProgressBar pgBar;
    Button signupBtn;
    ImageView imageField;
    Uri imagePath;

    //String data variable to store data URL
    String imageStoragePath;

    //User values Variables
    String name, email, phone, dob, pass, cpass;

    //Firebase Authentication Object
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    //Request code for gallery to get data
    final int GALLERY_REQUEST_CODE= 999;

    //Firebase Database Objects
    FirebaseDatabase database;
    DatabaseReference dbReference;

    //Cloud Storage Objects
    FirebaseStorage fileStorage;
    StorageReference fileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Image onClick Listener
        imageField= findViewById(R.id.imageField);
        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGallery= new Intent(Intent.ACTION_PICK);
                openGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
            }
        });

        //Signup onClick Listener
        signupBtn= findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Layout Binding
                nameField= findViewById(R.id.nameField);
                emailField= findViewById(R.id.emailField);
                phoneField= findViewById(R.id.phoneField);
                dobField= findViewById(R.id.dobField);
                passField= findViewById(R.id.passField);
                cpassField= findViewById(R.id.cpassField);

                //Getting values to variables
                name= nameField.getText().toString();
                email= emailField.getText().toString().trim();
                phone= phoneField.getText().toString();
                dob= dobField.getText().toString();
                pass= passField.getText().toString();
                cpass= cpassField.getText().toString();

                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty()
                        || pass.isEmpty() || cpass.isEmpty()) {
                    Toast.makeText(SignupScreen.this, "*Error: Fill all field!", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(cpass)) {
                    Toast.makeText(SignupScreen.this, "*Error: Both passwords are different", Toast.LENGTH_SHORT).show();
                }
                else if(imagePath == null) {
                    Toast.makeText(SignupScreen.this, "*Error: Please upload image", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(email, pass);
                }
            }
        });

        //Singin onCLick Listener
        TextView signinBtn= findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin= new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(toLogin);
            }
        });

    }

    //Function to get image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Condition to check the coming data is not null
        if(resultCode == RESULT_OK) {
            if(requestCode == GALLERY_REQUEST_CODE) { //Condition to check the request code
                imagePath= data.getData();
                imageField.setImageURI(imagePath);
            }
        }
    }

    //Function to register user and save data in database
    private void registerUser(String email, String pass) {
        pgBar= findViewById(R.id.pgBar);
        pgBar.setVisibility(View.VISIBLE);
        signupBtn.setVisibility(View.GONE);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    uploadImageToStorage();

                }
                else {
                    Toast.makeText(SignupScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    pgBar.setVisibility(View.GONE);
                    signupBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Function to check the file extensions
    private String getFileExtension(Uri imageUri) {
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(imageUri));
    }

    //Function to upload profile image to cloud storage
    private void uploadImageToStorage() {
        //Getting date-time to concatenate with image name
        Date currentTime= Calendar.getInstance().getTime();

        //Uploading Image to Cloud Storage
        fileStorage= FirebaseStorage.getInstance();
        fileReference= fileStorage.getReference("profileImage");
        String imgName= "IMG" + System.currentTimeMillis() +"." +getFileExtension(imagePath);
        fileReference.child(imgName).putFile(imagePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imageStoragePath= String.valueOf(task.getResult());
                                storeDataToDB();
                            }
                        });
                    }
                });
//                .addOnCompleteListener(SignupScreen.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if(task.isSuccessful()) {
//                    imageStoragePath= task.getResult().getStorage().getDownloadUrl().toString();
//                    storeDataToDB();
//                }
//                else {
//                    pgBar.setVisibility(View.GONE);
//                    signupBtn.setVisibility(View.VISIBLE);
//                    Toast.makeText(SignupScreen.this, "*Error: Image Uploading Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    //Function to store customer's data in realtime database
    private void storeDataToDB() {
        //Uploading user data to realtime database
        database= FirebaseDatabase.getInstance();
        dbReference= database.getReference().child("Customers");

        CustomerModel customer= new CustomerModel(name, email, phone, dob, pass, imageStoragePath);

        dbReference.push().setValue(customer).addOnSuccessListener(SignupScreen.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignupScreen.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                Intent toLogin= new Intent(SignupScreen.this, LoginScreen.class);
                pgBar.setVisibility(View.GONE);
                signupBtn.setVisibility(View.VISIBLE);
                startActivity(toLogin);
                finish();
            }
        }).addOnCanceledListener(SignupScreen.this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(SignupScreen.this, "*Error while storing data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}