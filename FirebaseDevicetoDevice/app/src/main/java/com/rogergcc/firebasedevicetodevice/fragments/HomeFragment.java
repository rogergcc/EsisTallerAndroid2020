package com.rogergcc.firebasedevicetodevice.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rogergcc.firebasedevicetodevice.R;
import com.google.android.gms.plus.PlusOneButton;

import java.util.Objects;

/**
 * A fragment with a Google +1 button.
 */
public class HomeFragment extends Fragment {





    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Find the +1 button
        Objects.requireNonNull(getActivity()).setTitle("Dashboard");



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.

    }


}
