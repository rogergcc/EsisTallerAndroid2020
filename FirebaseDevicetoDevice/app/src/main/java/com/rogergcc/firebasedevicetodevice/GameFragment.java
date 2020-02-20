package com.rogergcc.firebasedevicetodevice;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rogergcc.firebasedevicetodevice.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    private static final String TAG="GameFragment";

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null){
            GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
            String messg = args.getMessagearg();
            Log.e(TAG,"onvierCreated"+messg);

            User user = args.getUser();
            Log.e(TAG,"onvierCreated"+user.toString());
        }
        final NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.finishGameButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_gameFragment_to_endGameFragment);
            }
        });
    }

}
