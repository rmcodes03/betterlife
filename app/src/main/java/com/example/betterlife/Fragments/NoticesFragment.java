package com.example.betterlife.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Adapter.NoticeAdapter;
import com.example.betterlife.AddNoticeActivity;
import com.example.betterlife.Model.Notice;
import com.example.betterlife.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticesFragment extends Fragment {

    private FloatingActionButton addNoticeButton;
    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;

    public NoticesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);
        addNoticeButton = view.findViewById(R.id.addNoticeButton);
        recyclerView = view.findViewById(R.id.noticeRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        noticeList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Notice notice = ds.getValue(Notice.class);
                    noticeList.add(notice);
                    noticeAdapter = new NoticeAdapter(getContext(), noticeList);
                    recyclerView.setAdapter(noticeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        addNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNoticeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}