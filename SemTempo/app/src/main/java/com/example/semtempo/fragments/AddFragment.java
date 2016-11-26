package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.semtempo.R;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.FirebaseController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;

public class AddFragment extends Fragment {

    private final int SEND_ICON = R.drawable.ic_send_white_24dp;

    private List<Atividade> atividades;
    private List<String> ATIVIDADES;
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
        addFab.setVisibility(View.VISIBLE);

        setUp();
        configureAutoComplete();

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

                    Atividade atividade = new Atividade(autoCompleteTextView.getText().toString(), priority);
                    Calendar creation_date = new GregorianCalendar();
                    atividade.registrarNovoHorario(new Horario(Integer.parseInt(spent_time.getText().toString()), creation_date));

                    List<Atividade> lista = new ArrayList<Atividade>();
                    lista.add(atividade);

                    FirebaseController.getFirebase().child(UsuarioController.getInstance().getCurrentUser().getDisplayName()).setValue(lista);

                    showProgressDialog();

                }

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, ATIVIDADES);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        high_priority = (ImageView) rootView.findViewById(R.id.high_priority);
        medium_priority = (ImageView) rootView.findViewById(R.id.medium_priority);
        low_priority = (ImageView) rootView.findViewById(R.id.low_priority);
        high_priority.animate().rotation(180);
        medium_priority.animate().rotation(180);
        low_priority.animate().rotation(180);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.high_priority:
                        high_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
                        medium_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        low_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        break;
                    case R.id.medium_priority:
                        high_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        medium_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
                        low_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        break;
                    case R.id.low_priority:
                        high_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        medium_priority.animate().scaleX(1).scaleY(1).rotation(180).setDuration(200);
                        low_priority.animate().scaleX(1.3f).scaleY(1.3f).rotation(360).setDuration(200);
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

    private void configureAutoComplete(){
        ATIVIDADES = new ArrayList<>();

        for (int i = 0; i < atividades.size(); i++) {
            ATIVIDADES.add(atividades.get(i).getNome());
        }
    }

    private void showProgressDialog(){
        final int TIME = 1000;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Adicionando atividade...");
        dialog.setCancelable(false);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                fragmentTransaction.commit();
            }
        }, TIME);
    }

    private void setUp() {
        atividades = new ArrayList<>();

        Atividade atv1 = new Atividade("Jogar bola na UFCG", Prioridade.ALTA);
        atv1.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv2 = new Atividade("Fazer cocô", Prioridade.BAIXA);
        atv2.registrarNovoHorario(new Horario(8, new GregorianCalendar()));

        Atividade atv3 = new Atividade("Quebrar o dente", Prioridade.MEDIA);
        atv3.registrarNovoHorario(new Horario(5, new GregorianCalendar()));

        Atividade atv4 = new Atividade("Pular da janela", Prioridade.ALTA);
        atv4.registrarNovoHorario(new Horario(2, new GregorianCalendar()));
        atv4.registrarNovoHorario(new Horario(3, new GregorianCalendar()));

        Atividade atv5 = new Atividade("Quebrar a orelha", Prioridade.MEDIA);
        atv5.registrarNovoHorario(new Horario(1, new GregorianCalendar()));

        Atividade atv6 = new Atividade("Humilhar no LOL", Prioridade.MEDIA);
        atv6.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv7 = new Atividade("Cagar no DotA", Prioridade.MEDIA);
        atv7.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv8 = new Atividade("Morrer no CS", Prioridade.MEDIA);
        atv8.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        atividades.add(atv1);
        atividades.add(atv2);
        atividades.add(atv3);
        atividades.add(atv4);
        atividades.add(atv5);
        atividades.add(atv6);
        atividades.add(atv7);
        atividades.add(atv8);

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