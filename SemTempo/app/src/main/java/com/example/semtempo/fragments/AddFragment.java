package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.semtempo.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Atividade;
import model.Horario;
import model.Prioridade;

public class AddFragment extends Fragment {

    private final int SEND_ICON = R.drawable.ic_send_white_24dp;

    private String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    private ImageView high_priority;
    private ImageView medium_priority;
    private ImageView low_priority;
    private EditText spent_time;
    private EditText label_priority;
    private AutoCompleteTextView autoCompleteTextView;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add, container, false);
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(SEND_ICON);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateFields()) {
                    Prioridade priority;
                    if (isSelected(high_priority)) {
                        priority = Prioridade.ALTA;
                    } else if (isSelected(medium_priority)) {
                        priority = Prioridade.MEDIA;
                    } else {
                        priority = Prioridade.BAIXA;
                    }

                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Registrando atividade!");
                    dialog.setCancelable(false);
                    dialog.show();

                    Atividade atividade = new Atividade(autoCompleteTextView.getText().toString(), priority);
                    Calendar creation_date = new GregorianCalendar();
                    atividade.registrarNovoHorario(new Horario(Integer.parseInt(spent_time.getText().toString()), creation_date));
                    Toast.makeText(getActivity(), atividade.toString(), Toast.LENGTH_SHORT).show();

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                    fragmentTransaction.commit();


                }

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, COUNTRIES);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        high_priority = (ImageView) rootView.findViewById(R.id.high_radio);
        medium_priority = (ImageView) rootView.findViewById(R.id.medium_radio);
        low_priority = (ImageView) rootView.findViewById(R.id.low_radio);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.high_priority:
                        high_priority.animate().scaleX(1.3f).scaleY(1.3f);
                        medium_priority.animate().scaleX(1).scaleY(1);
                        low_priority.animate().scaleX(1).scaleY(1);
                        break;
                    case R.id.medium_priority:
                        high_priority.animate().scaleX(1).scaleY(1);
                        medium_priority.animate().scaleX(1.3f).scaleY(1.3f);
                        low_priority.animate().scaleX(1).scaleY(1);
                        break;
                    case R.id.low_priority:
                        high_priority.animate().scaleX(1).scaleY(1);
                        medium_priority.animate().scaleX(1).scaleY(1);
                        low_priority.animate().scaleX(1.3f).scaleY(1.3f);
                        break;
                }
            }
        };

        high_priority.setOnClickListener(listener);
        medium_priority.setOnClickListener(listener);
        low_priority.setOnClickListener(listener);

        label_priority = (EditText) rootView.findViewById(R.id.label_priority);
        label_priority.setEnabled(false);

        spent_time = (EditText) rootView.findViewById(R.id.spent_hours);

        return rootView;
    }

    private boolean validateFields(){
        boolean fields_ok = true;
        String err_msg = "";

        if (!isSelected(high_priority) && !isSelected(medium_priority) && !isSelected(low_priority)){
            err_msg += "Selecione uma prioridade!";
            fields_ok = false;

        } if (autoCompleteTextView.getText().toString().isEmpty()) {
            if (!err_msg.equals(""))
                err_msg += "\n";
            err_msg += "Informe-nos o nome da atividade";
            fields_ok = false;

        } if (spent_time.getText().toString().isEmpty()){
            if (!err_msg.equals(""))
                err_msg += "\n";
            err_msg += "Insira um tempo válido";
            fields_ok = false;

        }

        if (!fields_ok) {
            Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
        }

        return fields_ok;
    }

    private boolean isSelected(ImageView priority){
        return priority.getScaleX() == 1.3f;
    }

}