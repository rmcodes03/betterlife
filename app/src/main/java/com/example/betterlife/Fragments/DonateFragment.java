package com.example.betterlife.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.betterlife.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DonateFragment extends Fragment {

    private EditText expiryDateEditText;
    private EditText medicineEditText;
    private EditText typeEditText;
    private EditText quantityText;
    private Button donateButton;
    private FirebaseUser firebaseUser;

    private DatePickerDialog.OnDateSetListener setListener;

    public DonateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
        expiryDateEditText = view.findViewById(R.id.expiryDate);
        medicineEditText = view.findViewById(R.id.medicineName);
        typeEditText = view.findViewById(R.id.type);
        quantityText = view.findViewById(R.id.quantity);
        donateButton = view.findViewById(R.id.donateButton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        expiryDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"-"+month+"-"+year;
                expiryDateEditText.setText(date);
            }
        };

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medicineName = medicineEditText.getText().toString();
                String quantity = quantityText.getText().toString();
                String type = typeEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                if(TextUtils.isEmpty(medicineName) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(type) || TextUtils.isEmpty(expiryDate)){
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    String time = date.toString();
                    donateItem(time);
                }
            }
        });
        return view;
    }

    private void donateItem(String time){
        HashMap<String, Object> map = new HashMap<>();
        String donationId = FirebaseDatabase.getInstance().getReference().push().getKey();
        String historyId = FirebaseDatabase.getInstance().getReference().push().getKey();

        map.put("medicineName", medicineEditText.getText().toString());
        map.put("quantity", quantityText.getText().toString());
        map.put("type", typeEditText.getText().toString());
        map.put("expiryDate", expiryDateEditText.getText().toString());
        map.put("donor", firebaseUser.getUid());
        map.put("id", donationId);
        map.put("historyId", historyId);
        map.put("date", time);

        FirebaseDatabase.getInstance().getReference("MedicinesDonated").child(donationId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Items added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("medicineName", medicineEditText.getText().toString());
        map1.put("quantity", quantityText.getText().toString());
        map1.put("type", typeEditText.getText().toString());
        map1.put("expiryDate", expiryDateEditText.getText().toString());
        map1.put("donor", firebaseUser.getUid());
        map1.put("historyId", historyId);
        map1.put("date", time);

        FirebaseDatabase.getInstance().getReference("DonatedItems").child(historyId).setValue(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getContext(), "Items added", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        medicineEditText.setText("");
        quantityText.setText("");
        typeEditText.setText("");
        expiryDateEditText.setText("");
    }
}