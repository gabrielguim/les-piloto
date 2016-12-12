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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.semtempo.R;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Lucas on 12/10/2016.
 */

public class TagsFragment extends Fragment {

    private final String[] categoriesColors = {"#EB4E00", "#EE6773", "#667FFF", "#D3D3D3"};
    private List<Integer> chartColors;
    private ListView subtitles;
    private View rootView;
    private List<Atividade> activities;
    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private ToggleButton toggle;
    private String option;
    private Spinner spinnerTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_tags, container, false);
        option = "Hist. Geral";
        TextView warn = (TextView) rootView.findViewById(R.id.no_task_warn);
        warn.setVisibility(View.INVISIBLE);
        setUp();
        setFab();
        initToogleButton();
        return rootView;
    }

    private void initToogleButton(){
        toggle = (ToggleButton) rootView.findViewById(R.id.toogle_hist_type);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    option = "Hist. Week";
                } else {
                    option = "Hist. Geral";
                }

                plotChart();
            }
        });
    }

    private void setUpSpinner(){
        spinnerTags = (Spinner) rootView.findViewById(R.id.spinnerTags);

        List<String> tags =  listaDeTags();//new ArrayList<>();//listaDeTags();

        if (getActivity() != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tags);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerTags.setAdapter(dataAdapter);

        }

        AdapterView.OnItemSelectedListener adapter = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final int TIME = 1500;
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Filtro por tag " + spinnerTags.getSelectedItem());
                dialog.setCancelable(false);
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        plotChart();
                        dialog.dismiss();
                    }
                }, TIME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerTags.setOnItemSelectedListener(adapter);


    }

    private List<String> listaDeTags() {
        List<String> tags = new ArrayList<>();
        Set<String> aux = new HashSet<>();
        Atividade atv;
        for (int i = 0; i < activities.size(); i++) {
            atv = activities.get(i);
            if(atv.getTags()!= null && atv.getTags().size() > 0)
                aux.addAll(atv.getTags());
        }
        tags.addAll(aux);
        return tags;
    }

    private void setUp(){
        activities = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();

        final int TIME = 4000; //Timeout
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        initDialog(dialog);

        FirebaseController.retrieveActivities(currentUser.getDisplayName(), new OnGetDataListener() {
            @Override
            public void onStart() {}

            @Override
            public void onSuccess(final List<Atividade> data) {

                changeWarnVisibility(!data.isEmpty());
                activities = data;


                if (data != null) {
                    fillChartCollors();
                    plotChart();
                    setUpSpinner();
                }

                closeDialog(dialog);
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, TIME);

    }

    private void initDialog(ProgressDialog dialog){
        dialog.setMessage("Carregando dados...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void closeDialog(ProgressDialog dialog){
        dialog.dismiss();
    }

    private void changeWarnVisibility(boolean condition){
        TextView warn = (TextView) rootView.findViewById(R.id.no_task_warn);
        if (!condition){
            warn.setVisibility(View.VISIBLE);
        } else {
            warn.setVisibility(View.INVISIBLE);
        }
    }

    private void fillChartCollors(){
        chartColors = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            int color = Color.parseColor(categoriesColors[i]);
            chartColors.add(color);
        }
    }

    private void plotChart(){
        subtitles = (ListView) rootView.findViewById(R.id.subtitles);
        TextView perc_text =(TextView) rootView.findViewById(R.id.text_perc);

        List<String> categories = new ArrayList<String>();
        List<Float> perc = new ArrayList<Float>();
        float totalHours = 0;

       // TextView totalHoras = (TextView) rootView.findViewById(R.id.total_hours);

        Map<Atividade, Integer> categoriesHours = getHoursPerAct();

        for (Map.Entry<Atividade, Integer> entry : categoriesHours.entrySet()) {

            categories.add(entry.getKey().getNomeDaAtv() + " - Total: " + entry.getValue() + " hrs");
            totalHours += entry.getValue();
        }

        setTotalHorasTextAndDescription();

        for (Map.Entry<Atividade, Integer> entry : categoriesHours.entrySet()) {
            perc.add((entry.getValue()/totalHours)*100f);
        }

        if (getActivity() != null) {
            subtitles.setAdapter(new SubtitlesAdapter(getActivity(), categories, chartColors, perc, perc_text, rootView));
            subtitles.setDivider(null);
            Utils.setListViewHeightBasedOnChildren(subtitles);
        }


        final FitChart fitChart = (FitChart) rootView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);

        fitChart.setMaxValue(100f);

        Collection<FitChartValue> values = new ArrayList<FitChartValue>();

        try{
            for (int i = 0; i < categories.size(); i++) {
                values.add(new FitChartValue(perc.get(i), chartColors.get(i)));
            }
        }catch(Exception e){

        }

        fitChart.setValues(values);
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

    private Map<Atividade, Integer> getHoursPerAct(){
        Map<Atividade, Integer> hoursOfAtvs;
        TextView totalHoras = (TextView) rootView.findViewById(R.id.total_hours);

        //List<Atividade> filtered = AtividadeService.filtraPorTag(activities,spinnerTags.getSelectedItem().toString());
        if(option.equals("Hist. Week")){
            Calendar cal = new GregorianCalendar();
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            hoursOfAtvs = AtividadeService.getTotalHorasPorAtvPorSemana(activities, week);
        }else{
            hoursOfAtvs = AtividadeService.getTotalHorasPorAtividade(activities);
        }

        return hoursOfAtvs;
    }

//    private List<Atividade> filtraPorTag(String tag){
//        List<Atividade> filtradas = new ArrayList<>();
//        for (Atividade atv: activities) {
//            if(atv.getTags() != null && atv.getTags().contains(tag)){
//                filtradas.add(atv);
//            }
//        }
//        return filtradas;
//    }

    private void setTotalHorasTextAndDescription(){
        TextView textChart = (TextView) rootView.findViewById(R.id.text_chart);
        if(option.equals("Hist. Week")){
            textChart.setText("Filtro por Tags da Semana");
        }else{
            textChart.setText("Filtro por Tags do Histórico");
        }
    }
}
