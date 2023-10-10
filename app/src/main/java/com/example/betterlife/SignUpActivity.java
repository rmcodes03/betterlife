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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView loginTextView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String emailText, passwordText;

    private ProgressDialog progressDialogSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginTextView = findViewById(R.id.loginTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialogSignUp = new ProgressDialog(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = emailEditText.getText().toString();
                passwordText = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(SignUpActivity.this,"Username and password cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(emailText, passwordText);
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    public void registerUser(String emailText, String passwordText){
        progressDialogSignUp.setMessage("Registering");
        progressDialogSignUp.show();

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialogSignUp.dismiss();
                HashMap<String, Object> map = new HashMap<>();
                map.put("email", emailText);
                map.put("id", firebaseAuth.getCurrentUser().getUid());
                map.put("address","");
                map.put("phoneNumber","");
                map.put("name","");
                map.put("profileImageUrl","");

                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Login with registered email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogSignUp.dismiss();
                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}