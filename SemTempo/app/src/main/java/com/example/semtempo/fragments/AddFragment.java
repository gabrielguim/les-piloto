package com.example.semtempo.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.semtempo.R;

import model.Atividade;
import model.Prioridade;

public class AddFragment extends Fragment {

    private final int SEND_ICON = R.drawable.ic_send_white_24dp;

    private String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private RadioButton high_priority;
    private RadioButton medium_priority;
    private RadioButton low_priority;
    private EditText spent_time;
    private EditText label_priority;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(SEND_ICON);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateFields()) {
                    Prioridade priority;
                    if (high_priority.isSelected()) {
                        priority = Prioridade.ALTA;
                    } else if (medium_priority.isSelected()) {
                        priority = Prioridade.MEDIA;
                    } else {
                        priority = Prioridade.BAIXA;
                    }


                    Atividade atividade = new Atividade(autoCompleteTextView.getText().toString(), null, null, priority);
                    Toast.makeText(getActivity(), atividade.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, COUNTRIES);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        high_priority = (RadioButton) rootView.findViewById(R.id.high_radio);
        medium_priority = (RadioButton) rootView.findViewById(R.id.medium_radio);
        low_priority = (RadioButton) rootView.findViewById(R.id.low_radio);
        label_priority = (EditText) rootView.findViewById(R.id.label_priority);
        label_priority.setEnabled(false);

        spent_time = (EditText) rootView.findViewById(R.id.spent_hours);

        return rootView;
    }

    private boolean validateFields(){
        boolean fields_ok = true;

        return fields_ok;
    }

}