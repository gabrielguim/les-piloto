package com.example.semtempo.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.RecentTasksAdapter;
import com.example.semtempo.utils.Utils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import model.Atividade;
import model.Horario;
import model.Prioridade;

public class SeeMoreFragment extends Fragment {

    private ListView allTasks;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_see_more, container, false);

        setFab();

        allTasks = (ListView) rootView.findViewById(R.id.all_tasks);
        List<Atividade> atividades = new ArrayList<>();

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

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setVisibility(View.INVISIBLE);
    }


}