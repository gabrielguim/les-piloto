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
import android.widget.Toast;

import com.example.semtempo.R;
import com.example.semtempo.adapters.RecentTasksAdapter;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.controllers.UsuarioController;
import com.example.semtempo.database.FirebaseController;
import com.example.semtempo.database.OnGetDataListener;
import com.example.semtempo.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.semtempo.controllers.AtividadeController;
import com.example.semtempo.controllers.model.Atividade;
import com.example.semtempo.controllers.model.Horario;
import com.example.semtempo.controllers.model.Prioridade;

import static com.example.semtempo.utils.Utils.unlockWaiter;
import static com.example.semtempo.utils.Utils.waitForThread;

public class HomeFragment extends Fragment {

    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private ListView subtitles;
    private ListView recentTasks;
    private TextView seeMore;
    private List<Atividade> atividades;
    private Map<Atividade, Integer> atividadesDaSemana;
    private String[] chartColors = {"#7B68EE", "#4B0082", "#000080", "#4169E1", "#4682B4", "#DB7093", "#CD5C5C", "#00CED1", "#FA8072", "#3CB371", "#FF8C00", "#F4A460", };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        seeMore = (TextView) rootView.findViewById(R.id.see_more);

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

        setUp();
        setFab();
        if (atividadesDaSemana != null)
            plotChart();
        loadRecentTasks();

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);
        addFab.setVisibility(View.VISIBLE);

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

    private void loadRecentTasks(){
        recentTasks = (ListView) rootView.findViewById(R.id.recent_tasks);
        List<Atividade> atividades_recentes= new ArrayList<>();

        int i = 0;
        while (i < 5){
            try {
                atividades_recentes.add(atividades.get(i));
                i++;
            }catch (Exception e){
                i++;
                System.out.println(e.getMessage());
            }
        }

        recentTasks.setAdapter(new RecentTasksAdapter(getActivity(), atividades_recentes, rootView));
        Utils.setListViewHeightBasedOnChildren(recentTasks);

    }

    private void plotChart(){
        subtitles = (ListView) rootView.findViewById(R.id.subtitles);
        TextView perc_text =(TextView) rootView.findViewById(R.id.text_perc);

        List<Integer> colors = new ArrayList<>();
        List<String> valores = new ArrayList<>();
        List<Float> perc = new ArrayList<>();
        float totalHours = 0;

        int index_values = 0;

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            valores.add(entry.getKey().getNomeDaAtv());
            totalHours += entry.getValue();
            colors.add(Color.parseColor(chartColors[index_values]));
            index_values++;
        }

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            perc.add((entry.getValue()/totalHours)*100f);
        }

        subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, colors, perc, perc_text, rootView));
        subtitles.setDivider(null);
        Utils.setListViewHeightBasedOnChildren(subtitles);

        final FitChart fitChart = (FitChart) rootView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);

        fitChart.setMaxValue(100f);

        Collection<FitChartValue> values = new ArrayList<>();

        for (int i = 0; i < valores.size(); i++) {
            values.add(new FitChartValue(perc.get(i), colors.get(i)));
        }

        fitChart.setValues(values);
    }

    private void setUp(){

        atividades = new ArrayList<>();
        GoogleSignInAccount currentUser = UsuarioController.getInstance().getCurrentUser();


        FirebaseController.retrieveActivities(currentUser.getDisplayName(), new OnGetDataListener() {
            @Override
            public void onStart() {
                //
            }

            @Override
            public void onSuccess(final List<Atividade> data) {
                System.out.println("gg");
                unlockWaiter();

            }
        });



        System.out.println("Init");
        System.out.println("End");
       // waitForThread();
        setUpWeek();

    }

    private void setUpWeek(){

        atividadesDaSemana = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(GregorianCalendar.MONTH);
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        cal.set(year, month, day);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        atividadesDaSemana = AtividadeController.filterActivitiesByWeekAndSpentHours(atividades, week);
    }

}