package com.example.betterlife.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.betterlife.Adapter.SelectionPagerAdapter;
import com.example.betterlife.NewsActivity;
import com.example.betterlife.R;
import com.google.android.material.tabs.TabLayout;

public class CommunityFragment extends Fragment {

    private View myFragment;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment getInstance() {
        return new CommunityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_community, container, false);

        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.communityTabLayout);

        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SelectionPagerAdapter adapter = new SelectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new NewsFragment(), "News");
        adapter.addFragment(new NoticesFragment(),"Notices");

        viewPager.setAdapter(adapter);
    }
}