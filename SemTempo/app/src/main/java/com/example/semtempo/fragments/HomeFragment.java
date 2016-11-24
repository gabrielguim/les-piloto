package com.example.semtempo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.RecentTasksAdapter;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Atividade;
import model.Prioridade;

public class HomeFragment extends Fragment {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private ListView subtitles;
    private ListView recentTasks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setFab();
        plotChart();
        loadRecentTasks();

        return rootView;
    }

    private void setFab(){
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
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void loadRecentTasks(){
        recentTasks = (ListView) rootView.findViewById(R.id.recent_tasks);
        List<Atividade> atividades = new ArrayList<>();

        atividades.add(new Atividade("Jogar bola na UFCG", Prioridade.ALTA));
        atividades.add(new Atividade("Fazer cocô", Prioridade.BAIXA));
        atividades.add(new Atividade("Quebrar o dente", Prioridade.MEDIA));
        atividades.add(new Atividade("Pular da janela", Prioridade.ALTA));

        recentTasks.setAdapter(new RecentTasksAdapter(getActivity(), atividades, rootView));
        setListViewHeightBasedOnChildren(recentTasks);

    }

    private void plotChart(){
        subtitles = (ListView) rootView.findViewById(R.id.subtitles);
        int[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.BLACK};
        String[] valores = {"Jogar Bola na UFCGd 12d1 asdasd asd", "Peidard1 2da sd d12 1 ", "Fuasdsad 12d 12mar", "Cagar"};
        float[] perc = {30f, 20f, 15f, 10f};
        TextView perc_text =(TextView) rootView.findViewById(R.id.text_perc);

        subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, colors, perc, perc_text, rootView));
        subtitles.setDivider(null);
        setListViewHeightBasedOnChildren(subtitles);

        final FitChart fitChart = (FitChart) rootView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(30f, Color.RED));
        values.add(new FitChartValue(20f, Color.GREEN));
        values.add(new FitChartValue(15f, Color.BLUE));
        values.add(new FitChartValue(10f, Color.BLACK));

        fitChart.setValues(values);
    }

}