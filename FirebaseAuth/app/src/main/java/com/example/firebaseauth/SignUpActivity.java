package com.example.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import static android.util.Patterns.*;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();


    }

    public void switchActivity2(View view) {
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    public void authenticateUser(View view){

        if(registerUser()) {
            editTextEmail.setText("");
            editTextPassword.setText("");
        }
    }

    private boolean registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return false;
        }

        if(!EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }

        if(password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return false;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(SignUpActivity.this,ProfileActivity.class));
                    Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                } else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return true;
    }




    }

