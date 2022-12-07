package com.assignment.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {

    EditText emailField, passwordField;
    Button loginBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Binding XML with Java
        emailField= findViewById(R.id.emailField);
        passwordField= findViewById(R.id.passwordField);
        loginBtn= findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Values Variables

                String email= emailField.getText().toString().trim();
                String pass= passwordField.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()) {
                    signInUser(email, pass);
                }
                else {
                    Toast.makeText(LoginScreen.this, "*Error: Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView signupBtn= findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignup= new Intent(LoginScreen.this, SignupScreen.class);
                startActivity(toSignup);
            }
        });
    }

    //Method for SingIn Procedure
    private void signInUser(String email, String pass) {
        mAuth= FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginScreen.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent toHome= new Intent(LoginScreen.this, HomeScreen.class);
                    startActivity(toHome);
                    finish();
                }
                else {
                    Toast.makeText(LoginScreen.this, "*Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}