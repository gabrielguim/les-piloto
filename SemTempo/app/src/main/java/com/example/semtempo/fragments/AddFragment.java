package com.example.semtempo.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.semtempo.R;

public class AddFragment extends Fragment {

    private final int SEND_ICON = R.drawable.ic_send_white_24dp;
    private AutoCompleteTextView autoCompleteTextView;
    private String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(SEND_ICON);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "enviando", Toast.LENGTH_SHORT).show();

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, COUNTRIES);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        return rootView;
    }

}