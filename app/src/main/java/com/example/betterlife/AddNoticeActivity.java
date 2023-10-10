package com.example.betterlife;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

public class AddNoticeActivity extends AppCompatActivity {

    private EditText noticeEditText;
    private Button addNotice;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        noticeEditText = findViewById(R.id.noticeTextBox);
        addNotice = findViewById(R.id.addNotice);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(noticeEditText.getText().toString())){
                    Toast.makeText(AddNoticeActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    putNotice();
                }
                noticeEditText.setText(null);
            }
        });
    }

    private void putNotice(){
        Date date = new Date();
        HashMap<String, Object> map = new HashMap<>();
        map.put("notice", noticeEditText.getText().toString());
        map.put("writer", firebaseUser.getUid());
        map.put("date", date.toString());

        FirebaseDatabase.getInstance().getReference("Notice").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddNoticeActivity.this, "Notice added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddNoticeActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}