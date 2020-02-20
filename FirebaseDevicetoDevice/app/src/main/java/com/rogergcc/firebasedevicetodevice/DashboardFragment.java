package com.rogergcc.firebasedevicetodevice;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rogergcc.firebasedevicetodevice.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.startGameButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DashboardFragmentDirections.ActionDashboardFragmentToGameFragment action = DashboardFragmentDirections.actionDashboardFragmentToGameFragment();
//                action.setMessagearg("This message passed argums");
//
//                navController.navigate(action);
////                navController.navigate(R.id.action_dashboardFragment_to_gameFragment);
                    User user = new User("11","Roger","rogergcc@gmail.com");

                    DashboardFragmentDirections.ActionDashboardFragmentToGameFragment action = DashboardFragmentDirections.actionDashboardFragmentToGameFragment(user);
                    action.setMessagearg("This message arguments");

                    navController.navigate(action);

            }
        });
    }

}
