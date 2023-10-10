package com.example.betterlife.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterlife.Model.Articles;
import com.example.betterlife.Model.Headlines;
import com.example.betterlife.Adapter.NewsAdapter;
import com.example.betterlife.ApiClient;
import com.example.betterlife.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    RecyclerView recyclerView;
    final String API_KEY = "a2b782fb1c5a4c0ab1d12f11423c3ae9";
    NewsAdapter adapter;
    List<Articles> articles = new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String country = getCountry();
        retrieveJSON(country, API_KEY);

        return view;
    }

    public void retrieveJSON(String country, String apiKey) {
        //String category="health";
        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country, "health", apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new NewsAdapter(getContext(), articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        return "in";
    }
}