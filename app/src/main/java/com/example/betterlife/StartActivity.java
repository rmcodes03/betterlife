package com.example.betterlife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private Button loginButton, signUpButton;
    private ImageView logoImageView;
    private RelativeLayout relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginButton = findViewById(R.id.goToLoginActivityButton);
        signUpButton = findViewById(R.id.goToSignUpActivityButton);
        logoImageView = findViewById(R.id.logoImageView);
        relativeLayout2 = findViewById(R.id.relativeLayout2);

        logoImageView.setX(-500);
        logoImageView.animate().translationX(0).setDuration(1500);

        relativeLayout2.setX(1000);
        relativeLayout2.animate().translationX(0).setDuration(1500);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null ){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }
}