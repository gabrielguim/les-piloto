package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import im.dacer.androidcharts.LineView;

public class HistoryFragment extends Fragment {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private String[] weekDays = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
    private Map<Atividade, Integer> semanaAtual;
    private List<Atividade> atividades;
    private LineView lineView;
    private Spinner week_1;
    private Spinner week_2;
    private ListView week_subtitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        lineView = (LineView) rootView.findViewById(R.id.line_view);

        setUp();
        setFab();

        return rootView;
    }

    private void setUpSpinner(){

        week_1 = (Spinner) rootView.findViewById(R.id.filter_spinner1);
        week_2 = (Spinner) rootView.findViewById(R.id.filter_spinner2);

        TextView semanaAtual = (TextView) rootView.findViewById(R.id.current_week);
        Calendar cal = new GregorianCalendar();
        semanaAtual.setText("Semana atual: " + cal.get(Calendar.WEEK_OF_YEAR));

        List<String> categories = new ArrayList<>();
        List<String> semanas = listaDeSemanas();

        for (int i = 0; i < semanas.size(); i++) {
            categories.add(semanas.get(i));
        }

        if (getActivity() != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            week_1.setAdapter(dataAdapter);
            week_2.setAdapter(dataAdapter);
        }


        AdapterView.OnItemSelectedListener adapter = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final int TIME = 1500;
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Comparando semana " + week_1.getSelectedItem() + " e " + week_2.getSelectedItem());
                dialog.setCancelable(false);
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        plotChart(Integer.parseInt(week_1.getSelectedItem().toString()), Integer.parseInt(week_2.getSelectedItem().toString()));
                        dialog.dismiss();
                    }
                }, TIME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        week_1.setOnItemSelectedListener(adapter);
        week_2.setOnItemSelectedListener(adapter);

    }

    private List<String> listaDeSemanas(){
        List<String> semanas = new ArrayList<>();
        for (int i = 0; i < atividades.size(); i++) {
            int semana = atividades.get(i).getHorariosRealizDaAtv().getSemana();
            if (!semanas.contains(String.valueOf(semana)))
                semanas.add(String.valueOf(semana));
        }

        return semanas;
    }

    private void plotChart(int week1, int week2){
        ArrayList<String> chartWeekDays = new ArrayList<>();
        List<Integer> hoursWeek1;
        List<Integer> hoursWeek2;
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();

        week_subtitles = (ListView) rootView.findViewById(R.id.week_subtitles);

        for (int i = 0; i < weekDays.length; i++){
            chartWeekDays.add(weekDays[i]);
        }

        List<Atividade> lista1 = (List<Atividade>) AtividadeService.filterActivitiesByWeek(atividades, week1);
        hoursWeek1 = AtividadeService.filterByDayAndSpentHours(lista1);
        List<Atividade> lista2 = (List<Atividade>) AtividadeService.filterActivitiesByWeek(atividades, week2);
        hoursWeek2 = AtividadeService.filterByDayAndSpentHours(lista2);

        dataLists.add((ArrayList<Integer>) hoursWeek1);
        dataLists.add((ArrayList<Integer>) hoursWeek2);

        lineView.setBottomTextList(chartWeekDays);
        lineView.setDataList(dataLists);

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#66CCFF"));
        colors.add(Color.parseColor("#EB4E00"));
        int[] cores = {Color.parseColor("#66CCFF"), Color.parseColor("#EB4E00")};

        List<String> valores = new ArrayList<>();
        valores.add("Semana " + week1 + " - Total de horas: " + AtividadeService.getTotalSpentHours(lista1));
        valores.add("Semana " + week2 + " - Total de horas: " + AtividadeService.getTotalSpentHours(lista2));

        lineView.setColorArray(cores);
        lineView.setDrawDotLine(true);

        week_subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, colors, null, null, rootView));
        week_subtitles.setDivider(null);
        Utils.setListViewHeightBasedOnChildren(week_subtitles);
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);
        addFab.setVisibility(View.VISIBLE);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AddFragment(), "NewFragmentTag");
                ft.commit();
            }
        });
    }

    private void setUp(){
        atividades = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();

        final int TIME = 4000; //Timeout
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
                setUpSpinner();
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, TIME);

    }
}