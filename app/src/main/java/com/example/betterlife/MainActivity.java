package com.example.betterlife;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.betterlife.Fragments.AboutFragment;
import com.example.betterlife.Fragments.CommunityFragment;
import com.example.betterlife.Fragments.DonateAndReceiveFragment;
import com.example.betterlife.Fragments.HomeFragment;
import com.example.betterlife.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_community:
                        fragment = new CommunityFragment();
                        break;
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.nav_donateAndReceive:
                        fragment = new DonateAndReceiveFragment();
                        break;
                    case R.id.nav_about:
                        fragment = new AboutFragment();
                        break;
                }
                if(fragment !=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CommunityFragment()).commit();

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

            }
        });
    }
}