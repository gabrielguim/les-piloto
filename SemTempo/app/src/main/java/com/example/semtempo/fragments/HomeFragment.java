package com.example.semtempo.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.semtempo.R;

public class HomeFragment extends Fragment {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFragment fragment = new AddFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }
        });

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}