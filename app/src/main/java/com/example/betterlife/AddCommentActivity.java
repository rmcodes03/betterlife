package com.example.betterlife;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Adapter.CommentAdapter;
import com.example.betterlife.Model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private Button addCommentButton;
    private SocialEditText commentEditText;
    private String postId;
    private String user;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        addCommentButton =findViewById(R.id.addCommentButton);
        commentEditText = findViewById(R.id.commentEditText);
        recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);

        recyclerView.setAdapter(commentAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        user = intent.getStringExtra("user");

        getComment();
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(commentEditText.getText().toString())){
                    Toast.makeText(AddCommentActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    putComment();
                }
                commentEditText.setText(null);
            }
        });
    }

    private void getComment() {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Comment comment = snapshot1.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putComment(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", commentEditText.getText().toString());
        map.put("user", firebaseUser.getUid());

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddCommentActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddCommentActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}