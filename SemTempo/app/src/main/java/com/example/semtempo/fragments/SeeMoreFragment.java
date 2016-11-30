package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.semtempo.R;
import com.example.semtempo.adapters.AllTasksAdapter;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.semtempo.model.Atividade;

public class SeeMoreFragment extends Fragment {

    private ListView allTasks;
    private View rootView;
    private List<Atividade> atividades;
    private Spinner sort_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_see_more, container, false);

        setFab();
        setUp();
        setUpSpinner();

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setVisibility(View.INVISIBLE);
    }

    private void setUpSpinner(){

        sort_spinner = (Spinner) rootView.findViewById(R.id.filter_spinner);

        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("Menor prioridade - Maior prioridade")){
                    Utils.sortByPriority(atividades);
                    Collections.reverse(atividades);
                } else if (item.equals("Menos horas - Mais horas")){
                    Utils.sortByHours(atividades);
                } else if (item.equals("Maior prioridade - Menor prioridade")){
                    Utils.sortByPriority(atividades);
                } else if (item.equals("Mais horas - Menos horas")) {
                    Utils.sortByHours(atividades);
                    Collections.reverse(atividades);
                } else if (item.equals("Mais recente - Menos recente")){
                    Utils.sortByDate(atividades);
                } else {
                    Utils.sortByDate(atividades);
                    Collections.reverse(atividades);
                }

                allTasks.setAdapter(new AllTasksAdapter(getActivity(), atividades, rootView));
                Utils.setListViewHeightBasedOnChildren(allTasks);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Maior prioridade - Menor prioridade");
        categories.add("Menor prioridade - Maior prioridade");
        categories.add("Menos horas - Mais horas");
        categories.add("Mais horas - Menos horas");
        categories.add("Mais recente - Menos recente");
        categories.add("Menos recente - Mais recente");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sort_spinner.setAdapter(dataAdapter);
    }

    private void setUp() {
        allTasks = (ListView) rootView.findViewById(R.id.all_tasks);
        allTasks.setDivider(null);
        atividades = new ArrayList<>();

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseController.retrieveActivities(UsuarioController.getInstance().getCurrentUser().getDisplayName(), new OnGetDataListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {
                dialog.dismiss();

                atividades = data;

                if (getActivity() != null) {
                    allTasks.setAdapter(new AllTasksAdapter(getActivity(), atividades, rootView));
                    Utils.setListViewHeightBasedOnChildren(allTasks);
                }
            }
        });

    }


}