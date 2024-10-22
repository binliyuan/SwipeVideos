package com.deepscience.business;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.deepscience.business.fragment.DashboardFragment;
import com.deepscience.business.fragment.HomeFragment;
import com.deepscience.business.fragment.NotificationsFragment;
import com.deepscience.business.utils.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_dashboard:
                        selectedFragment = new DashboardFragment();
                        break;
                    case R.id.navigation_notifications:
                        selectedFragment = new NotificationsFragment();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        // 默认显示第一个Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission.requestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
