package com.example.betterlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String emailText, passwordText;

    private ProgressDialog progressDialogLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialogLogin = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = emailEditText.getText().toString();
                passwordText = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(LoginActivity.this,"Username and password cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    loginUser(emailText, passwordText);
                }
            }
        });
    }

    private void loginUser(String emailText, String passwordText){
        progressDialogLogin.setMessage("Logging in");
        progressDialogLogin.show();

        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialogLogin.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogLogin.dismiss();
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}