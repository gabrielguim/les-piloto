package com.example.semtempo.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.AllTasksAdapter;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.model.Atividade;

public class HomeFragment extends Fragment {

    // Lista de cores agradaveis
    private final String[] niceColors = {"#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF9966",
            "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6", "#29B8FF", "#009CEB",
            "#FF7029", "#EB4E00", "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6",
            "#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF7029",
            "#EB4E00", "#29B8FF", "#009CEB"};

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private ListView subtitles;
    private ListView recentTasks;
    private TextView seeMore;
    private List<Atividade> atividades;
    private Map<Atividade, Integer> atividadesDaSemana;
    private List<Integer> chartColors;
    private String flag;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        seeMore = (TextView) rootView.findViewById(R.id.see_more);
        TextView warn = (TextView) rootView.findViewById(R.id.no_task_warn);
        warn.setVisibility(View.INVISIBLE);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            flag = bundle.getString("flag");
        } else {
            flag = "not";
        }

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AddFragment(), "NewFragmentTag");
                ft.commit();
            }
        });

        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeMoreFragment fragment = new SeeMoreFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        setFab();
        setUp();

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);
        addFab.setVisibility(View.VISIBLE);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addFragment = new AddFragment();
                Bundle bundle = new Bundle();
                bundle.putString("flag", flag);
                addFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, addFragment , "NewFragmentTag");
                ft.commit();


            }
        });
    }

    private void loadRecentTasks(ProgressDialog dialog){
        recentTasks = (ListView) rootView.findViewById(R.id.recent_tasks);
        recentTasks.setDivider(null);
        List<Atividade> atividades_recentes= new ArrayList<>();
        Utils.sortByDate(atividades);

        int i = 0;
        while (i < 5){
            try {
                atividades_recentes.add(atividades.get(i));
                i++;
            }catch (Exception e){
                i++;
            }
        }

        if (atividades_recentes.isEmpty()){
            seeMore.setVisibility(View.INVISIBLE);
        } else {
            seeMore.setVisibility(View.VISIBLE);
        }

        if(getActivity() != null) {
            recentTasks.setAdapter(new AllTasksAdapter(getActivity(), atividades_recentes, rootView));
            Utils.setListViewHeightBasedOnChildren(recentTasks);
        }

        dialog.dismiss();

    }

    private void plotChart(){
        subtitles = (ListView) rootView.findViewById(R.id.subtitles);
        TextView perc_text =(TextView) rootView.findViewById(R.id.text_perc);

        List<String> valores = new ArrayList<>();
        List<Float> perc = new ArrayList<>();
        float totalHours = 0;

        Calendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        TextView totalHoras = (TextView) rootView.findViewById(R.id.total_hours);
        List<Atividade> atividades_da_semana = AtividadeService.filterActivitiesByWeek(atividades, week);
        totalHoras.setText("Horas investidas na semana: " + AtividadeService.getTotalSpentHours(atividades_da_semana));

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            valores.add(entry.getKey().getNomeDaAtv() + " - Total de horas: " + entry.getValue());
            totalHours += entry.getValue();
        }

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            perc.add((entry.getValue()/totalHours)*100f);
        }

        if (getActivity() != null) {
            subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, chartColors, perc, perc_text, rootView));
            subtitles.setDivider(null);
            Utils.setListViewHeightBasedOnChildren(subtitles);
        }


        final FitChart fitChart = (FitChart) rootView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);

        fitChart.setMaxValue(100f);

        Collection<FitChartValue> values = new ArrayList<>();

        try{
            for (int i = 0; i < valores.size(); i++) {
                values.add(new FitChartValue(perc.get(i), chartColors.get(i)));
            }
        }catch(Exception e){}

        fitChart.setValues(values);
    }

    private void fillChartCollors(){
        chartColors = new ArrayList<>();
        for (int i = 0; i < atividadesDaSemana.size(); i++) {
            int color = Color.parseColor(niceColors[i]);
            chartColors.add(color);
        }
    }


    private void setUp(){
        atividades = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();
        final int TIME = 3000;

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseController.retrieveActivities(currentUser.getDisplayName(), new OnGetDataListener() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {

                changeVisibility(!atividades.isEmpty());

                atividades = data;
                if (atividades!= null) {
                    Boolean registrouAtividadeOntem = AtividadeService.registerActivityYesterday(atividades);
                    if (registrouAtividadeOntem != null) {
                        System.out.println(registrouAtividadeOntem.toString());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("ontem", registrouAtividadeOntem.toString());
                        editor.commit();
                    }
                }
                setUpWeek();
                if (atividadesDaSemana != null) {
                    fillChartCollors();
                    plotChart();
                }

                loadRecentTasks(dialog);
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
                changeVisibility(!atividades.isEmpty());
            }
        }, TIME);

    }

    private void changeVisibility(boolean condicao){
        TextView warn = (TextView) rootView.findViewById(R.id.no_task_warn);
        if (!condicao){
            warn.setVisibility(View.VISIBLE);
        } else {
            warn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();
        super.onResume();
        setUp();
        loadRecentTasks(dialog);
    }

    private void setUpWeek(){

        atividadesDaSemana = new HashMap<>();

        Calendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        atividadesDaSemana = AtividadeService.groupActivitiesByWeekAndSpentHours(atividades, week);
    }

}