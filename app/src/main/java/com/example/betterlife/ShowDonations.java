package com.example.betterlife;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Adapter.HistoryAdapter;
import com.example.betterlife.Model.History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDonations extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<History> historyList;
    private HistoryAdapter historyAdapter;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_donations);

        recyclerView = findViewById(R.id.myDonationRecyclerView);
        currentUserId = FirebaseAuth.getInstance().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowDonations.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        historyList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonatedItems");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    History history = ds.getValue(History.class);
                    if(currentUserId.equals(history.getDonor())) {
                        historyList.add(history);
                    }
                    historyAdapter = new HistoryAdapter(ShowDonations.this, historyList);
                    recyclerView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDonations.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}