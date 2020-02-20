package com.rogergcc.firebasedevicetodevice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment_bottom);


        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}
