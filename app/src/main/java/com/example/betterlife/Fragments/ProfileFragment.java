package com.example.betterlife.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.betterlife.LogoutActivity;
import com.example.betterlife.Model.User;
import com.example.betterlife.ProfileSection.ShowPosts;
import com.example.betterlife.R;
import com.example.betterlife.ShowDonations;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView profileImageView;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private TextView emailTextView;
    private Button changeProfileImage;
    private Button saveButton;
    private AlertDialog.Builder saveAlertDiaglog;
    private ProgressDialog progressDialog;

    private Uri profileImageUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;

    private FirebaseUser fuser;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        emailTextView = view.findViewById(R.id.emailTextView);
        addressEditText = view.findViewById(R.id.addressEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        profileImageView = view.findViewById(R.id.profileImageView);
        changeProfileImage = view.findViewById(R.id.changeProfileImageButton);
        saveButton = view.findViewById(R.id.saveChangesButton);
        saveAlertDiaglog = new AlertDialog.Builder(getContext());
        progressDialog = new ProgressDialog(getContext());

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");

        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                emailTextView.setText(user.getEmail());
                nameEditText.setText(user.getName());
                phoneEditText.setText(user.getPhoneNumber());
                addressEditText.setText(user.getAddress());
                //Picasso.get().load(R.drawable.user).into(profileImageView);
                try {
                    Picasso.get().load(user.getProfileImageUrl()).into(profileImageView);
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else {
                        Intent intent = CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .getIntent(getActivity());
                        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAlertDiaglog.setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressDialog.setMessage("Saving...");
                                progressDialog.show();
                                uploadImage();
                                updateProfile();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = saveAlertDiaglog.create();
                alert.setTitle("Update Profile");
                alert.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Image Selected", Toast.LENGTH_SHORT).show();
                profileImageUri = result.getUri();
                Picasso.get().load(profileImageUri).into(profileImageView);
            }
        }
    }

    private void updateProfile(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("email",emailTextView.getText().toString());
        map.put("name",nameEditText.getText().toString());
        map.put("address", addressEditText.getText().toString());
        map.put("phoneNumber", phoneEditText.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).updateChildren(map);
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage(){
        try {
            if (profileImageView != null) {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpg");
                uploadTask = fileRef.putFile(profileImageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String url = downloadUri.toString();

                            FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("profileImageUrl").setValue(url);
                        }
                        else {
                            Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getContext(), "Profile Picture updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.myPost:
                startActivity(new Intent(getActivity(), ShowPosts.class));
                return true;
            case R.id.myDonations:
                startActivity(new Intent(getActivity(), ShowDonations.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getActivity(), LogoutActivity.class);
                startActivity(intent);
            default:
                return false;
        }
    }

}