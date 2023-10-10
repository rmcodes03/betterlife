package com.example.betterlife.ProfileSection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.betterlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.HashMap;

public class SharePost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;

    private Button shareButton;
    private Button selectImageButton;
    private ImageView postImageView;
    private SocialAutoCompleteTextView postCaption;
    private ProgressBar progressBar;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private FirebaseUser fuser;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        postImageView = findViewById(R.id.postImageView);
        shareButton = findViewById(R.id.shareButton);
        postCaption = findViewById(R.id.postCaption);
        selectImageButton = findViewById(R.id.selectImageButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentUserId = fuser.getUid();

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2,2)
                        .getIntent(getApplicationContext());
                        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = postCaption.getText().toString();
                if(imageUri!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    StorageReference postRef = storageReference.child("Post Images").child(System.currentTimeMillis() + ".jpg");
                    postRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Date date = new Date();
                                        String postId = databaseReference.push().getKey();
                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image", uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("caption", caption);
                                        postMap.put("time", date.toString());
                                        postMap.put("postId", postId);

                                        databaseReference.child("Posts").child(postId).setValue(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if(task.isSuccessful()){
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(SharePost.this, "Post added", Toast.LENGTH_SHORT).show();
                                                    postImageView.setImageURI(null);
                                                    postCaption.setText("");
                                                    startActivity(new Intent(SharePost.this, ShowPosts.class));
                                                    finish();
                                                }
                                                else {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(SharePost.this, "Something wnet wrong", Toast.LENGTH_SHORT).show();
                                                    postImageView.setImageURI(null);
                                                    postCaption.setText(null);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SharePost.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SharePost.this, "Add image and caption", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Image Selected", Toast.LENGTH_SHORT).show();
                imageUri = result.getUri();
                Picasso.get().load(imageUri).into(postImageView);
            }
        }
    }
}
