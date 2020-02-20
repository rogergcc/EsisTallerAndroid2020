package com.rogergcc.firebasedevicetodevice.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rogergcc.firebasedevicetodevice.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //Find the +1 button
        Objects.requireNonNull(getActivity()).setTitle("List");



        return view;
    }

}
