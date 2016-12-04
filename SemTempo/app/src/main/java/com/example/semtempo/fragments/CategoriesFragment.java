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
import android.widget.ListView;
import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.controllers.FirebaseController;
import com.example.semtempo.controllers.OnGetDataListener;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Tag;
import com.example.semtempo.services.AtividadeService;
import com.example.semtempo.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Rafael on 12/3/2016.
 */

public class CategoriesFragment extends Fragment {

    private final String[] categoriesColors = {"#A52A2A", "#6495ED", "#A9A9A9"};
    private List<Integer> chartColors;
    private ListView subtitles;
    private View rootView;
    private List<Atividade> activities;
    private final int ADD_ICON = R.drawable.ic_add_white_24dp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        TextView warn = (TextView) rootView.findViewById(R.id.no_task_warn);
        warn.setVisibility(View.INVISIBLE);
        setUp();
        setFab();


//        SwitchCompat simpleSwitch = (SwitchCompat) rootView.findViewById(R.id.mySwitch); // initiate Switch
//
//        simpleSwitch.setTextOn("On"); // displayed text of the Switch whenever it is in checked or on state
//        simpleSwitch.setTextOff("Off");
        return rootView;
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
                }

                closeDialog(dialog);
            }
        });

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

        Calendar cal = new GregorianCalendar();
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        TextView totalHoras = (TextView) rootView.findViewById(R.id.total_hours);
        Map<Tag, Integer> categoriesHours = getCategoriesHoursPerOption();

        totalHoras.setText("Horas investidas na semana: " + AtividadeService.getTotalSpentHours(activities));

        for (Map.Entry<Tag, Integer> entry : categoriesHours.entrySet()) {
            categories.add(entry.getKey().toString() + " - Total de horas: " + entry.getValue());
            totalHours += entry.getValue();
        }

        for (Map.Entry<Tag, Integer> entry : categoriesHours.entrySet()) {
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

    private Map<Tag, Integer> getCategoriesHoursPerOption(){
        String option = "";
        Map<Tag, Integer> categoriesHours;

        if(option.equals("Hist. Geral")){
            Calendar cal = new GregorianCalendar();
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            categoriesHours = AtividadeService.getTotalSpentHoursByCategoriesActWeek(activities, week);
        }else{
            categoriesHours = AtividadeService.getTotalSpentHoursByCategories(activities);
        }

        return categoriesHours;
    }
}
