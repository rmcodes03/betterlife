package com.example.betterlife.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.betterlife.Adapter.SelectionPagerAdapter;
import com.example.betterlife.R;
import com.google.android.material.tabs.TabLayout;

public class DonateAndReceiveFragment extends Fragment {

    private View myFragment;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public DonateAndReceiveFragment() {
        // Required empty public constructor
    }

    public static DonateAndReceiveFragment newInstance() {
        return new DonateAndReceiveFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_donate_and_receive, container, false);
        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.donateAndReceiveTabLayout);

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
        SelectionPagerAdapter adapter1 = new SelectionPagerAdapter(getChildFragmentManager());

        adapter1.addFragment(new DonateFragment(), "Donate");
        adapter1.addFragment(new ReceiveFragment(),"Receive");

        viewPager.setAdapter(adapter1);
    }
}