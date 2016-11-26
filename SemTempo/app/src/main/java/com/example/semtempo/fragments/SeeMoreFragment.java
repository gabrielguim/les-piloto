package com.example.semtempo.fragments;

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
import com.example.semtempo.adapters.RecentTasksAdapter;
import com.example.semtempo.utils.Utils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;

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
                    Utils.sortByPriority(atividades, 0, atividades.size()-1);
                } else if (item.equals("Menos horas - Mais horas")){
                    Utils.sortByHours(atividades, 0, atividades.size()-1);
                }

                allTasks.setAdapter(new RecentTasksAdapter(getActivity(), atividades, rootView));
                Utils.setListViewHeightBasedOnChildren(allTasks);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Menos horas - Mais horas");
        categories.add("Mais horas - Menos horas");
        categories.add("Menor prioridade - Maior prioridade");
        categories.add("Maior prioridade - Menor prioridade");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sort_spinner.setAdapter(dataAdapter);
    }

    private void setUp() {
        allTasks = (ListView) rootView.findViewById(R.id.all_tasks);
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

        allTasks.setAdapter(new RecentTasksAdapter(getActivity(), atividades, rootView));
        Utils.setListViewHeightBasedOnChildren(allTasks);

    }


}