package com.example.betterlife.ProfileSection;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Adapter.GalleryAdapter;
import com.example.betterlife.Model.Post;
import com.example.betterlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPosts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textView;
    private GalleryAdapter galleryAdapter;
    private List<Post> postList;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posts);
        textView  = findViewById(R.id.textView);
        currentUserId = FirebaseAuth.getInstance().getUid();
        //textView.setText(currentUserId);

        recyclerView = findViewById(R.id.galleryRecyclerView);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        //galleryAdapter = new GalleryAdapter(this, postList);

        //recyclerView.setAdapter(galleryAdapter);

        //loadPosts(currentUserId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if(currentUserId.equals(post.getUser())) {
                        postList.add(post);
                    }
                    galleryAdapter = new GalleryAdapter(ShowPosts.this, postList);
                    recyclerView.setAdapter(galleryAdapter);
                    galleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case 101:
                galleryAdapter.deletePost();
                return true;
            default:
                return false;
        }
    }

     */

   /*
    private void loadPosts(String currentUserId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if(currentUserId.equals(post.getUser())) {
                        postList.add(post);
                    }
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), postList);
                    recyclerView.setAdapter(galleryAdapter);
                    galleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

     */
}