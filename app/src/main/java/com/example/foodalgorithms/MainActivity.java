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

//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
////        BottomNavigationView navView = findViewById(R.id.nav_view);
////        // Passing each menu ID as a set of Ids because each
////        // menu should be considered as top level destinations.
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.navigation_home, R.id.navigation_search, R.id.navigation_search,R.id.navigation_combo,R.id.navigation_notifications)
////                .build();
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
////        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
////        NavigationUI.setupWithNavController(binding.navView, navController);
//
//
//        TabLayout tabLayout = findViewById(R.id);
//        int currentPosition = -1;
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                // go to the respective tab
//                int position = tab.getPosition();
//                if (position != currentPosition) {
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    if (position == 0) {
////                        HomeFragment homeFragment = new HomeFragment();
////                        fragmentTransaction.replace(R.id.fragmentContainerView, homeFragment);
////                        Log.i("POSITION", "position: " + position);
////                    } else if (position == 1) {
////                        ExploreFragment exploreFragment = new ExploreFragment();
////                        fragmentTransaction.replace(R.id.fragmentContainerView, exploreFragment);
////                        Log.i("POSITION", "position: " + position);
////                    } else if (position == 2) {
////                        FavoritesFragment favoritesFragment = new FavoritesFragment();
////                        fragmentTransaction.replace(R.id.fragmentContainerView, favoritesFragment);
////                        Log.i("POSITION", "position: " + position);
////                    } else if (position == 3) {
////                        HistoryFragment historyFragment = new HistoryFragment();
////                        fragmentTransaction.replace(R.id.fragmentContainerView, historyFragment);
////                        Log.i("POSITION", "position: " + position);
////                    } else {
////                        SettingsFragment settingsFragment = new SettingsFragment();
////                        fragmentTransaction.replace(R.id.fragmentContainerView, settingsFragment);
////                        Log.i("POSITION", "position: " + position);
////                    }
//                    fragmentTransaction.commit();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

}