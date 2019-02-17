package com.example.finaltodo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finaltodo.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import static android.util.Patterns.EMAIL_ADDRESS;

public class SignupActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextPasswordCnf;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textViewLogin;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        FirebaseApp.initializeApp(this);


        editTextEmail = (EditText) findViewById(R.id.editTextName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordCnf = (EditText) findViewById(R.id.editTextPasswordConfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        btnSignup = (Button) findViewById(R.id.buttonSignUp);

        textViewLogin = findViewById(R.id.loginText);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerUser()){
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }
            }
        });
    }

    public boolean registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordCnf = editTextPasswordCnf.getText().toString().trim();

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

        if(!password.equals(passwordCnf)){
            editTextPassword.setError("Passwords do not match");
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
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(SignupActivity.this,MainActivity.class));

                    Toast.makeText(getApplicationContext(), "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return true;
    }

    public void forgotPass(View view){
        Toast.makeText(getApplicationContext(), "Too Bad :`( ", Toast.LENGTH_LONG).show();
    }
}
