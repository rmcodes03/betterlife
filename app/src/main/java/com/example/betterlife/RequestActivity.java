package com.example.betterlife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.betterlife.Model.Receive;
import com.example.betterlife.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends AppCompatActivity {

    private TextView donorName, address, phoneNo, emailId, medicineName, quantity, type, expirationDate;
    private CircleImageView profileImage;
    private Button requestButton;
    private AlertDialog.Builder requestAlertDialog;
    private FirebaseUser firebaseUser;
    private Context context;
    String medicineDonatedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        donorName = findViewById(R.id.donorName);
        address = findViewById(R.id.address);
        phoneNo = findViewById(R.id.phoneNo);
        emailId = findViewById(R.id.emailId);
        medicineName = findViewById(R.id.medicineName);
        quantity = findViewById(R.id.quantity);
        type = findViewById(R.id.type);
        expirationDate = findViewById(R.id.expirationDate);
        profileImage = findViewById(R.id.profileImage);
        requestButton = findViewById(R.id.requestButton);
        requestAlertDialog = new AlertDialog.Builder(RequestActivity.this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String donationId = intent.getStringExtra("id");
        String donorId = intent.getStringExtra("donorId");
        String historyId = intent.getStringExtra("historyId");

        FirebaseDatabase.getInstance().getReference("Users").child(donorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String username = user.getName();
                if(username.equals("")){
                    donorName.setText("Donor name: -");
                }else{
                    donorName.setText("Donor name: "+username);
                }
                String profileImageUri = user.getProfileImageUrl();
                if(profileImageUri.equals("")){
                    Glide.with(getApplicationContext()).load(R.drawable.user).into(profileImage);
                }else{
                    Glide.with(getApplicationContext()).load(profileImageUri).into(profileImage);
                }
                String phoneNumber = user.getPhoneNumber();
                if(phoneNumber.equals("")){
                    phoneNo.setText("Phone number: -");
                }
                else{
                    phoneNo.setText("Phone number: "+phoneNumber);
                }
                String addressText = user.getAddress();
                if(addressText.equals("")){
                    address.setText("Address: -");
                }
                else{
                    address.setText("Address: "+addressText);
                }
                emailId.setText("Email: "+user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("DonatedItems").child(historyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Receive receive = snapshot.getValue(Receive.class);
                medicineName.setText("Name: "+receive.getMedicineName());
                type.setText("Type: "+receive.getType());
                quantity.setText("Quantity: "+receive.getQuantity());
                expirationDate.setText("Expiration Date: "+receive.getExpiryDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestAlertDialog.setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestItem(donorId, firebaseUser.getUid(), historyId, donationId);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = requestAlertDialog.create();
                //alert.setTitle("Request Medicine");
                alert.show();
            }
        });
    }

    public void requestItem(String donor, String receiver, String historyId, String donationId) {
        HashMap<String, Object> map = new HashMap<>();
        String requestId = FirebaseDatabase.getInstance().getReference().push().getKey();
        map.put("donor", donor);
        map.put("receiver", receiver);
        map.put("requestId", requestId);
        map.put("historyId", historyId);

        FirebaseDatabase.getInstance().getReference("RequestedMedicines").child(requestId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(), "Items requested", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("MedicinesDonated").child(donationId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(RequestActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Toast.makeText(RequestActivity.this, "Items requested", Toast.LENGTH_SHORT).show();
            }
        });
    }
}