package com.example.foodalgorithms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodalgorithms.ui.combo.ComboFragment;
import com.example.foodalgorithms.ui.home.HomeFragment;
import com.example.foodalgorithms.ui.profile.ProfileFragment;
import com.example.foodalgorithms.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, new HomeFragment()).commit();
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, new SearchFragment()).commit();
                    return true;
                }
                else if (itemId == R.id.navigation_combo) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, new ComboFragment()).commit();
                    return true;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, new ProfileFragment()).commit();
                return true;

            }
        });

    }

}