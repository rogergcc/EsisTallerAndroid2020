package com.rogergcc.firebasedevicetodevice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        navController = Navigation.findNavController(this, R.id.fragment2);

        NavigationUI.setupActionBarWithNavController(this, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {

        navController.navigateUp();

        return super.onSupportNavigateUp();
    }

}
