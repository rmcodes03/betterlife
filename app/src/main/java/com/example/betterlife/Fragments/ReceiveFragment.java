package com.example.betterlife.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Adapter.ReceiveAdapter;
import com.example.betterlife.Model.Receive;
import com.example.betterlife.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReceiveFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Receive> receiveList;
    private ReceiveAdapter receiveAdapter;
    private SearchView searchBar;

    public ReceiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive, container, false);
        recyclerView = view.findViewById(R.id.receiveRecyclerView);
        searchBar = view.findViewById(R.id.searchBar);
        String currentUserId = FirebaseAuth.getInstance().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        receiveList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MedicinesDonated");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiveList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Receive receive = ds.getValue(Receive.class);
                    if(currentUserId.equals(receive.getDonor())) {
                        continue;
                    }
                    else{
                        receiveList.add(receive);
                    }
                    receiveAdapter = new ReceiveAdapter(getContext(), receiveList);
                    recyclerView.setAdapter(receiveAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    receiveAdapter.getFilter().filter(newText);
                }
                catch (Exception e){
                    //Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return view;
    }
}