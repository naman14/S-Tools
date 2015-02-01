package com.naman14.stools.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naman14.stools.R;
import com.naman14.stools.location.LocationActivity;
import com.shamanland.fab.FloatingActionButton;

/**
 * Created by naman on 30/01/15.
 */
public class LocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_location, container, false);
        FloatingActionButton fab2 = (FloatingActionButton)v.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LocationActivity.class);

                startActivity(intent);
            }


        });



        return v;
    }}

