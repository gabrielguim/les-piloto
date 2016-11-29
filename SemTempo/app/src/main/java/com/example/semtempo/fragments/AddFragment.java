package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.semtempo.R;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.database.OnGetDataListener;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);

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

                    Calendar creation_date = new GregorianCalendar();

                    Horario horario = new Horario(Integer.parseInt(spent_time.getText().toString()), creation_date);
                    Atividade atv = new Atividade(autoCompleteTextView.getText().toString(), priority, horario);

                    FirebaseController.saveActivity(UsuarioController.getInstance().getCurrentUser().getDisplayName(), atv);

                    showProgressDialog();

                }

            }
        });


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
        ATIVIDADES = new ArrayList<String>();

        for (int i = 0; i < atividades.size(); i++) {
            if(!(ATIVIDADES.contains(atividades.get(i).getNomeDaAtv()))){
                ATIVIDADES.add(atividades.get(i).getNomeDaAtv());
            }
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        }, TIME);
    }

    private void setUp() {
        atividades = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();

        final int TIME = 3000; //Timeout
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseController.retrieveActivities(currentUser.getDisplayName(), new OnGetDataListener() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {
                dialog.dismiss();
                atividades = data;
                configureAutoComplete();
                if(getActivity() != null) {
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, ATIVIDADES);
                    autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.name_atv);
                    autoCompleteTextView.setThreshold(1);
                    autoCompleteTextView.setAdapter(adapter);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, TIME);

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